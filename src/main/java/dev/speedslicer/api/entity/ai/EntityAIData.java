package dev.speedslicer.api.entity.ai;

import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;

import java.util.Map;

@JsonAdapter(EntityAIDataAdapter.class)
public record EntityAIData(
        String id,
        Map<String, JsonElement> data
) {
    public EntityAIData {
        data = data == null ? Map.of() : Map.copyOf(data);
    }

    public EntityAIData(String id) {
        this(id, Map.of());
    }
}
