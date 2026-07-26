package dev.speedslicer.api.item.data;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

public class ItemData {
    private int version;
    private String id;
    private String name;
    private List<String> description;
    private String material;
    private int maxStackSize;
    private ItemDisplayOptions displayOptions;
    private Map<String, JsonElement> customData;

    protected ItemData() {
    }

    public ItemData(
            int version,
            String id,
            String name,
            List<String> description,
            String material,
            int maxStackSize,
            ItemDisplayOptions displayOptions,
            Map<String, JsonElement> customData
    ) {
        this.version = version;
        this.id = id;
        this.name = name;
        this.description = description == null
                ? List.of()
                : List.copyOf(description);
        this.material = material;
        this.maxStackSize = maxStackSize;
        this.displayOptions = displayOptions;
        this.customData = customData == null
                ? Map.of()
                : Map.copyOf(customData);
    }

    public int version() {
        return version;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public List<String> description() {
        return description == null ? List.of() : List.copyOf(description);
    }

    public String material() {
        return material;
    }

    public int maxStackSize() {
        return maxStackSize > 0 ? maxStackSize : 64;
    }

    public ItemDisplayOptions displayOptions() {
        return displayOptions == null
                ? new ItemDisplayOptions(
                        false,
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of()
                )
                : displayOptions;
    }

    public Map<String, JsonElement> customData() {
        return customData == null ? Map.of() : Map.copyOf(customData);
    }
}
