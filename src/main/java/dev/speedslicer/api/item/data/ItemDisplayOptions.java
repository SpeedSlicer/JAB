package dev.speedslicer.api.item.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemDisplayOptions {
    @SerializedName(value = "enchantGlint", alternate = "isEnchantGlint")
    private boolean enchantGlint;
    private List<Float> floats;
    private List<Boolean> flags;
    private List<String> strings;
    private List<ItemModelColor> colors;


    public ItemDisplayOptions(
            boolean enchantGlint,
            List<Float> floats,
            List<Boolean> flags,
            List<String> strings,
        List<? extends ItemModelColor> colors
    ) {
        this.enchantGlint = enchantGlint;
        this.floats = floats == null ? List.of() : List.copyOf(floats);
        this.flags = flags == null ? List.of() : List.copyOf(flags);
        this.strings = strings == null ? List.of() : List.copyOf(strings);
        this.colors = colors == null ? List.of() : List.copyOf(colors);
    }

    public boolean isEnchantGlint() {
        return enchantGlint;
    }

    public List<Float> floats() {
        return floats == null ? List.of() : List.copyOf(floats);
    }

    public List<Boolean> flags() {
        return flags == null ? List.of() : List.copyOf(flags);
    }

    public List<String> strings() {
        return strings == null ? List.of() : List.copyOf(strings);
    }

    public List<ItemModelColor> colors() {
        return colors == null ? List.of() : List.copyOf(colors);
    }
}
