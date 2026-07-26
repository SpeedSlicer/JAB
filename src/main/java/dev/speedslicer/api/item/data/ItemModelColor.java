package dev.speedslicer.api.item.data;

import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Range;

public class ItemModelColor implements RGBLike {
    private int red;
    private int green;
    private int blue;

    protected ItemModelColor() {
    }

    public ItemModelColor(int red, int green, int blue) {
        this.red = validateChannel("red", red);
        this.green = validateChannel("green", green);
        this.blue = validateChannel("blue", blue);
    }

    public static ItemModelColor of(int red, int green, int blue) {
        return new ItemModelColor(red, green, blue);
    }

    @Override
    public @Range(from = 0L, to = 255L) int red() {
        return red;
    }

    @Override
    public @Range(from = 0L, to = 255L) int green() {
        return green;
    }

    @Override
    public @Range(from = 0L, to = 255L) int blue() {
        return blue;
    }

    private static int validateChannel(String name, int value) {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException(
                    name + " must be between 0 and 255"
            );
        }

        return value;
    }
}
