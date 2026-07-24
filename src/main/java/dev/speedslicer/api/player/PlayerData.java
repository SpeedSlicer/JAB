package dev.speedslicer.api.player;

import dev.speedslicer.api.weapon.WeaponData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;

    private int coins;
    private final List<String> weaponInventory;
    private int selectedSlot;
    private long lvl;

    public PlayerData(UUID uuid) {
        this(uuid, 0, new ArrayList<>(), 0, 0);
    }

    public PlayerData(
            UUID uuid,
            int coins,
            List<String> weaponInventory,
            int selectedSlot,
            long lvl
    ) {
        this.uuid = uuid;
        this.coins = coins;
        this.weaponInventory = new ArrayList<>(weaponInventory);
        this.lvl = lvl;

        if (weaponInventory.isEmpty()) {
            this.selectedSlot = 0;
        } else if (selectedSlot < 0 || selectedSlot >= weaponInventory.size()) {
            this.selectedSlot = 0;
        } else {
            this.selectedSlot = selectedSlot;
        }
    }

    public void addWeapon(String weaponData) {
        weaponInventory.add(weaponData);
    }

    public void addWeapon(WeaponData weaponData) {
        weaponInventory.add(weaponData.id());
    }

    public String getSelectedID() {
        if (weaponInventory.isEmpty()) {
            return null;
        }

        return weaponInventory.get(selectedSlot);
    }

    public void setWeapon(int selectedSlot) {
        if (selectedSlot < 0 || selectedSlot >= weaponInventory.size()) {
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

    public List<String> getWeaponInventory() {
        return List.copyOf(weaponInventory);
    }
}