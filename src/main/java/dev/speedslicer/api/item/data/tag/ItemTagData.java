package dev.speedslicer.api.item.data.tag;

import com.google.gson.JsonElement;

import java.util.List;

public record ItemTagData(
        ItemTagType type,
        JsonElement value,
        boolean list,
        List<String> path
) {
    public ItemTagData {
        path = path == null ? List.of() : List.copyOf(path);
    }
}
