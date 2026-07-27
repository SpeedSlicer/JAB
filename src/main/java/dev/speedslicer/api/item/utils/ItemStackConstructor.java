package dev.speedslicer.api.item.utils;

import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.api.item.data.ItemDisplayOptions;
import dev.speedslicer.api.item.data.attribute.BoostType;
import dev.speedslicer.api.item.data.attribute.ItemBoost;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ItemStackConstructor {

    private ItemStackConstructor() {
    }

    public static ItemStack constructItemFromData(ItemData itemData) {
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
                .withAmount(itemData.amount())
                .withMaxStackSize(itemData.maxStackSize());

        if (itemData.name() != null && !itemData.name().isBlank()) {
            item = item.withCustomName(
                    Component.text(itemData.name(), NamedTextColor.GOLD)
                            .decoration(TextDecoration.ITALIC, false)
            );
        }

        List<Component> lore = createDescription(itemData.description());
        addItemBoosts(lore, itemData.getItemBoosts());

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

        return ItemStackConfigurationApplier.apply(item, itemData);
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

    private static void addItemBoosts(
            List<Component> lore,
            List<ItemBoost> itemBoosts
    ) {
        if (itemBoosts == null || itemBoosts.isEmpty()) {
            return;
        }

        List<ItemBoost> validBoosts = itemBoosts.stream()
                .filter(boost ->
                        boost != null && boost.boostType() != null
                )
                .toList();

        if (validBoosts.isEmpty()) {
            return;
        }

        if (!lore.isEmpty()) {
            lore.add(Component.empty());
        }

        lore.add(
                Component.text("Item Attributes", NamedTextColor.YELLOW)
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)
        );

        for (ItemBoost itemBoost : validBoosts) {
            BoostType boostType = itemBoost.boostType();

            lore.add(
                    Component.text(
                                    "  " + prettify(boostType.name()) + ": ",
                                    NamedTextColor.DARK_GRAY
                            )
                            .append(
                                    Component.text(
                                            formatSigned(itemBoost.amount()),
                                            boostColor(boostType)
                                    )
                            )
                            .decoration(TextDecoration.ITALIC, false)
            );
        }
    }

    private static NamedTextColor boostColor(BoostType boostType) {
        return switch (boostType) {
            case DAMAGE -> NamedTextColor.RED;
            case HEALTH -> NamedTextColor.GREEN;
        };
    }

    private static String formatSigned(double value) {
        if (value > 0) {
            return "+" + formatNumber(value);
        }

        return formatNumber(value);
    }

    private static String formatNumber(double value) {
        if (value == Math.rint(value)) {
            return Long.toString((long) value);
        }

        return String.format(Locale.US, "%.2f", value)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
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
