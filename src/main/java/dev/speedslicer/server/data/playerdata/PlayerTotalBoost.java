package dev.speedslicer.server.data.playerdata;

import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.api.item.data.attribute.BoostType;
import dev.speedslicer.api.item.data.attribute.ItemBoost;
import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.server.bootstrap.registry.impl.ItemRegistry;
import dev.speedslicer.server.main.Main;

import java.util.HashMap;
import java.util.UUID;

public class PlayerTotalBoost {
    UUID playerUUID;
    HashMap<BoostType, Double> playerStats;
    public PlayerTotalBoost(UUID player) {
        playerUUID = player;
        playerStats = new HashMap<>();
    }
    public Double getStat(BoostType boostType) {
        return playerStats.get(boostType);
    }
    public void updatePlayerStats(PlayerData playerData) {
        ItemRegistry itemRegistry = Main.getServerController().getItemRegistry();
        for (var slot : playerData.getSelectedItems().keySet()) {
            ItemData item = itemRegistry.getItem(playerData.getSelectedItems().get(slot));
            for (ItemBoost itemBoost : item.getItemBoosts()) {
                assert playerStats != null;
                playerStats.putIfAbsent(itemBoost.boostType(), 0D);
                playerStats.put(itemBoost.boostType(), playerStats.get(itemBoost.boostType()) + itemBoost.amount());
            }
        }
    }
}
