package dev.speedslicer.api.dungeon;

import net.minestom.server.coordinate.Pos;

import java.util.List;

public record DungeonData (String id,
                           String name,
                           String material,
                           List<String> mobs,
                           String completeLootTable,
                           Pos spawn,
                           List<RoomData> rooms){
}
