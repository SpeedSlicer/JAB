package dev.speedslicer.api.entity.meta;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.metadata.EntityMeta;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.registry.Holder;
import net.minestom.server.registry.RegistryKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public final class EntityPropertyApplier {
    private static final Gson GSON = new Gson();
    private static final Map<String, String> PROPERTY_ALIASES = Map.of(
            "glowing", "hasGlowingEffect",
            "noGravity", "hasNoGravity"
    );

    public void apply(EntityCreature entity, Map<String, JsonElement> properties) {
        if (properties == null || properties.isEmpty()) {
            return;
        }

        EntityMeta metadata = entity.getEntityMeta();
        metadata.setNotifyAboutChanges(false);

        try {
            for (Map.Entry<String, JsonElement> property
                    : properties.entrySet()) {
                applyProperty(
                        metadata,
                        property.getKey(),
                        property.getValue()
                );
            }
        } finally {
            metadata.setNotifyAboutChanges(true);
        }
    }

    private void applyProperty(
            EntityMeta metadata,
            String propertyName,
            JsonElement value
    ) {
        String aliasedName = PROPERTY_ALIASES.getOrDefault(
                propertyName,
                propertyName
        );
        String normalizedName = normalize(aliasedName);
        ArrayList<Method> candidates = new ArrayList<>();

        for (Method method : metadata.getClass().getMethods()) {
            if (!Modifier.isPublic(method.getModifiers())
                    || !method.getName().startsWith("set")
                    || method.getParameterCount() == 0
                    || method.getName().equals("setNotifyAboutChanges")) {
                continue;
            }

            String setterProperty = method.getName().substring(3);

            if (normalize(setterProperty).equals(normalizedName)) {
                candidates.add(method);
            }
        }

        candidates.sort(
                Comparator.comparing(this::methodSignature)
        );

        if (candidates.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unknown metadata property '" + propertyName
                            + "' for "
                            + metadata.getClass().getSimpleName()
            );
        }

        ArrayList<String> failures = new ArrayList<>();

        for (Method setter : candidates) {
            try {
                setter.invoke(metadata, convertArguments(setter, value));
                return;
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException(
                        "Cannot access metadata setter " + setter.getName(),
                        exception
                );
            } catch (InvocationTargetException exception) {
                Throwable cause = exception.getCause();
                failures.add(
                        methodSignature(setter)
                                + ": " + cause.getMessage()
                );
            } catch (RuntimeException exception) {
                failures.add(
                        methodSignature(setter)
                                + ": " + exception.getMessage()
                );
            }
        }

        throw new IllegalArgumentException(
                "Invalid value for metadata property '" + propertyName
                        + "' on " + metadata.getClass().getSimpleName()
                        + " (" + String.join("; ", failures) + ")"
        );
    }

    private Object[] convertArguments(Method setter, JsonElement value) {
        Type[] genericTypes = setter.getGenericParameterTypes();
        Class<?>[] parameterTypes = setter.getParameterTypes();

        if (parameterTypes.length == 1) {
            return new Object[]{
                    convert(value, genericTypes[0], parameterTypes[0])
            };
        }

        if (!value.isJsonArray()
                || value.getAsJsonArray().size() != parameterTypes.length) {
            throw new IllegalArgumentException(
                    "setters with " + parameterTypes.length
                            + " values require a JSON array of that size"
            );
        }

        Object[] arguments = new Object[parameterTypes.length];

        for (int index = 0; index < parameterTypes.length; index++) {
            arguments[index] = convert(
                    value.getAsJsonArray().get(index),
                    genericTypes[index],
                    parameterTypes[index]
            );
        }

        return arguments;
    }

    private Object convert(
            JsonElement value,
            Type targetType,
            Class<?> targetClass
    ) {
        if (value == null || value.isJsonNull()) {
            if (targetClass.isPrimitive()) {
                throw new IllegalArgumentException(
                        "null cannot be used for " + targetClass.getSimpleName()
                );
            }

            return null;
        }

        if (Component.class.isAssignableFrom(targetClass)) {
            return LegacyComponentSerializer.legacyAmpersand()
                    .deserialize(value.getAsString());
        }

        if (targetClass == Point.class || targetClass == Pos.class) {
            return readPosition(value);
        }

        if (targetClass == Vec.class) {
            Point point = readPosition(value);
            return new Vec(point.x(), point.y(), point.z());
        }

        if (targetClass == ItemStack.class) {
            return readItemStack(value);
        }

        if (targetClass == Color.class) {
            return readColor(value);
        }

        if (targetClass == Block.class && value.isJsonPrimitive()) {
            Block block = Block.fromKey(value.getAsString());

            if (block == null) {
                throw new IllegalArgumentException(
                        "unknown block " + value.getAsString()
                );
            }

            return block;
        }

        if (targetClass == UUID.class) {
            return UUID.fromString(value.getAsString());
        }

        if ((RegistryKey.class.isAssignableFrom(targetClass)
                || targetClass == Holder.class)
                && value.isJsonPrimitive()) {
            return RegistryKey.unsafeOf(value.getAsString());
        }

        if (targetClass.isEnum()) {
            String requestedValue = normalize(value.getAsString());

            for (Object constant : targetClass.getEnumConstants()) {
                if (normalize(((Enum<?>) constant).name())
                        .equals(requestedValue)) {
                    return constant;
                }
            }

            throw new IllegalArgumentException(
                    "unknown " + targetClass.getSimpleName()
                            + " value " + value.getAsString()
            );
        }

        if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
            Object resolved = invokeStringFactory(
                    targetClass,
                    value.getAsString()
            );

            if (resolved != null) {
                return resolved;
            }
        }

        return GSON.fromJson(value, targetType);
    }

    private Pos readPosition(JsonElement value) {
        if (value.isJsonArray() && value.getAsJsonArray().size() == 3) {
            return new Pos(
                    value.getAsJsonArray().get(0).getAsDouble(),
                    value.getAsJsonArray().get(1).getAsDouble(),
                    value.getAsJsonArray().get(2).getAsDouble()
            );
        }

        if (!value.isJsonObject()) {
            throw new IllegalArgumentException(
                    "position must be {x,y,z} or [x,y,z]"
            );
        }

        JsonObject object = value.getAsJsonObject();
        return new Pos(
                object.get("x").getAsDouble(),
                object.get("y").getAsDouble(),
                object.get("z").getAsDouble()
        );
    }

    private ItemStack readItemStack(JsonElement value) {
        String materialId;
        int amount = 1;

        if (value.isJsonPrimitive()) {
            materialId = value.getAsString();
        } else {
            JsonObject object = value.getAsJsonObject();
            materialId = object.get("material").getAsString();

            if (object.has("amount")) {
                amount = object.get("amount").getAsInt();
            }
        }

        Material material = Material.fromKey(materialId);

        if (material == null) {
            throw new IllegalArgumentException(
                    "unknown material " + materialId
            );
        }

        return ItemStack.of(material, amount);
    }

    private Color readColor(JsonElement value) {
        if (value.isJsonPrimitive()) {
            return new Color(value.getAsInt());
        }

        Point channels = readPosition(value);
        return new Color(
                (int) channels.x(),
                (int) channels.y(),
                (int) channels.z()
        );
    }

    private Object invokeStringFactory(
            Class<?> targetClass,
            String value
    ) {
        for (String methodName : new String[]{
                "fromKey",
                "fromNamespaceId",
                "valueOf"
        }) {
            try {
                Method factory = targetClass.getMethod(
                        methodName,
                        String.class
                );

                if (!Modifier.isStatic(factory.getModifiers())
                        || !targetClass.isAssignableFrom(
                        factory.getReturnType()
                )) {
                    continue;
                }

                return factory.invoke(null, value);
            } catch (NoSuchMethodException ignored) {
                // Try the next conventional factory name.
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException(
                        "Cannot access " + targetClass.getSimpleName()
                                + "." + methodName,
                        exception
                );
            } catch (InvocationTargetException exception) {
                throw new IllegalArgumentException(
                        exception.getCause().getMessage(),
                        exception.getCause()
                );
            }
        }

        return null;
    }

    private String methodSignature(Method method) {
        StringBuilder signature = new StringBuilder(method.getName())
                .append('(');

        for (int index = 0; index < method.getParameterCount(); index++) {
            if (index > 0) {
                signature.append(", ");
            }

            signature.append(
                    method.getParameterTypes()[index].getSimpleName()
            );
        }

        return signature.append(')').toString();
    }

    private String normalize(String value) {
        return value.replaceAll("[^A-Za-z0-9]", "")
                .toLowerCase(Locale.ROOT);
    }
}
