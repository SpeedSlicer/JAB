package dev.speedslicer.api.entity;

import com.google.gson.JsonElement;
import net.minestom.server.coordinate.Pos;

import java.util.List;
import java.util.Map;

public record EntityData(
        int version,
        String id,
        String nametag,
        String baseEntity,
        List<String> associatedAI,
        Map<String, JsonElement> properties
) {
}