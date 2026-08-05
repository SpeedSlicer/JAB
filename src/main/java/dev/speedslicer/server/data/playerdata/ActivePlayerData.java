package dev.speedslicer.server.data.playerdata;

import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.api.player.PlayerSlot;

public class ActivePlayerData {
    PlayerData playerData;
    PlayerTotalBoost boost;

    public ActivePlayerData(PlayerData playerData) {
        this.playerData = playerData;
        this.boost = new PlayerTotalBoost(playerData.getUuid());
    }
    public PlayerData getPlayerData() {
        return playerData;
    }
    public PlayerTotalBoost getPlayerBoosts() {
        return boost;
    }

    public void addCoins(Integer amount) {
        playerData.addCoins(amount);
    }

    public void addItem(String item, Integer amount) {
        playerData.addItem(item, amount);
    }

    public void setSlot(PlayerSlot playerSlot, String s) {
        playerData.setSlot(playerSlot, s);
        boost.updatePlayerStats(playerData);
    }

    public void addItem(String s) {
        addItem(s, 1);
    }

    public String getHandItem() {
        return playerData.getHandItem();
    }
    public PlayerTotalBoost getPlayerBoost() {
        return boost;
    }
}
