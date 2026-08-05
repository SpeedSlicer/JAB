package dev.speedslicer.api.dungeon;

import dev.speedslicer.api.lootable.LootTableData;
import net.minestom.server.coordinate.Pos;

import java.util.List;
import java.util.Map;

public record RoomData (Map<String, Pos> mobs,
                        Pos exitDoorA,
                        Pos exitDoorB,
                        List<LootTableData> completionLootTable){
}
