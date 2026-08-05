package dev.speedslicer.server.data.dungeon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.speedslicer.api.APIVersion;
import dev.speedslicer.api.dungeon.DungeonData;
import dev.speedslicer.api.entity.EntityData;
import dev.speedslicer.api.item.ItemCategories;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.server.ServerSettings;
import dev.speedslicer.server.bootstrap.registry.impl.DungeonRegistry;
import dev.speedslicer.server.data.FileUtil;
import dev.speedslicer.server.main.Main;
import dev.speedslicer.server.main.ServerController;
import net.minestom.server.coordinate.Area;
import net.minestom.server.coordinate.ChunkRange;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.DimensionType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class DungeonDataLoader {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public void bootstrapLoad(ServerController serverController)
            throws IOException {
        Path dungeonsData = Path.of("data", "dungeons");

            Files.createDirectories(dungeonsData);
            try (Stream<Path> paths = Files.walk(dungeonsData)) {
                paths.filter(Files::isRegularFile)
                        .filter(FileUtil::isJsonFile)
                        .forEach(path -> loadDungeon(path, serverController));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    private void loadDungeon(
            Path path,
            ServerController serverController
    ) {
        Main.getLogger().info("Loading dungeon {}", path);

        try {
            String json = Files.readString(path);
            DungeonData dungeonData = gson.fromJson(json, DungeonData.class);

            if (dungeonData == null) {
                Main.getLogger().warn("Dungeon file produced null: {}", path);
                return;
            }

            if (dungeonData.version() != APIVersion.dungeonDataVersion) {
                Main.getLogger().warn("Loading non-similar version of dungeon data for {}, update your JSON!", dungeonData.id());
                if (ServerSettings.safeMode) {
                    Main.getLogger().error("Safe mode on! Aborting load");
                    throw new RuntimeException();
                }
            }

            if (dungeonData.id() == null || dungeonData.id().isBlank()) {
                Main.getLogger().warn("Dungeon has no ID: {}", path);
                return;
            }

            serverController.getDungeonRegistry()
                    .register(dungeonData.id(), dungeonData);

            Main.getLogger().info(
                    "Loaded dungeon {} from {}",
                    dungeonData.id(),
                    path
            );
        } catch (IOException exception) {
            Main.getLogger().error(
                    "Could not read entity file {}",
                    path,
                    exception
            );
        } catch (RuntimeException exception) {
            Main.getLogger().error(
                    "Could not parse entity file {}",
                    path,
                    exception
            );
        }
    }
}

