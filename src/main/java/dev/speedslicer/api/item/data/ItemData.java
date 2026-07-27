package dev.speedslicer.api.item.data;

import com.google.gson.JsonElement;
import dev.speedslicer.api.item.data.attribute.ItemBoost;
import dev.speedslicer.api.item.data.tag.ItemTagData;

import java.util.List;
import java.util.Map;

public class ItemData {
    private final int version;
    private final String category;
    private final String id;
    private final String name;
    private final List<String> description;
    private final String material;
    private final int amount;
    private final int maxStackSize;
    private final ItemDisplayOptions displayOptions;
    private final Map<String, JsonElement> customData;
    private final List<ItemBoost> itemBoosts;
    private final Map<String, JsonElement> components;
    private final List<String> resetComponents;
    private final List<String> removeComponents;
    private final Map<String, ItemTagData> tags;

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
        this(
                version,
                id,
                category,
                name,
                description,
                material,
                1,
                maxStackSize,
                displayOptions,
                customData,
                itemBoosts,
                Map.of(),
                List.of(),
                List.of(),
                Map.of()
        );
    }

    public ItemData(
            int version,
            String id,
            String category,
            String name,
            List<String> description,
            String material,
            int amount,
            int maxStackSize,
            ItemDisplayOptions displayOptions,
            Map<String, JsonElement> customData,
            List<ItemBoost> itemBoosts,
            Map<String, JsonElement> components,
            List<String> resetComponents,
            List<String> removeComponents,
            Map<String, ItemTagData> tags
    ) {
        this.version = version;
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description == null
                ? List.of()
                : List.copyOf(description);
        this.material = material;
        this.amount = amount;
        this.maxStackSize = maxStackSize;
        this.displayOptions = displayOptions;
        this.customData = customData == null
                ? Map.of()
                : Map.copyOf(customData);
        this.itemBoosts = itemBoosts == null
                ? List.of()
                : List.copyOf(itemBoosts);
        this.components = components == null
                ? Map.of()
                : Map.copyOf(components);
        this.resetComponents = resetComponents == null
                ? List.of()
                : List.copyOf(resetComponents);
        this.removeComponents = removeComponents == null
                ? List.of()
                : List.copyOf(removeComponents);
        this.tags = tags == null
                ? Map.of()
                : Map.copyOf(tags);
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
    public int amount() {
        return amount > 0 ? amount : 1;
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

    public Map<String, JsonElement> components() {
        return components == null ? Map.of() : Map.copyOf(components);
    }

    public List<String> resetComponents() {
        return resetComponents == null ? List.of() : List.copyOf(resetComponents);
    }

    public List<String> removeComponents() {
        return removeComponents == null ? List.of() : List.copyOf(removeComponents);
    }

    public Map<String, ItemTagData> tags() {
        return tags == null ? Map.of() : Map.copyOf(tags);
    }
}
