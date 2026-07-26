package dev.speedslicer.api.jsonExample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.speedslicer.api.entity.EntityData;
import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.api.weapon.data.*;
import dev.speedslicer.server.main.Main;
import net.minestom.server.coordinate.Pos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ExampleGenerator {
    PlayerData playerDataExample;
    WeaponData weaponDataExample;
    EntityData entityDataExample;
    Gson gson;
    public ExampleGenerator() {
        playerDataExample = new PlayerData(UUID.randomUUID());
        weaponDataExample = new WeaponData(
                1,
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
        Map<String, JsonElement> x = new HashMap<>();
        x.put("glowing", new JsonPrimitive(true));
        entityDataExample = new EntityData(1, "john", "&6John", "minecraft:villager", List.of(), x);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }
    public void generateExamples() {
        try {
            generateExample(playerDataExample, "playerDataExample");
            generateExample(weaponDataExample, "weaponDataExample");
            generateExample(entityDataExample, "entityDataExample");

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
