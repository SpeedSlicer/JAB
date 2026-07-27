package dev.speedslicer.api.item.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.api.item.data.tag.ItemTagData;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Transcoder;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.MapPostProcessing;
import net.minestom.server.registry.RegistryTranscoder;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

final class ItemStackConfigurationApplier {

    private ItemStackConfigurationApplier() {
    }

    static ItemStack apply(ItemStack item, ItemData data) {
        RegistryTranscoder<JsonElement> jsonCodec = new RegistryTranscoder<>(
                Transcoder.JSON,
                MinecraftServer.getRegistries()
        );

        item = applyComponents(item, data.components(), jsonCodec);
        item = applyLegacyCustomData(item, data.customData());
        item = applyTags(item, data.tags(), jsonCodec);
        item = resetComponents(item, data.resetComponents());
        return removeComponents(item, data.removeComponents());
    }

    private static ItemStack applyComponents(
            ItemStack item,
            Map<String, JsonElement> components,
            RegistryTranscoder<JsonElement> jsonCodec
    ) {
        for (Map.Entry<String, JsonElement> entry : components.entrySet()) {
            DataComponent<?> component = findComponent(entry.getKey());
            JsonElement value = entry.getValue();

            if (value == null || value.isJsonNull()) {
                item = item.without(component);
                continue;
            }

            item = applyComponent(item, component, value, jsonCodec);
        }
        return item;
    }

    private static ItemStack applyComponent(
            ItemStack item,
            DataComponent<?> component,
            JsonElement value,
            RegistryTranscoder<JsonElement> jsonCodec
    ) {
        if (isUnitComponent(component)) {
            if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isBoolean()) {
                throw new IllegalArgumentException(
                        "Unit component '" + component.key() + "' must be true or false"
                );
            }
            return value.getAsBoolean() ? applyUnit(item, component) : item.without(component);
        }

        if (component == DataComponents.ADDITIONAL_TRADE_COST) {
            return item.with(DataComponents.ADDITIONAL_TRADE_COST, value.getAsInt());
        }
        if (component == DataComponents.MAP_POST_PROCESSING) {
            MapPostProcessing processing = MapPostProcessing.valueOf(
                    value.getAsString().toUpperCase(Locale.US)
            );
            return item.with(DataComponents.MAP_POST_PROCESSING, processing);
        }
        if (component.codec() == null) {
            throw new IllegalArgumentException(
                    "Component '" + component.key() + "' has no JSON codec"
            );
        }

        return decodeComponent(item, component, value, jsonCodec);
    }

    private static boolean isUnitComponent(DataComponent<?> component) {
        return component == DataComponents.UNBREAKABLE
                || component == DataComponents.CREATIVE_SLOT_LOCK
                || component == DataComponents.INTANGIBLE_PROJECTILE
                || component == DataComponents.GLIDER;
    }

    @SuppressWarnings("unchecked")
    private static ItemStack applyUnit(ItemStack item, DataComponent<?> component) {
        return item.with((DataComponent<Unit>) component, Unit.INSTANCE);
    }

    private static <T> ItemStack decodeComponent(
            ItemStack item,
            DataComponent<T> component,
            JsonElement value,
            RegistryTranscoder<JsonElement> jsonCodec
    ) {
        T decoded = component.decode(jsonCodec, value).orElseThrow(
                "Invalid value for item component '" + component.key() + "'"
        );
        return item.with(component, decoded);
    }

    private static ItemStack applyLegacyCustomData(
            ItemStack item,
            Map<String, JsonElement> customData
    ) {
        for (Map.Entry<String, JsonElement> entry : customData.entrySet()) {
            BinaryTag value = decodeNbt(entry.getValue(), "customData." + entry.getKey());
            item = item.withTag(Tag.NBT(entry.getKey()), value);
        }
        return item;
    }

    private static ItemStack applyTags(
            ItemStack item,
            Map<String, ItemTagData> tags,
            RegistryTranscoder<JsonElement> jsonCodec
    ) {
        for (Map.Entry<String, ItemTagData> entry : tags.entrySet()) {
            String key = entry.getKey();
            ItemTagData data = entry.getValue();

            if (data == null || data.type() == null) {
                throw new IllegalArgumentException("Tag '" + key + "' is missing its type");
            }

            item = switch (data.type()) {
                case BYTE -> applyTag(item, key, data, Tag.Byte(key), JsonElement::getAsByte);
                case BOOLEAN -> applyTag(item, key, data, Tag.Boolean(key), JsonElement::getAsBoolean);
                case SHORT -> applyTag(item, key, data, Tag.Short(key), JsonElement::getAsShort);
                case INTEGER -> applyTag(item, key, data, Tag.Integer(key), JsonElement::getAsInt);
                case LONG -> applyTag(item, key, data, Tag.Long(key), JsonElement::getAsLong);
                case FLOAT -> applyTag(item, key, data, Tag.Float(key), JsonElement::getAsFloat);
                case DOUBLE -> applyTag(item, key, data, Tag.Double(key), JsonElement::getAsDouble);
                case STRING -> applyTag(item, key, data, Tag.String(key), JsonElement::getAsString);
                case UUID -> applyTag(
                        item,
                        key,
                        data,
                        Tag.UUID(key),
                        value -> UUID.fromString(value.getAsString())
                );
                case COMPONENT -> applyTag(
                        item,
                        key,
                        data,
                        Tag.Component(key),
                        value -> Codec.COMPONENT.decode(Transcoder.JSON, value)
                                .orElseThrow("Invalid component tag '" + key + "'")
                );
                case ITEM_STACK -> applyTag(
                        item,
                        key,
                        data,
                        Tag.ItemStack(key),
                        value -> ItemStack.CODEC.decode(jsonCodec, value)
                                .orElseThrow("Invalid item stack tag '" + key + "'")
                );
                case NBT -> applyTag(
                        item,
                        key,
                        data,
                        Tag.NBT(key),
                        value -> decodeNbt(value, "tags." + key)
                );
            };
        }
        return item;
    }

    private static <T> ItemStack applyTag(
            ItemStack item,
            String key,
            ItemTagData data,
            Tag<T> baseTag,
            TagValueDecoder<T> decoder
    ) {
        Tag<T> tag = data.path().isEmpty()
                ? baseTag
                : baseTag.path(data.path().toArray(String[]::new));
        JsonElement value = data.value();

        if (data.list()) {
            Tag<List<T>> listTag = tag.list();
            if (value == null || value.isJsonNull()) {
                return item.withTag(listTag, null);
            }
            if (!value.isJsonArray()) {
                throw new IllegalArgumentException("List tag '" + key + "' must contain a JSON array");
            }

            JsonArray jsonValues = value.getAsJsonArray();
            List<T> values = new ArrayList<>(jsonValues.size());
            for (JsonElement jsonValue : jsonValues) {
                values.add(decoder.decode(jsonValue));
            }
            return item.withTag(listTag, List.copyOf(values));
        }

        return item.withTag(
                tag,
                value == null || value.isJsonNull() ? null : decoder.decode(value)
        );
    }

    private static BinaryTag decodeNbt(JsonElement value, String location) {
        if (value == null || value.isJsonNull()) {
            return null;
        }
        return Transcoder.JSON.convertTo(Transcoder.NBT, value)
                .orElseThrow("Invalid NBT value at '" + location + "'");
    }

    private static ItemStack resetComponents(ItemStack item, List<String> componentKeys) {
        for (String key : componentKeys) {
            item = item.reset(findComponent(key));
        }
        return item;
    }

    private static ItemStack removeComponents(ItemStack item, List<String> componentKeys) {
        for (String key : componentKeys) {
            item = item.without(findComponent(key));
        }
        return item;
    }

    private static DataComponent<?> findComponent(String configuredKey) {
        if (configuredKey == null || configuredKey.isBlank()) {
            throw new IllegalArgumentException("Item component key cannot be blank");
        }

        String key = configuredKey.contains(":")
                ? configuredKey
                : "minecraft:" + configuredKey;
        DataComponent<?> component = DataComponent.fromKey(key);
        if (component == null) {
            throw new IllegalArgumentException("Unknown item component '" + configuredKey + "'");
        }
        return component;
    }

    @FunctionalInterface
    private interface TagValueDecoder<T> {
        T decode(JsonElement value);
    }
}
