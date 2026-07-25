package dev.speedslicer.api.jsonExample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.api.weapon.data.*;
import dev.speedslicer.server.main.Main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExampleGenerator {
    PlayerData playerDataExample;
    WeaponData weaponDataExample;
    Gson gson;
    public ExampleGenerator() {
        playerDataExample = new PlayerData(UUID.randomUUID());
        weaponDataExample = new WeaponData(
                0,
                "example",
                "example",
                List.of("example"),
                WeaponType.SWORD,
                WeaponClass.WEAK,
                WeaponWorld.OVERWORLD,
                "minecraft:iron_sword",
                new WeaponStats(0,0d,0d,0d, 0d),
                new WeaponDisplayOptions(false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
        );
        gson = new GsonBuilder().setPrettyPrinting().create();
    }
    public void generateExamples() {
        try {
            generateExample(playerDataExample, "playerDataExample");
            generateExample(weaponDataExample, "weaponDataExample");
        }
        catch (Exception e) {
            Main.getLogger().error("One or more of the data failed construct!");
            e.printStackTrace();
        }

    }
    private void generateExample(Object object, String fileName) throws IOException {
        Path directory = Path.of("example", "json");
        Files.createDirectories(directory);

        Path file = directory.resolve(fileName + ".json");
        String json = gson.toJson(object);

        Files.writeString(
                file,
                json,
                StandardCharsets.UTF_8
        );
    }
}
