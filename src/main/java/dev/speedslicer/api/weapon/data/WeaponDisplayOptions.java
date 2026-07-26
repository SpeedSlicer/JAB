package dev.speedslicer.api.weapon.data;

import dev.speedslicer.api.item.data.ItemDisplayOptions;

import java.util.List;

/**
 * @deprecated Use {@link ItemDisplayOptions}; display options are shared by all
 * items.
 */
@Deprecated
public class WeaponDisplayOptions extends ItemDisplayOptions {
    public WeaponDisplayOptions(
            boolean enchantGlint,
            List<Float> floats,
            List<Boolean> flags,
            List<String> strings,
            List<WeaponModelColor> colors
    ) {
        super(enchantGlint, floats, flags, strings, colors);
    }
}
