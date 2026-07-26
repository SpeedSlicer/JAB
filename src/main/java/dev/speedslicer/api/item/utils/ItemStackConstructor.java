package dev.speedslicer.api.item.utils;

import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.api.item.data.ItemDisplayOptions;
import dev.speedslicer.api.weapon.data.WeaponData;
import dev.speedslicer.api.weapon.utils.WeaponItemStackConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public final class ItemStackConstructor {

    private ItemStackConstructor() {
    }

    public static ItemStack constructItemFromData(ItemData itemData) {
        if (itemData instanceof WeaponData weaponData) {
            return WeaponItemStackConstructor.constructItemFromWeaponData(
                    weaponData
            );
        }

        if (itemData == null) {
            throw new IllegalArgumentException("itemData cannot be null");
        }

        Material material = Material.fromKey(itemData.material());

        if (material == null) {
            throw new IllegalArgumentException(
                    "Unknown material '" + itemData.material()
                            + "' for item '" + itemData.id() + "'"
            );
        }

        ItemStack item = ItemStack.of(material)
                .withAmount(1)
                .withMaxStackSize(itemData.maxStackSize());

        if (itemData.name() != null && !itemData.name().isBlank()) {
            item = item.withCustomName(
                    Component.text(itemData.name(), NamedTextColor.GOLD)
                            .decoration(TextDecoration.ITALIC, false)
            );
        }

        List<Component> lore = createDescription(itemData.description());

        if (!lore.isEmpty()) {
            item = item.withLore(lore);
        }

        ItemDisplayOptions displayOptions = itemData.displayOptions();
        item = item.withGlowing(displayOptions.isEnchantGlint());

        boolean hasCustomModelData =
                !displayOptions.floats().isEmpty()
                        || !displayOptions.flags().isEmpty()
                        || !displayOptions.strings().isEmpty()
                        || !displayOptions.colors().isEmpty();

        if (hasCustomModelData) {
            item = item.withCustomModelData(
                    displayOptions.floats(),
                    displayOptions.flags(),
                    displayOptions.strings(),
                    new ArrayList<>(displayOptions.colors())
            );
        }

        return item;
    }

    private static List<Component> createDescription(
            List<String> description
    ) {
        List<Component> lore = new ArrayList<>();

        for (String line : description) {
            if (line == null || line.isBlank()) {
                lore.add(Component.empty());
            } else {
                lore.add(
                        Component.text(line, NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false)
                );
            }
        }

        return lore;
    }
}
