package dev.speedslicer.server.bootstrap.data.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.speedslicer.api.APIVersion;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.server.ServerSettings;
import dev.speedslicer.server.main.Main;
import dev.speedslicer.server.main.ServerController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public final class ItemDataLoader {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public void bootstrapLoad(ServerController serverController)
            throws IOException {
        Main.getLogger().info("Generic item bootstrap load");

        Path folder = Path.of("data", "items");
        Files.createDirectories(folder);

        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(this::isJsonFile)
                    .forEach(path -> loadItem(path, serverController));
        }
    }

    private boolean isJsonFile(Path path) {
        return path.getFileName()
                .toString()
                .toLowerCase()
                .endsWith(".json");
    }

    private void loadItem(
            Path path,
            ServerController serverController
    ) {
        Main.getLogger().info("Loading item {}", path);

        try {
            ItemData itemData = gson.fromJson(
                    Files.readString(path),
                    ItemData.class
            );

            if (itemData == null) {
                Main.getLogger().warn("Item file produced null: {}", path);
                return;
            }

            if (itemData.version() != APIVersion.itemDataVersion) {
                Main.getLogger().warn(
                        "Loading item data version {} for {}; expected {}",
                        itemData.version(),
                        itemData.id(),
                        APIVersion.itemDataVersion
                );

                if (ServerSettings.safeMode) {
                    throw new IllegalStateException(
                            "Safe mode rejected item " + itemData.id()
                    );
                }
            }

            if (itemData.id() == null || itemData.id().isBlank()) {
                Main.getLogger().warn("Item has no ID: {}", path);
                return;
            }

            serverController.getItemRegistry()
                    .register(itemData.id(), itemData);

            Main.getLogger().info(
                    "Loaded item {} from {}",
                    itemData.id(),
                    path
            );
        } catch (IOException exception) {
            Main.getLogger().error(
                    "Could not read item file {}",
                    path,
                    exception
            );
        } catch (RuntimeException exception) {
            Main.getLogger().error(
                    "Could not parse item file {}",
                    path,
                    exception
            );
        }
    }
}
