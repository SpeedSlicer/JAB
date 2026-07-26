package dev.speedslicer.api.weapon.data;

import dev.speedslicer.api.item.data.ItemData;

import java.util.List;
import java.util.Map;

public final class WeaponData extends ItemData {
    private WeaponType weaponType;
    private WeaponClass weaponClass;
    private WeaponWorld weaponWorld;
    private WeaponStats weaponStats;
    private WeaponDisplayOptions weaponDisplayOptions;

    private WeaponData() {
    }

    public WeaponData(
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
        super(
                version,
                id,
                name,
                description,
                material,
                1,
                null,
                Map.of()
        );
        this.weaponType = weaponType;
        this.weaponClass = weaponClass;
        this.weaponWorld = weaponWorld;
        this.weaponStats = weaponStats;
        this.weaponDisplayOptions = weaponDisplayOptions;
    }

    public WeaponType weaponType() {
        return weaponType;
    }

    public WeaponClass weaponClass() {
        return weaponClass;
    }

    public WeaponWorld weaponWorld() {
        return weaponWorld;
    }

    public WeaponStats weaponStats() {
        return weaponStats;
    }

    public WeaponDisplayOptions weaponDisplayOptions() {
        return displayOptions();
    }

    @Override
    public int maxStackSize() {
        return 1;
    }

    @Override
    public WeaponDisplayOptions displayOptions() {
        return weaponDisplayOptions == null
                ? new WeaponDisplayOptions(
                        false,
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of()
                )
                : weaponDisplayOptions;
    }
}
