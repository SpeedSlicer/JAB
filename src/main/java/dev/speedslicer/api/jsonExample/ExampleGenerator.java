package dev.speedslicer.api.jsonExample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.speedslicer.api.entity.EntityData;
import dev.speedslicer.api.entity.ai.EntityAIData;
import dev.speedslicer.api.entity.items.EntityEquipmentData;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.api.item.data.ItemDisplayOptions;
import dev.speedslicer.api.item.data.attribute.BoostType;
import dev.speedslicer.api.item.data.attribute.ItemBoost;
import dev.speedslicer.api.item.data.tag.ItemTagData;
import dev.speedslicer.api.item.data.tag.ItemTagType;
import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.server.main.Main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ExampleGenerator {
    PlayerData playerDataExample;
    ItemData itemDataExample;
    EntityData entityDataExample;
    Gson gson;
    public ExampleGenerator() {
        playerDataExample = new PlayerData(UUID.randomUUID());
        Map<String, JsonElement> x = new HashMap<>();
        x.put("glowing", new JsonPrimitive(true));
        x.put("customNameVisible", new JsonPrimitive(true));
        entityDataExample = new EntityData(
                1,
                "john",
                "&6John",
                "minecraft:villager",
                List.of(
                        new EntityAIData(
                                "aggressive_attack_player",
                                Map.of(
                                        "attackRange",
                                        new JsonPrimitive(4.0),
                                        "attackDelayMillis",
                                        new JsonPrimitive(2_000),
                                        "targetRange",
                                        new JsonPrimitive(10.0)
                                )
                        )
                ),
                x,
                new EntityEquipmentData(
                        null,
                        null,
                        null,
                        null,
                        "weapon:basic_sword",
                        null,
                        null,
                        null
                )
        );
        itemDataExample = new ItemData(
                3,
                "example",
                "registry",
                "Example Thing",
                List.of("Example thing"),
                "minecraft:iron_sword",
                1,
                1,
                new ItemDisplayOptions(true, List.of(), List.of(), List.of(), List.of()),
                new HashMap<>(),
                List.of(new ItemBoost(BoostType.DAMAGE, 2.1)),
                Map.of(
                        "minecraft:rarity", new JsonPrimitive("rare"),
                        "minecraft:repair_cost", new JsonPrimitive(0),
                        "minecraft:unbreakable", new JsonPrimitive(true)
                ),
                List.of(),
                List.of(),
                Map.of(
                        "jab:item_id", new ItemTagData(
                                ItemTagType.STRING,
                                new JsonPrimitive("registry:example"),
                                false,
                                List.of()
                        ),
                        "jab:level", new ItemTagData(
                                ItemTagType.INTEGER,
                                new JsonPrimitive(1),
                                false,
                                List.of()
                        ),
                        "jab:labels", new ItemTagData(
                                ItemTagType.STRING,
                                JsonParser.parseString("[\"example\", \"generated\"]"),
                                true,
                                List.of()
                        ),
                        "payload", new ItemTagData(
                                ItemTagType.NBT,
                                JsonParser.parseString(
                                        "{\"enabled\": true, \"source\": \"example\"}"
                                ),
                                false,
                                List.of("jab")
                        )
                ));
        gson = new GsonBuilder().setPrettyPrinting().create();
    }
    public void generateExamples() {
        try {
            generateExample(playerDataExample, "playerDataExample");
            generateExample(itemDataExample, "itemDataExample");
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
