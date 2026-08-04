package dev.speedslicer.server.data.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.speedslicer.api.APIVersion;
import dev.speedslicer.api.entity.EntityData;
import dev.speedslicer.server.ServerSettings;
import dev.speedslicer.server.data.FileUtil;
import dev.speedslicer.server.main.Main;
import dev.speedslicer.server.main.ServerController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class EntityDataLoader {

    private final Gson gson;

    public EntityDataLoader() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void bootstrapLoad(ServerController serverController) throws IOException {
        Main.getLogger().info("entity Item Bootstrap Load");

        Path folder = Path.of("data", "entities");
        Files.createDirectories(folder);
        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(FileUtil::isJsonFile)
                    .forEach(path -> loadEntity(path, serverController));
        }
    }

    private void loadEntity(
            Path path,
            ServerController serverController
    ) {
        Main.getLogger().info("Loading entity {}", path);

        try {
            String json = Files.readString(path);
            EntityData entityData = gson.fromJson(json, EntityData.class);

            if (entityData == null) {
                Main.getLogger().warn("Entity file produced null: {}", path);
                return;
            }

            if (entityData.version() != APIVersion.entityDataVersion) {
                Main.getLogger().warn("Loading non-similar version of entity data for {}, update your JSON!", entityData.id());
                if (ServerSettings.safeMode) {
                    Main.getLogger().error("Safe mode on! Aborting load");
                    throw new RuntimeException();
                }
            }

            if (entityData.id() == null || entityData.id().isBlank()) {
                Main.getLogger().warn("Entity has no ID: {}", path);
                return;
            }

            serverController.getEntityRegistry()
                    .register(entityData.id(), entityData);

            Main.getLogger().info(
                    "Loaded entity {} from {}",
                    entityData.id(),
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
