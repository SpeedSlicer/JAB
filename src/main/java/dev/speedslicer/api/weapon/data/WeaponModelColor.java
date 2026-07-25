package dev.speedslicer.api.weapon.data;

import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Range;

public class WeaponModelColor implements RGBLike {
    int red;
    int blue;
    int green;
    public WeaponModelColor(int red, int blue, int green) {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }
    public static WeaponModelColor of(int red, int blue, int green) {
        return new WeaponModelColor(red,  blue,  green);
    }
    @Override
    public @Range(from = 0L, to = 255L) int red() {
        return red;
    }

    @Override
    public @Range(from = 0L, to = 255L) int green() {
        return blue;
    }

    @Override
    public @Range(from = 0L, to = 255L) int blue() {
        return green;
    }
}
