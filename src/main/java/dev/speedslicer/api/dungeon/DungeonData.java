package dev.speedslicer.api.dungeon;

import dev.speedslicer.api.lootable.LootTableData;
import net.minestom.server.coordinate.Pos;

import java.util.List;

public record DungeonData (
        int version,
        String id,
        String name,
        String material,
        List<String> mobs,
        LootTableData completeLootTable,
        Pos spawn,
        List<RoomData> rooms){
}
