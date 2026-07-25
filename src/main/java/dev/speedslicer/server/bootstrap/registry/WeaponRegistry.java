package dev.speedslicer.server.bootstrap.registry;

import dev.speedslicer.api.weapon.data.WeaponData;
import dev.speedslicer.server.main.Main;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class WeaponRegistry {

    private final Map<String, WeaponData> weapons = new HashMap<>();

    public void register(String id, WeaponData weapon) {
        if (weapons.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Weapon already registered: " + id
            );
        }
        Main.getLogger().info("Weapon Registered " + " ID: " + id + " DATA: " + weapon);
        weapons.put(id, weapon);
    }

    public WeaponData get(String id) {
        return weapons.get(id);
    }

    public WeaponData require(String id) {
        WeaponData weapon = weapons.get(id);

        if (weapon == null) {
            throw new IllegalArgumentException(
                    "Unknown weapon: " + id
            );
        }

        return weapon;
    }

    public Collection<WeaponData> getAll() {
        return Collections.unmodifiableCollection(weapons.values());
    }

    public void clear() {
        weapons.clear();
    }

    public int size() {
        return weapons.size();
    }
}