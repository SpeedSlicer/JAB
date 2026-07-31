package dev.speedslicer.server.main;

import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.api.player.PlayerSlot;
import dev.speedslicer.server.data.entity.EntityAIDataLoader;
import dev.speedslicer.server.data.entity.EntityDataLoader;
import dev.speedslicer.server.data.items.ItemDataLoader;
import dev.speedslicer.server.data.playerdata.ActivePlayerData;
import dev.speedslicer.server.data.playerdata.PlayerDataManager;
import dev.speedslicer.server.bootstrap.registry.EntityAIRegistry;
import dev.speedslicer.server.bootstrap.registry.EntityRegistry;
import dev.speedslicer.server.bootstrap.registry.ItemRegistry;
import dev.speedslicer.server.instances.lobby.LobbyInstanceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.advancements.Notification;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.player.GameProfile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ServerController {
    ItemDataLoader itemDataLoader;
    EntityDataLoader entityDataLoader;
    EntityAIDataLoader entityAIDataLoader;

    ItemRegistry itemRegistry;
    EntityRegistry entityRegistry;
    EntityAIRegistry entityAIRegistry;

    PlayerDataManager playerDataManager;
    LobbyInstanceManager lobbyInstanceManager;
    InstanceContainer routeInstanceContainer;

    public ServerController() throws IOException {
        itemRegistry = new ItemRegistry();
        entityRegistry = new EntityRegistry();
        entityAIRegistry = new EntityAIRegistry();

        itemDataLoader = new ItemDataLoader();
        entityDataLoader = new EntityDataLoader();
        entityAIDataLoader = new EntityAIDataLoader();

        playerDataManager = new PlayerDataManager();

        itemDataLoader.bootstrapLoad(this);
        entityDataLoader.bootstrapLoad(this);
        entityAIDataLoader.bootstrapLoad(this);
        MinecraftServer minecraftServer = MinecraftServer.init();
        lobbyInstanceManager = new LobbyInstanceManager(this);

        routeInstanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer();
        SetupGlobalPackets();
        minecraftServer.start("0.0.0.0", 25565);
    }

    public void SetupGlobalPackets() {

        routeInstanceContainer.eventNode().addListener(PlayerSpawnEvent.class, event -> {
           event.getPlayer().setInstance(lobbyInstanceManager.getAvailableLobby());
        });

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            Main.getLogger().info("Player " + player.getUsername() + " connected with UUID " + player.getUuid());
            event.setSpawningInstance(routeInstanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
            playerDataManager.registerPlayer(player.getUuid());
        });
        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            // TODO Replace this block
            ActivePlayerData data = playerDataManager.getPlayerData(player.getUuid());
            if (!data.getPlayerData().hasCompletedTutorial()) { // TODO replace with an actual tutorial
                Notification notification = new Notification(
                        Component.text("Welcome to JAB!", NamedTextColor.GREEN),
                        FrameType.GOAL,
                        ItemStack.of(Material.GOLD_INGOT)
                );

                data.addItem("weapon:basic_sword");
                data.setSlot(PlayerSlot.HAND, "weapon:basic_sword");

                player.sendNotification(notification);
                data.getPlayerData().completeTutorial();
            }
        });
        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();
            Main.getLogger().info("Player {} disconnected with UUID {}", player.getUsername(), player.getUuid());
            playerDataManager.unregisterPlayer(player.getUuid());
        });
        globalEventHandler.addListener(AsyncPlayerPreLoginEvent.class, event -> {
            String username = event.getUsername();

            byte[] bytes = ("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8);
            UUID offlineUuid = UUID.nameUUIDFromBytes(bytes);

            event.setGameProfile(new GameProfile(offlineUuid, username));
        });
    }

    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    public EntityRegistry getEntityRegistry() {
        return entityRegistry;
    }
    public EntityAIRegistry getEntityAIRegistry() {
        return entityAIRegistry;
    }
    public PlayerDataManager getPlayerDataManager() {return playerDataManager;}
}
