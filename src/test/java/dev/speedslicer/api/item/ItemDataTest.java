package dev.speedslicer.api.item;

import com.google.gson.Gson;
import dev.speedslicer.MinestomTestBootstrap;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.api.item.data.ItemDisplayOptions;
import dev.speedslicer.api.item.utils.ItemStackConstructor;
import dev.speedslicer.api.weapon.data.WeaponData;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemDataTest {

    @BeforeAll
    static void initializeMinestom() {
        MinestomTestBootstrap.ensureInitialized();
    }

    @Test
    void deserializesLegacyWeaponJsonIntoSharedItemModel() {
        String json = """
                {
                  "version": 1,
                  "id": "test:sword",
                  "name": "Test Sword",
                  "description": ["A weapon"],
                  "weaponType": "SWORD",
                  "weaponClass": "WEAK",
                  "weaponWorld": "OVERWORLD",
                  "material": "minecraft:iron_sword",
                  "weaponStats": {
                    "damage": 3,
                    "healthBoost": 0,
                    "speed": 0,
                    "critChance": 0,
                    "luck": 0
                  },
                  "weaponDisplayOptions": {
                    "isEnchantGlint": true,
                    "floats": [],
                    "flags": [],
                    "strings": [],
                    "colors": []
                  }
                }
                """;

        ItemData itemData = new Gson().fromJson(json, WeaponData.class);

        WeaponData weaponData = assertInstanceOf(
                WeaponData.class,
                itemData
        );
        assertEquals("test:sword", weaponData.id());
        assertEquals("minecraft:iron_sword", weaponData.material());
        assertEquals(3, weaponData.weaponStats().damage());
        assertTrue(weaponData.displayOptions().isEnchantGlint());
        assertEquals(
                Material.IRON_SWORD,
                ItemStackConstructor.constructItemFromData(itemData).material()
        );
    }

    @Test
    void buildsGenericItems() {
        ItemData itemData = new ItemData(
                1,
                "test:apple",
                "Apple",
                java.util.List.of("A generic item"),
                "minecraft:apple",
                16,
                new ItemDisplayOptions(
                        false,
                        java.util.List.of(),
                        java.util.List.of(),
                        java.util.List.of(),
                        java.util.List.of()
                ),
                java.util.Map.of()
        );

        var itemStack = ItemStackConstructor.constructItemFromData(itemData);

        assertEquals(Material.APPLE, itemStack.material());
        assertEquals(16, itemStack.maxStackSize());
    }
}
