package dev.speedslicer.server.instances.dungeon.events;

import dev.speedslicer.api.dungeon.RoomData;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.instance.Instance;

public class RoomClearEvent implements InstanceEvent {
    private final Instance instance;
    private final RoomData room;
    private final RoomData nextRoom;

    public RoomClearEvent(Instance instance, RoomData room, RoomData nextRoom) {
        this.instance = instance;
        this.room = room;
        this.nextRoom = nextRoom;
    }

    @Override
    public Instance getInstance() {
        return instance;
    }

    public RoomData getRoom() {
        return room;
    }

    public RoomData getNextRoom() {
        return nextRoom;
    }
}
