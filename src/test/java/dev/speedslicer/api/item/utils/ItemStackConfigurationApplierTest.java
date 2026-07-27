package dev.speedslicer.api.item.utils;

import com.google.gson.Gson;
import dev.speedslicer.api.item.data.ItemData;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.ItemRarity;
import net.minestom.server.tag.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemStackConfigurationApplierTest {

    @BeforeAll
    static void initializeMinestom() {
        MinecraftServer.init();
    }

    @Test
    void appliesCodecBackedComponentsAndTypedTags() {
        ItemData data = new Gson().fromJson("""
                {
                  "version": 3,
                  "category": "test",
                  "id": "configured_item",
                  "material": "minecraft:iron_sword",
                  "amount": 2,
                  "maxStackSize": 16,
                  "components": {
                    "rarity": "rare",
                    "repair_cost": 7,
                    "unbreakable": true
                  },
                  "tags": {
                    "jab:level": {
                      "type": "INTEGER",
                      "value": 4
                    },
                    "jab:labels": {
                      "type": "STRING",
                      "list": true,
                      "value": ["test", "weapon"]
                    },
                    "payload": {
                      "type": "NBT",
                      "path": ["jab"],
                      "value": {"enabled": true}
                    }
                  }
                }
                """, ItemData.class);

        ItemStack item = ItemStackConstructor.constructItemFromData(data);

        assertEquals(2, item.amount());
        assertEquals(16, item.maxStackSize());
        assertEquals(ItemRarity.RARE, item.get(DataComponents.RARITY));
        assertEquals(7, item.get(DataComponents.REPAIR_COST));
        assertTrue(item.has(DataComponents.UNBREAKABLE));
        assertEquals(4, item.getTag(Tag.Integer("jab:level")));
        assertEquals(
                List.of("test", "weapon"),
                item.getTag(Tag.String("jab:labels").list())
        );
        assertNotNull(item.getTag(Tag.NBT("payload").path("jab")));
    }

    @Test
    void explicitlyHandlesEveryComponentWithoutAJsonCodec() {
        Set<String> codecLessComponents = DataComponent.values().stream()
                .filter(component -> component.codec() == null)
                .map(component -> component.key().asString())
                .collect(Collectors.toSet());

        assertEquals(
                Set.of(
                        "minecraft:creative_slot_lock",
                        "minecraft:additional_trade_cost",
                        "minecraft:map_post_processing"
                ),
                codecLessComponents
        );
    }
}
