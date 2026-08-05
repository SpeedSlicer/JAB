package dev.speedslicer.server.instances.dungeon;

import java.util.ArrayList;
import java.util.List;

public class DungeonInstanceManager {
    List<DungeonInstance> dungeonInstances;
    public DungeonInstanceManager() {
        dungeonInstances = new ArrayList<>();
    }

    public void createInstance(DungeonInstance instance) {
        dungeonInstances.add(instance);
    }
}
