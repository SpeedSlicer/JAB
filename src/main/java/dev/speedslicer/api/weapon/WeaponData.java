package dev.speedslicer.api.weapon;

import java.util.List;

public record WeaponData(
        String id,
        String name,
        List<String> description,
        WeaponType weaponType,
        WeaponClass weaponClass,
        WeaponWorld weaponWorld,
        String material
) {
    public WeaponData {
        description = List.copyOf(description);
    }
}