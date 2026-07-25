package dev.speedslicer.api.weapon.data;

public enum WeaponClass {
    WEAK("wc_weak"),
    FORGED("wc_forged"),
    TEMPERED("wc_tempered"),
    IMPERFECT("wc_imperfect"),
    PERFECT("wc_perfect");
    final String id;

    WeaponClass(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }
}
