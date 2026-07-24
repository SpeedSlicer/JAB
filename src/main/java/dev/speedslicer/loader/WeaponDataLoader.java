package dev.speedslicer.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.speedslicer.api.weapon.WeaponData;
import dev.speedslicer.main.Main;
import dev.speedslicer.main.ServerController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class WeaponDataLoader {

    private final Gson gson;

    public WeaponDataLoader() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void bootstrapLoad(ServerController serverController) throws IOException {
        Main.getLogger().info("Weapon Item Bootstrap Load");

        Path folder = Path.of("data", "weapons");
        Files.createDirectories(folder);

        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(this::isJsonFile)
                    .forEach(path -> loadWeapon(path, serverController));
        }
    }

    private boolean isJsonFile(Path path) {
        return path.getFileName()
                .toString()
                .toLowerCase()
                .endsWith(".json");
    }

    private void loadWeapon(
            Path path,
            ServerController serverController
    ) {
        Main.getLogger().info("Loading weapon {}", path);

        try {
            String json = Files.readString(path);
            WeaponData weaponData = gson.fromJson(json, WeaponData.class);

            if (weaponData == null) {
                Main.getLogger().warn("Weapon file produced null: {}", path);
                return;
            }

            if (weaponData.id() == null || weaponData.id().isBlank()) {
                Main.getLogger().warn("Weapon has no ID: {}", path);
                return;
            }

            serverController.getWeaponRegistry()
                    .register(weaponData.id(), weaponData);

            Main.getLogger().info(
                    "Loaded weapon {} from {}",
                    weaponData.id(),
                    path
            );
        } catch (IOException exception) {
            Main.getLogger().error(
                    "Could not read weapon file {}",
                    path,
                    exception
            );
        } catch (RuntimeException exception) {
            Main.getLogger().error(
                    "Could not parse weapon file {}",
                    path,
                    exception
            );
        }
    }
}