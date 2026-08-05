package dev.speedslicer.api.player.util;

import dev.speedslicer.api.item.data.attribute.BoostType;
import dev.speedslicer.server.data.playerdata.ActivePlayerData;

import java.util.Random;

public class PlayerDamageHandler {
    public static float getPlayerDamage(ActivePlayerData playerData) {
        Random random = new Random();
        return ((float) playerData.getPlayerBoosts().getStat(BoostType.DAMAGE).doubleValue());
    }
    public static float getPlayerCrit(ActivePlayerData playerData) {
        Random random = new Random();
        boolean applyCrit = playerData.getPlayerBoosts().getStat(BoostType.CRIT) < random.nextInt(100);
        float damage = (float) playerData.getPlayerBoosts().getStat(BoostType.DAMAGE).doubleValue();
        return (applyCrit ? damage * 0.2F : 0);
    }
}
