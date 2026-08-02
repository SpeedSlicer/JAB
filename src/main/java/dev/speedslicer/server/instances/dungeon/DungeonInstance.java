package dev.speedslicer.server.instances.dungeon;

import dev.speedslicer.api.dungeon.DungeonData;
import dev.speedslicer.server.instances.dungeon.events.RoomClearEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Area;
import net.minestom.server.coordinate.ChunkRange;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.EntityDeathEvent;
import net.minestom.server.instance.*;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.DimensionType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DungeonInstance {
    InstanceContainer sourceContainer;
    InstanceManager instanceManager;
    DungeonData dungeonData;

    List<DungeonSession> activeDungeonSessions;

    public DungeonInstance(DungeonData dungeonData) {
        this.dungeonData = dungeonData;
        instanceManager = MinecraftServer.getInstanceManager();
        Path worldPath = Path.of("data/" + dungeonData.id() + "/world");
        sourceContainer = instanceManager.createInstanceContainer(new AnvilLoader(worldPath, DimensionType.OVERWORLD.key()));
        sourceContainer.setChunkSupplier(LightingChunk::new);
        var chunks = new ArrayList<CompletableFuture<Chunk>>();

        ChunkRange.chunksInRange(0, 0, 2, (x, z) ->
                chunks.add(sourceContainer.loadChunk(x, z))
        );

        CompletableFuture.allOf(
                chunks.toArray(CompletableFuture[]::new)
        ).join();

        sourceContainer.setBlockArea(
                Area.cube(new Pos(0, 30, 0), 5),
                Block.STONE
        );

        LightingChunk.relight(
                sourceContainer,
                sourceContainer.getChunks()
        );
    }

    public void createAndTransport(List<UUID> players) {
        var newSession = new DungeonSession(instanceManager.createSharedInstance(sourceContainer), dungeonData, players);
        activeDungeonSessions.add(newSession);
        newSession.init();
    }
}
