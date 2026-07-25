package dev.speedslicer.api.weapon.data;

import net.kyori.adventure.util.RGBLike;

import java.util.List;

public record WeaponDisplayOptions (boolean isEnchantGlint, List<Float> floats, List<Boolean> flags, List<String> strings, List<WeaponModelColor> colors){
}
