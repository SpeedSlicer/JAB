package dev.speedslicer.api.weapon.data;

import dev.speedslicer.api.item.data.ItemModelColor;

/**
 * @deprecated Use {@link ItemModelColor}; model colors are not weapon-specific.
 */
@Deprecated
public class WeaponModelColor extends ItemModelColor {
    public WeaponModelColor(int red, int blue, int green) {
        super(red, green, blue);
    }

    public static WeaponModelColor of(int red, int blue, int green) {
        return new WeaponModelColor(red, green, blue);
    }
}
