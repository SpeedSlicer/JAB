package dev.speedslicer.api.weapon.utils;

import dev.speedslicer.api.weapon.data.WeaponData;
import dev.speedslicer.api.weapon.data.WeaponStats;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class WeaponItemStackConstructor {

    private WeaponItemStackConstructor() {
    }

    public static ItemStack constructItemFromWeaponData(WeaponData weaponData) {
        if (weaponData == null) {
            throw new IllegalArgumentException("weaponData cannot be null");
        }

        Material material = Material.fromKey(weaponData.material());

        if (material == null) {
            throw new IllegalArgumentException(
                    "Unknown material '" + weaponData.material()
                            + "' for weapon '" + weaponData.id() + "'"
            );
        }

        List<Component> lore = new ArrayList<>();

        addDescription(lore, weaponData.description());

        if (!lore.isEmpty()) {
            lore.add(Component.empty());
        }

        addWeaponInformation(lore, weaponData);
        lore.add(Component.empty());
        addWeaponStats(lore, weaponData.weaponStats());

        ItemStack item = ItemStack.of(material)
                .withAmount(1)
                .withMaxStackSize(1)
                .withCustomName(
                        Component.text(weaponData.name(), NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                )
                .withLore(lore)
                .withGlowing(
                        weaponData.weaponDisplayOptions().isEnchantGlint()
                )
                .with(
                        DataComponents.TOOLTIP_DISPLAY,
                        TooltipDisplay.EMPTY.without(
                                DataComponents.ATTRIBUTE_MODIFIERS
                        )
                );

        var displayOptions = weaponData.weaponDisplayOptions();

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

    private static void addDescription(
            List<Component> lore,
            List<String> description
    ) {
        if (description == null) {
            return;
        }

        for (String line : description) {
            if (line == null || line.isBlank()) {
                lore.add(Component.empty());
                continue;
            }

            lore.add(
                    Component.text(line, NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false)
            );
        }
    }

    private static void addWeaponInformation(
            List<Component> lore,
            WeaponData weaponData
    ) {
        lore.add(labelValue(
                "Type",
                prettify(weaponData.weaponType().name()),
                NamedTextColor.WHITE
        ));

        lore.add(labelValue(
                "Class",
                prettify(weaponData.weaponClass().name()),
                NamedTextColor.AQUA
        ));

        lore.add(labelValue(
                "World",
                prettify(weaponData.weaponWorld().name()),
                NamedTextColor.LIGHT_PURPLE
        ));
    }

    private static void addWeaponStats(
            List<Component> lore,
            WeaponStats stats
    ) {
        if (stats == null) {
            return;
        }

        lore.add(
                Component.text("Weapon Stats", NamedTextColor.YELLOW)
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)
        );

        addStat(
                lore,
                "Damage",
                formatNumber(stats.damage()),
                NamedTextColor.RED
        );

        addStat(
                lore,
                "Health Boost",
                formatSigned(stats.healthBoost()),
                NamedTextColor.GREEN
        );

        addStat(
                lore,
                "Speed",
                formatSigned(stats.speed()),
                NamedTextColor.AQUA
        );

        addStat(
                lore,
                "Critical Chance",
                formatPercent(stats.critChance()),
                NamedTextColor.GOLD
        );

        addStat(
                lore,
                "Luck",
                formatSigned(stats.luck()),
                NamedTextColor.LIGHT_PURPLE
        );
    }

    private static void addStat(
            List<Component> lore,
            String name,
            String value,
            NamedTextColor valueColor
    ) {
        lore.add(
                Component.text("  " + name + ": ", NamedTextColor.DARK_GRAY)
                        .append(Component.text(value, valueColor))
                        .decoration(TextDecoration.ITALIC, false)
        );
    }

    private static Component labelValue(
            String label,
            String value,
            NamedTextColor valueColor
    ) {
        return Component.text(label + ": ", NamedTextColor.DARK_GRAY)
                .append(Component.text(value, valueColor))
                .decoration(TextDecoration.ITALIC, false);
    }

    private static String formatNumber(double value) {
        if (value == Math.rint(value)) {
            return Long.toString((long) value);
        }

        return String.format(Locale.US, "%.2f", value)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
    }

    private static String formatSigned(double value) {
        if (value > 0) {
            return "+" + formatNumber(value);
        }

        return formatNumber(value);
    }

    private static String formatPercent(double value) {
        return formatNumber(value * 100) + "%";
    }

    private static String prettify(String enumName) {
        String[] words = enumName
                .toLowerCase(Locale.US)
                .split("_");

        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            if (!result.isEmpty()) {
                result.append(' ');
            }

            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1));
        }

        return result.toString();
    }
}