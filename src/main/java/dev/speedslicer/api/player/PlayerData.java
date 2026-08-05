package dev.speedslicer.api.player;

import dev.speedslicer.api.APIVersion;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.api.permissions.PermGroup;

import java.util.*;
import java.util.function.Function;

public class PlayerData {
    private final int version;
    private final UUID uuid;
    private final HashMap<String, Integer> items;
    private final HashMap<String, Integer> armors;
    private final HashMap<String, Integer> charms;
    private int coins;
    private long lvl;
    private Map<PlayerSlot, String> selectedItems;
    private boolean completedTutorial;
    List<String> permGroups;

    public PlayerData(UUID uuid) {
        this(uuid, 0, new HashMap<String, Integer> (), new HashMap<String, Integer> (), new HashMap<String, Integer> (), new HashMap<>(), 0, false, List.of("User"));
    }

    public PlayerData(
            UUID uuid,
            int coins,
            HashMap<String, Integer> items,
            HashMap<String, Integer>  armors,
            HashMap<String, Integer>  charms,
            Map<PlayerSlot, String>  selectedItems,
            long lvl,
            boolean completedTutorial,
            List<String> permissionGroups
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
        this.permGroups = permissionGroups;
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

    public void addCoins(int amount) {
        coins += amount;
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

    public Map<PlayerSlot, String> getSelectedItems() {
        return selectedItems;
    }

    public void addGroup(String permGroup) {
        permGroups.add(permGroup);
    }
    public void removeGroup(String permGroup) {
        permGroups.remove(permGroup);
    }
    public boolean hasGroup(String permGroup) {
        return permGroups.contains(permGroup);
    }
}