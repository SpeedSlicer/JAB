package dev.speedslicer.api.lootable;

import dev.speedslicer.api.Constants;
import dev.speedslicer.server.data.playerdata.PlayerDataManager;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;
public class LootTableDataHandler {

    public static Random random = new Random();

    public static void addRewards(LootTableData table, UUID playerUUID, PlayerDataManager dataManager) {
        for (String v : table.rewards().keySet()) {
            if (table.rewards().get(v).chance() >= random.nextDouble(0, 1)) {
                if (Objects.equals(v, Constants.currencyName)) {
                    dataManager.getPlayerData(playerUUID)
                            .addCoins((table.rewards().get(v).amount()));
                }
                else {
                    dataManager.getPlayerData(playerUUID)
                            .addItem(v, table.rewards().get(v).amount());
                }
            }
        }
    }
}
