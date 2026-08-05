package dev.speedslicer.server.instances.dungeon;

import dev.speedslicer.api.dungeon.DungeonData;
import dev.speedslicer.api.dungeon.RoomData;
import dev.speedslicer.api.entity.EntityData;
import dev.speedslicer.api.entity.stats.EntityStatType;
import dev.speedslicer.api.entity.stats.EntityStats;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.api.item.data.attribute.BoostType;
import dev.speedslicer.api.item.utils.ItemStackConstructor;
import dev.speedslicer.api.lootable.LootTableDataHandler;
import dev.speedslicer.api.player.util.PlayerDamageHandler;
import dev.speedslicer.server.anticheat.AnticheatConfig;
import dev.speedslicer.server.anticheat.warn.AnticheatWarning;
import dev.speedslicer.server.anticheat.warn.WarningLevel;
import dev.speedslicer.server.data.playerdata.ActivePlayerData;
import dev.speedslicer.server.instances.dungeon.events.RoomClearEvent;
import dev.speedslicer.server.main.Main;
import dev.speedslicer.server.main.ServerController;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.entity.EntityDeathEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;

import java.util.*;
import java.util.stream.Collectors;

public class DungeonSession {
    SharedInstance dungeonInstance;
    int roomNumber;
    DungeonData dungeonData;
    HashMap<UUID, EntityData> activeEnemies;
    List<UUID> players;
    ServerController controller = Main.getServerController();
    Pos doorPosA, doorPosB;
    public DungeonSession(SharedInstance dungeonInstance, DungeonData dungeonData, List<UUID> players) {
        this.dungeonData = dungeonData;
        this.dungeonInstance = dungeonInstance;
        roomNumber = 0;
        activeEnemies = new HashMap<>();
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
        dungeonInstance.eventNode().addListener(EntityAttackEvent.class, event -> {
            if (event.getEntity() instanceof Player && event.getTarget() instanceof Player) return;
            if (event.getTarget() instanceof LivingEntity) {
                if (event.getEntity() instanceof Player) {
                    ActivePlayerData playerData = controller.getPlayerDataManager().getPlayerData(event.getEntity().getUuid());
                    if (Math.abs(event.getTarget().getPosition().distance(event.getTarget().getPosition()))
                            < playerData.getPlayerBoosts().getStat(BoostType.RANGE) + AnticheatConfig.attackRangeBuffer) {
                        float damage = PlayerDamageHandler.getPlayerDamage(playerData);
                        float damageCrit = PlayerDamageHandler.getPlayerCrit(playerData);
                        if (damageCrit > 0) {
                            ParticlePacket critParticle = new ParticlePacket(
                                    Particle.CRIT,
                                    event.getTarget().getPosition(),
                                    Pos.ZERO,
                                    0.05f,
                                    16
                            );
                            dungeonInstance.sendGroupedPacket(critParticle);
                        }
                        ((LivingEntity) event.getTarget()).damage(Damage.fromEntity(event.getEntity(), damage + damageCrit));
                    }
                    else {
                        AnticheatWarning.sendWarningPlayer(event.getEntity().getUuid(), "Player hit beyond range!", WarningLevel.WARN);
                    }
                }
                else {
                    EntityData stats = activeEnemies.get(event.getEntity().getUuid());
                    ((LivingEntity) event.getTarget()).damage(Damage.fromEntity(event.getEntity(), stats.entityStats().stats().get(EntityStatType.DAMAGE).floatValue()));
                }
            }
        });
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void destroyEnemy(UUID enemy) {
        if (activeEnemies.containsKey(enemy)) {
            activeEnemies.remove(enemy);
            Objects.requireNonNull(dungeonInstance.getEntityByUuid(enemy)).remove();
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
