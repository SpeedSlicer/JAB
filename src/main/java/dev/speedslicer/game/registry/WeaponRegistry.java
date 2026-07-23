package dev.speedslicer.game.registry;

import dev.speedslicer.api.weapon.WeaponData;

import java.util.HashMap;
import java.util.Map;

public final class WeaponRegistry {

    private final Map<String, WeaponData> weapons = new HashMap<>();

    public void register(WeaponData weapon) {
        if (weapons.putIfAbsent(weapon.id(), weapon) != null) {
            throw new IllegalArgumentException(
                    "Duplicate weapon ID: " + weapon.id()
            );
        }
    }

    public WeaponData get(String id) {
        WeaponData weapon = weapons.get(id);

        if (weapon == null) {
            throw new IllegalArgumentException(
                    "Unknown weapon ID: " + id
            );
        }

        return weapon;
    }
}