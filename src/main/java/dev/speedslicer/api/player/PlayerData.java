package dev.speedslicer.api.player;

import dev.speedslicer.api.APIVersion;
import dev.speedslicer.api.item.data.ItemData;

import java.util.*;

public class PlayerData {
    private final int version;
    private final UUID uuid;
    private final HashMap<String, Integer> items;
    private final HashMap<String, Integer> armors;
    private final HashMap<String, Integer> charms;
    private final int coins;
    private final long lvl;
    private Map<PlayerSlot, String> selectedItems;
    private boolean completedTutorial;

    public PlayerData(UUID uuid) {
        this(uuid, 0, new HashMap<String, Integer> (), new HashMap<String, Integer> (), new HashMap<String, Integer> (), new HashMap<>(), 0, false);
    }

    public PlayerData(
            UUID uuid,
            int coins,
            HashMap<String, Integer> items,
            HashMap<String, Integer>  armors,
            HashMap<String, Integer>  charms,
            Map<PlayerSlot, String>  selectedItems,
            long lvl,
            boolean completedTutorial
    ) {
        this.version = APIVersion.playerDataVersion;
        this.uuid = uuid;
        this.coins = coins;
        this.items = new HashMap<>(items);
        this.armors = new HashMap<>(armors);
        this.charms = new HashMap<>(charms);
        this.selectedItems = selectedItems;
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

    public String getHandItem() {
        return selectedItems.get(PlayerSlot.HAND);
    }

    public void setSlot(PlayerSlot slot, String selectedItem) {
        if (items.get(selectedItem) == null) {
            throw new IndexOutOfBoundsException();
        }
        selectedItems.put(slot, selectedItem);
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