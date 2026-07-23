package dev.speedslicer.main;

import dev.speedslicer.game.registry.WeaponRegistry;

public class ServerController {
    WeaponRegistry weaponRegistry;
    public ServerController() {
        weaponRegistry = new WeaponRegistry();

    }
    public WeaponRegistry getWeaponRegistry() {
        return weaponRegistry;
    }
}
