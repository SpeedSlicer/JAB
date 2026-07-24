package dev.speedslicer.api.player;

import dev.speedslicer.api.weapon.WeaponData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;

    private int coins;
    private final List<String> weapons;
    private final List<String> armors;
    private final List<String> charms;
    private int selectedSlot;
    private long lvl;
    private boolean completedTutorial;
    public PlayerData(UUID uuid) {
        this(uuid, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, false);
    }

    public PlayerData(
            UUID uuid,
            int coins,
            List<String> weapons,
            List<String> armors,
            List<String> charms,
            int selectedSlot,
            long lvl,
            boolean completedTutorial
    ) {
        this.uuid = uuid;
        this.coins = coins;
        this.weapons = new ArrayList<>(weapons);
        this.armors = new ArrayList<>(armors);
        this.charms = new ArrayList<>(charms);

        this.lvl = lvl;
        this.completedTutorial = completedTutorial;
        if (weapons.isEmpty()) {
            this.selectedSlot = 0;
        } else if (selectedSlot < 0 || selectedSlot >= weapons.size()) {
            this.selectedSlot = 0;
        } else {
            this.selectedSlot = selectedSlot;
        }
    }

    public void addWeapon(String weaponData) {
        weapons.add(weaponData);
    }

    public void addWeapon(WeaponData weaponData) {
        weapons.add(weaponData.id());
    }

    public String getSelectedID() {
        if (weapons.isEmpty()) {
            return null;
        }

        return weapons.get(selectedSlot);
    }

    public void setWeapon(int selectedSlot) {
        if (selectedSlot < 0 || selectedSlot >= weapons.size()) {
            throw new IndexOutOfBoundsException(
                    "Invalid weapon slot: " + selectedSlot
            );
        }

        this.selectedSlot = selectedSlot;
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

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public List<String> getWeapons() {
        return List.copyOf(weapons);
    }

    public boolean hasCompletedTutorial() {
        return completedTutorial;
    }

    public void completeTutorial() {
        completedTutorial = true;
    }
}