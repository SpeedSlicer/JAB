package dev.speedslicer.api.item.data;

import com.google.gson.JsonElement;
import dev.speedslicer.api.item.data.attribute.ItemBoost;

import java.util.List;
import java.util.Map;

public class ItemData {
    private final int version;
    private final String category;
    private final String id;
    private final String name;
    private final List<String> description;
    private final String material;
    private final int maxStackSize;
    private final ItemDisplayOptions displayOptions;
    private final Map<String, JsonElement> customData;
    private final List<ItemBoost> itemBoosts;

    public ItemData(
            int version,
            String id,
            String category,
            String name,
            List<String> description,
            String material,
            int maxStackSize,
            ItemDisplayOptions displayOptions,
            Map<String, JsonElement> customData,
            List<ItemBoost> itemBoosts
    ) {
        this.version = version;
        this.id = id;
        this.category = category;
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
        this.itemBoosts = itemBoosts == null
                ? List.of()
                : List.copyOf(itemBoosts);
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
    public String category() {
        return category;
    }
    public int maxStackSize() {
        return maxStackSize > 0 ? maxStackSize : 64;
    }
    public List<ItemBoost> getItemBoosts() {
        return itemBoosts == null ? List.of() : List.copyOf(itemBoosts);
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
