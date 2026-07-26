package dev.speedslicer.server.main;

import dev.speedslicer.api.entity.utils.EntityDataConstructor;
import dev.speedslicer.api.item.utils.ItemStackConstructor;
import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.server.bootstrap.data.entity.EntityAIDataLoader;
import dev.speedslicer.server.bootstrap.data.entity.EntityDataLoader;
import dev.speedslicer.server.bootstrap.data.items.ItemDataLoader;
import dev.speedslicer.server.bootstrap.data.playerdata.PlayerDataManager;
import dev.speedslicer.server.bootstrap.data.weapons.WeaponDataLoader;
import dev.speedslicer.server.bootstrap.registry.EntityAIRegistry;
import dev.speedslicer.server.bootstrap.registry.EntityRegistry;
import dev.speedslicer.server.bootstrap.registry.ItemRegistry;
import dev.speedslicer.server.bootstrap.registry.WeaponRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.advancements.Notification;
import net.minestom.server.coordinate.Area;
import net.minestom.server.coordinate.ChunkRange;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.EntityAI;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServerController {
    WeaponDataLoader weaponDataLoader;
    ItemDataLoader itemDataLoader;
    EntityDataLoader entityDataLoader;
    EntityAIDataLoader entityAIDataLoader;

    WeaponRegistry weaponRegistry;
    EntityRegistry entityRegistry;
    EntityAIRegistry entityAIRegistry;

    InstanceContainer instanceContainer; // TODO replac with actual lobby system
    PlayerDataManager playerDataManager;

    public ServerController() throws IOException {
        weaponRegistry = new WeaponRegistry();
        entityRegistry = new EntityRegistry();
        entityAIRegistry = new EntityAIRegistry();

        weaponDataLoader = new WeaponDataLoader();
        itemDataLoader = new ItemDataLoader();
        entityDataLoader = new EntityDataLoader();
        entityAIDataLoader = new EntityAIDataLoader();

        playerDataManager = new PlayerDataManager();

        itemDataLoader.bootstrapLoad(this);
        weaponDataLoader.bootstrapLoad(this);
        entityDataLoader.bootstrapLoad(this);
        entityAIDataLoader.bootstrapLoad(this);
        MinecraftServer minecraftServer = MinecraftServer.init();
        SetupLobby();
        SetupGlobalPackets();
        minecraftServer.start("0.0.0.0", 25565);
    }

    public void SetupLobby() {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        instanceContainer = instanceManager.createInstanceContainer();

        instanceContainer.setChunkSupplier(LightingChunk::new);

        var chunks = new ArrayList<CompletableFuture<Chunk>>();

        ChunkRange.chunksInRange(0, 0, 2, (x, z) ->
                chunks.add(instanceContainer.loadChunk(x, z))
        );

        CompletableFuture.allOf(
                chunks.toArray(CompletableFuture[]::new)
        ).join();

        instanceContainer.setBlockArea(
                Area.cube(new Pos(0, 30, 0), 5),
                Block.STONE
        );

        LightingChunk.relight(
                instanceContainer,
                instanceContainer.getChunks()
        );
        instanceContainer.eventNode().addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            PlayerData playerData = playerDataManager.getPlayerData(player.getUuid());
            player.getInventory().clear();
            var item = ItemStackConstructor.constructItemFromData(
                    weaponRegistry.get(playerData.getSelectedWeapon())
            );
            player.getInventory().setItemStack(0, item);
            player.setHeldItemSlot((byte) 0);
        });
        instanceContainer.eventNode().addListener(EntityAttackEvent.class, event -> {
            if (event.getTarget() instanceof LivingEntity target) {
                target.damage(Damage.fromEntity(event.getEntity(), 4));
            }
        });

        Main.getLogger().info("Lobby loaded and lighting calculated");

        Pos spawnPosition = new Pos(0D, 42D, 0D);
        EntityCreature john = EntityDataConstructor.generateEntityCreatureFromData(
                entityRegistry.get("john"),
                entityAIRegistry,
                weaponRegistry
        );
        john.setInstance(instanceContainer, spawnPosition);
    }

    public void SetupGlobalPackets() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            Main.getLogger().info("Player " + player.getUsername() + " connected with UUID " + player.getUuid());
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
            playerDataManager.registerPlayer(player.getUuid());
        });
        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            // TODO Replace this block
            if (!playerDataManager.getPlayerData(player.getUuid()).hasCompletedTutorial()) { // TODO replace with an actual tutorial
                Notification notification = new Notification(
                        Component.text("Welcome to JAB!", NamedTextColor.GREEN),
                        FrameType.GOAL,
                        ItemStack.of(Material.GOLD_INGOT)
                );
                player.sendNotification(notification);
                playerDataManager.getPlayerData(player.getUuid()).completeTutorial();
                playerDataManager.getPlayerData(player.getUuid()).addWeapon("tutorial:basic_sword");
            }
        });
        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();
            Main.getLogger().info("Player {} disconnected with UUID {}", player.getUsername(), player.getUuid());
            playerDataManager.unregisterPlayer(player.getUuid());
        });
    }

    public WeaponRegistry getWeaponRegistry() {
        return weaponRegistry;
    }

    public ItemRegistry getItemRegistry() {
        return weaponRegistry;
    }

    public EntityRegistry getEntityRegistry() {
        return entityRegistry;
    }
    public EntityAIRegistry getEntityAIRegistry() {
        return entityAIRegistry;
    }
}
