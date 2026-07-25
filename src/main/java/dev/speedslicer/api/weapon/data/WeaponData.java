package dev.speedslicer.api.weapon.data;

import java.util.List;

public record WeaponData(
        int version,
        String id,
        String name,
        List<String> description,
        WeaponType weaponType,
        WeaponClass weaponClass,
        WeaponWorld weaponWorld,
        String material,
        WeaponStats weaponStats,
        WeaponDisplayOptions weaponDisplayOptions
) {
    public WeaponData {
        description = List.copyOf(description);
    }
}