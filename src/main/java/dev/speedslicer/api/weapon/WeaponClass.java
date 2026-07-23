package dev.speedslicer.api.weapon;

public enum WeaponClass {
    WEAK("wc_weak"),
    FORGED("wc_forged"),
    TEMPERED("wc_annealed"),
    IMPERFECT("wc_quenched"),
    PERFECT("wc_tempered");
    final String id;
    WeaponClass(String id) {
        this.id = id;
    }
    public String getID() {
        return id;
    }
}
