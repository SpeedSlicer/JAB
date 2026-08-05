package dev.speedslicer.server.bootstrap.registry.impl;

import dev.speedslicer.api.APIVersion;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.server.ServerSettings;
import dev.speedslicer.server.bootstrap.registry.Registry;
import dev.speedslicer.server.main.Main;

import java.util.HashMap;

public class ItemRegistry{
    private static HashMap<String, Registry<ItemData>> item_registries = new HashMap<>();

    public void createRegistry(String registryName) {
        item_registries.put(registryName, new Registry<>());
    }

    public void addItem(ItemData item) {
        if (item_registries.get(item.category()) == null) {
            Main.getLogger().error("Registry {} doesn't exist", item.category());
            return;
        }
        if (item.version() != APIVersion.itemDataVersion) {
            Main.getLogger().info("{} item does not match current version {}, needs upgrading from {}", item.id(), APIVersion.itemDataVersion, item.version());
            if (ServerSettings.safeMode) {
                return;
            }
        }
        item_registries.get(item.category()).register(item.id(), item);
    }
    /*
     NOTE
     "<registry>:<item>" works instead of having to define it every time
     */

    public ItemData getItem(String registry, String id) {
        String[] parts = id.split(":", 2);

        if (parts.length != 1) {
            Main.getLogger().warn("Wanted to have the : while using getItem(reg, id) method?");
        }
        return item_registries.get(registry).get(parts[0]);
    }

    public ItemData getItem(String regId) {
        String[] parts = regId.split(":", 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Item ID must be in format <registry>:<item>"
            );
        }

        return getItem(parts[0], parts[1]);
    }
}
