package dev.speedslicer.api.player;

import dev.speedslicer.api.APIVersion;
import dev.speedslicer.api.weapon.data.WeaponData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerData {
    private final int version;
    private final UUID uuid;
    private final HashMap<String, Integer> weapons;
    private final HashMap<String, Integer> armors;
    private final HashMap<String, Integer> charms;
    private final int coins;
    private final long lvl;
    private String selectedWeapon;
    private boolean completedTutorial;

    public PlayerData(UUID uuid) {
        this(uuid, 0, new HashMap<String, Integer> (), new HashMap<String, Integer> (), new HashMap<String, Integer> (), "basic_sword", 0, false);
    }

    public PlayerData(
            UUID uuid,
            int coins,
            HashMap<String, Integer> weapons,
            HashMap<String, Integer>  armors,
            HashMap<String, Integer>  charms,
            String selectedWeapon,
            long lvl,
            boolean completedTutorial
    ) {
        this.version = APIVersion.playerDataVersion;
        this.uuid = uuid;
        this.coins = coins;
        this.weapons = new HashMap<>(weapons);
        this.armors = new HashMap<>(armors);
        this.charms = new HashMap<>(charms);
        this.selectedWeapon = selectedWeapon;
        this.lvl = lvl;
        this.completedTutorial = completedTutorial;
    }

    public void addWeapon(String weaponData, int amount) {
        if (weapons.get(weaponData) == null) {
            weapons.put(weaponData, 1);
        }
        else {
            weapons.put(weaponData, weapons.get(weaponData) + amount);

        }
    }
    public void addWeapon(String weaponData) {
        addWeapon(weaponData, 1);
    }

    public void addWeapon(WeaponData weaponData, int amount) {
        addWeapon(weaponData.id(), amount);
    }

    public void addWeapon(WeaponData weaponData) {
        addWeapon(weaponData, 1);
    }

    public String getSelectedID() {
        if (weapons.get(selectedWeapon) == null) {
            return null;
        }

        return selectedWeapon;
    }

    public void setWeapon(String selectedWeapon) {
        if (weapons.get(selectedWeapon) == null) {
            throw new IndexOutOfBoundsException();
        }

        this.selectedWeapon = selectedWeapon;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getCoins() {
        return coins;
    }

    public long getLvl() {
        return lvl;
    }

    public String getSelectedWeapon() {
        return selectedWeapon;
    }

    public List<String> getWeapons() {
        return List.copyOf(weapons.keySet());
    }

    public boolean hasCompletedTutorial() {
        return completedTutorial;
    }

    public void completeTutorial() {
        completedTutorial = true;
    }

    public int getVersion() {
        return version;
    }
}