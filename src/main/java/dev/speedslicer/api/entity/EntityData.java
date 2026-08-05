package dev.speedslicer.api.entity;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import dev.speedslicer.api.entity.ai.EntityAIData;
import dev.speedslicer.api.entity.items.EntityEquipmentData;
import dev.speedslicer.api.entity.stats.EntityStats;

import java.util.List;
import java.util.Map;

public record EntityData(
        int version,
        String id,
        String nametag,
        String baseEntity,
        @SerializedName(value = "associatedAI", alternate = "ai")
        List<EntityAIData> associatedAI,
        @SerializedName(value = "metadata", alternate = "properties")
        Map<String, JsonElement> metadata,
        EntityEquipmentData equipment,
        @SerializedName(value="entityStatistics", alternate="stats") EntityStats entityStats
) {
    public EntityData {
        associatedAI = associatedAI == null
                ? List.of()
                : List.copyOf(associatedAI);
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }
}
