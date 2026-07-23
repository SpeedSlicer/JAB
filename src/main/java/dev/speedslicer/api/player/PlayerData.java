package dev.speedslicer.api.player;

import dev.speedslicer.api.weapon.WeaponData;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    String uuid;

    List<WeaponData> weaponInventory = new ArrayList<>();
    int selected = 0;
    public void AddWeapon(WeaponData addData) {
        weaponInventory.add(addData);
    }
    public WeaponData GetSelectedData() {
        return weaponInventory.get(selected);
    }
    public void SetWeapon(int selected) {
        this.selected = selected;
    }
}
