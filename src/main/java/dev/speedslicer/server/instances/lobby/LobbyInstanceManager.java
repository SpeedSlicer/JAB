package dev.speedslicer.server.instances.lobby;

import com.jodexindustries.jguiwrapper.api.gui.factory.GuiOptions;
import com.jodexindustries.jguiwrapper.api.gui.factory.GuiType;
import com.jodexindustries.jguiwrapper.minestom.MinestomGuiApi;
import com.jodexindustries.jguiwrapper.minestom.gui.types.advanced.MinestomAdvancedGui;
import dev.speedslicer.api.item.utils.ItemStackConstructor;
import dev.speedslicer.api.player.PlayerSlot;
import dev.speedslicer.server.data.playerdata.ActivePlayerData;
import dev.speedslicer.server.main.ServerController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.advancements.Notification;
import net.minestom.server.coordinate.Area;
import net.minestom.server.coordinate.ChunkRange;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LobbyInstanceManager {
    InstanceManager instanceManager;
    public List<SharedInstance> lobbyInstances;
    InstanceContainer baseLobbyContainer;
    ServerController controller;
    public LobbyInstanceManager(ServerController controller) {
        this.controller = controller;
        instanceManager = MinecraftServer.getInstanceManager();
        baseLobbyContainer = instanceManager.createInstanceContainer();

        baseLobbyContainer.setChunkSupplier(LightingChunk::new);

        lobbyInstances = new ArrayList<>();

        var chunks = new ArrayList<CompletableFuture<Chunk>>();

        ChunkRange.chunksInRange(0, 0, 2, (x, z) ->
                chunks.add(baseLobbyContainer.loadChunk(x, z))
        );

        CompletableFuture.allOf(
                chunks.toArray(CompletableFuture[]::new)
        ).join();

        baseLobbyContainer.setBlockArea(
                Area.cube(new Pos(0, 30, 0), 5),
                Block.STONE
        );

        LightingChunk.relight(
                baseLobbyContainer,
                baseLobbyContainer.getChunks()
        );
    }
    private SharedInstance constructLobbyNode () {
        SharedInstance construct = instanceManager.createSharedInstance(baseLobbyContainer);
        construct.eventNode().addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            ActivePlayerData playerData = controller.getPlayerDataManager().getPlayerData(player.getUuid());

            if (!playerData.getPlayerData().hasCompletedTutorial()) { // TODO replace with an actual tutorial
                Notification notification = new Notification(
                        Component.text("Welcome to JAB!", NamedTextColor.GREEN),
                        FrameType.GOAL,
                        ItemStack.of(Material.GOLD_INGOT)
                );

                playerData.addItem("weapon:basic_sword");
                playerData.setSlot(PlayerSlot.HAND, "weapon:basic_sword");

                player.sendNotification(notification);
                playerData.getPlayerData().completeTutorial();
            }

            // starting item thing
            player.getInventory().clear();

            var item = ItemStackConstructor.constructItemFromData(
                    controller.getItemRegistry().getItem(playerData.getHandItem()));
            player.getInventory().setItemStack(0, item);
            player.setHeldItemSlot((byte) 0);
            player.setGameMode(GameMode.ADVENTURE);
        });

        construct.eventNode().addListener(ItemDropEvent.class, event -> {
            event.setCancelled(true);
        });
        construct.eventNode().addListener(EntityAttackEvent.class, event -> {
        });
        construct.eventNode().addListener(RemoveEntityFromInstanceEvent.class, event -> {
            if (event.getInstance().getPlayers().isEmpty()) {
                instanceManager.unregisterInstance(construct);
                for (Entity entity : event.getInstance().getEntities()) {
                    if (!(entity instanceof Player)) {
                        entity.remove();
                    }
                }
                lobbyInstances.remove(construct);
            }
        });
        lobbyInstances.add(construct);
        return construct;
    }

    public SharedInstance getAvailableLobby() {
        for (SharedInstance lobby : lobbyInstances) {
            if (lobby.getPlayers().size() < LobbyConfiguration.maximumPlayerLobby) {
                return lobby;
            }
        }
        return constructLobbyNode();
    }

    public void movePlayerToLobby(Player player) {
        player.setInstance(getAvailableLobby());
    }
    public void movePlayerToLobby(Player player, int lobbyNumber) {
        player.setInstance(lobbyInstances.get(lobbyNumber));
    }
}
