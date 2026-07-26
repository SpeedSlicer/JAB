package dev.speedslicer.server.bootstrap.registry;

import dev.speedslicer.server.main.Main;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Registry<T> {
    private final Map<String, T> registryStorage = new HashMap<>();

    public void register(String id, T value) {
        if (registryStorage.containsKey(id)) {
            throw new IllegalArgumentException(
                    id + " id already registered "
            );
        }
        Main.getLogger().info("Registered " + " ID: " + id + " DATA: " + value);
        registryStorage.put(id, value);
    }

    public T get(String id) {
        return registryStorage.get(id);
    }

    public T require(String id) {
        var weapon = registryStorage.get(id);

        if (weapon == null) {
            throw new IllegalArgumentException(
                    "Unknown: " + id
            );
        }

        return weapon;
    }

    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(registryStorage.values());
    }

    protected void clear() {
        registryStorage.clear();
    }

    public int size() {
        return registryStorage.size();
    }
}
