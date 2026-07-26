package dev.speedslicer.api.player;

import dev.speedslicer.api.APIVersion;
import dev.speedslicer.api.item.data.ItemData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerData {
    private final int version;
    private final UUID uuid;
    private final HashMap<String, Integer> items;
    private final HashMap<String, Integer> armors;
    private final HashMap<String, Integer> charms;
    private final int coins;
    private final long lvl;
    private String selectedItem;
    private boolean completedTutorial;

    public PlayerData(UUID uuid) {
        this(uuid, 0, new HashMap<String, Integer> (), new HashMap<String, Integer> (), new HashMap<String, Integer> (), "basic_sword", 0, false);
    }

    public PlayerData(
            UUID uuid,
            int coins,
            HashMap<String, Integer> items,
            HashMap<String, Integer>  armors,
            HashMap<String, Integer>  charms,
            String selectedItem,
            long lvl,
            boolean completedTutorial
    ) {
        this.version = APIVersion.playerDataVersion;
        this.uuid = uuid;
        this.coins = coins;
        this.items = new HashMap<>(items);
        this.armors = new HashMap<>(armors);
        this.charms = new HashMap<>(charms);
        this.selectedItem = selectedItem;
        this.lvl = lvl;
        this.completedTutorial = completedTutorial;
    }

    public void addItem(String itemData, int amount) {
        if (items.get(itemData) == null) {
            items.put(itemData, 1);
        }
        else {
            items.put(itemData, items.get(itemData) + amount);

        }
    }
    public void addItem(String itemData) {
        addItem(itemData, 1);
    }

    public void addItem(ItemData itemData, int amount) {
        addItem(itemData.id(), amount);
    }

    public void addItem(ItemData itemData) {
        addItem(itemData, 1);
    }

    public String getSelectedID() {
        if (items.get(selectedItem) == null) {
            return null;
        }

        return selectedItem;
    }

    public void setItem(String selectedItem) {
        if (items.get(selectedItem) == null) {
            throw new IndexOutOfBoundsException();
        }

        this.selectedItem = selectedItem;
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

    public String getSelectedItem() {
        return selectedItem;
    }

    public List<String> getItems() {
        return List.copyOf(items.keySet());
    }

    public boolean hasCompletedTutorial() {
        return completedTutorial;
    }

    public void completeTutorial() {
        completedTutorial = true;
    }

    public int getVersion() {
        return version;
    }
}