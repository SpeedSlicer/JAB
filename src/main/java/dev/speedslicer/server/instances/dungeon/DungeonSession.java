package dev.speedslicer.server.instances.dungeon;

import dev.speedslicer.api.dungeon.DungeonData;
import dev.speedslicer.api.dungeon.RoomData;
import dev.speedslicer.api.item.utils.ItemStackConstructor;
import dev.speedslicer.api.lootable.LootTableDataHandler;
import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.api.player.PlayerSlot;
import dev.speedslicer.server.data.playerdata.ActivePlayerData;
import dev.speedslicer.server.instances.dungeon.events.RoomClearEvent;
import dev.speedslicer.server.main.Main;
import dev.speedslicer.server.main.ServerController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.advancements.Notification;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.EntityDeathEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DungeonSession {
    SharedInstance dungeonInstance;
    int roomNumber;
    DungeonData dungeonData;
    List<UUID> activeEnemies;
    List<UUID> players;
    ServerController controller = Main.getServerController();
    Pos doorPosA, doorPosB;
    public DungeonSession(SharedInstance dungeonInstance, DungeonData dungeonData, List<UUID> players) {
        this.dungeonData = dungeonData;
        this.dungeonInstance = dungeonInstance;
        roomNumber = 0;
        activeEnemies = new ArrayList<>();
        this.players = players;
        dungeonInstance.eventNode().addListener(EntityDeathEvent.class, event -> {
            if (!players.contains(event.getEntity().getUuid())) {
                destroyEnemy(event.getEntity().getUuid());
            }
        });
        dungeonInstance.eventNode().addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            final ActivePlayerData playerData = controller.getPlayerDataManager().getPlayerData(player.getUuid());
            player.getInventory().clear();
            var item = ItemStackConstructor.constructItemFromData(
                    controller.getItemRegistry().getItem(playerData.getHandItem()));
            player.getInventory().setItemStack(0, item);
            player.setHeldItemSlot((byte) 0);
            player.setGameMode(GameMode.ADVENTURE);
        });
        dungeonInstance.eventNode().addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();
            players.remove(player.getUuid());
        });
        dungeonInstance.eventNode().addListener(RoomClearEvent.class, event -> {
            final RoomData currentRoom = event.getRoom();
            final RoomData nextRoom = event.getNextRoom();
            if (!currentRoom.completionLootTable().isEmpty()) {
                currentRoom.completionLootTable()
                        .forEach(t ->
                                players.forEach(p -> LootTableDataHandler
                                        .addRewards(t, p, controller.getPlayerDataManager())));
            }
            if (nextRoom == null) {
                // completion
            }
            else {
                doorPosA = nextRoom.exitDoorA();
                doorPosB = nextRoom.exitDoorB();
            }

        });
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void destroyEnemy(UUID enemy) {
        if (activeEnemies.contains(enemy)) {
            activeEnemies.remove(enemy);
            dungeonInstance.getEntityByUuid(enemy).remove();
        }
        else {
            Main.getLogger().warn("Entity {} does not exist in activeEnemies", enemy);
        }
        if (activeEnemies.isEmpty()) {
            dungeonInstance.eventNode().call(new RoomClearEvent(dungeonInstance,
                    getRoomOrNull(roomNumber),
                    getRoomOrNull(roomNumber + 1)));
        }
    }
    public RoomData getRoomOrNull(int index) {
        if (index < 0 || index >= dungeonData.rooms().size()) {
            return null;
        }

        return dungeonData.rooms().get(index);
    }

    public void init() {
        for (UUID player : players) {
            if (dungeonInstance.getPlayerByUuid(player) != null) {
                dungeonInstance.getPlayerByUuid(player).setInstance(dungeonInstance);
            }
            else {
                Main.getLogger().error("{} doesn't exist, trying to transport to session still?", player);
            }
        }
    }
}
