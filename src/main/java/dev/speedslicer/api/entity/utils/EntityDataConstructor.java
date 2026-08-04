package dev.speedslicer.api.entity.utils;

import dev.speedslicer.api.entity.EntityData;
import dev.speedslicer.api.entity.ai.EntityAIApplier;
import dev.speedslicer.api.entity.ai.EntityAIData;
import dev.speedslicer.api.entity.items.EntityEquipmentData;
import dev.speedslicer.api.entity.meta.EntityPropertyApplier;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.api.item.utils.ItemStackConstructor;
import dev.speedslicer.server.bootstrap.registry.impl.EntityAIRegistry;
import dev.speedslicer.server.bootstrap.registry.impl.ItemRegistry;
import dev.speedslicer.server.main.Main;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;

import java.util.LinkedHashMap;
import java.util.Map;

public final class EntityDataConstructor {
    private static final EntityPropertyApplier PROPERTY_APPLIER =
            new EntityPropertyApplier();
    private static final EntityAIApplier AI_APPLIER = new EntityAIApplier();

    private EntityDataConstructor() {
    }

    public static EntityCreature generateEntityCreatureFromData(
            EntityData entityData,
            EntityAIRegistry aiRegistry,
            ItemRegistry itemRegistry
    ) {
        if (entityData == null) {
            throw new IllegalArgumentException("entityData cannot be null");
        }

        EntityType type = EntityType.fromKey(entityData.baseEntity());

        if (type == null) {
            throw new IllegalArgumentException(
                    "Unknown entity type: " + entityData.baseEntity()
            );
        }

        EntityCreature entity = new EntityCreature(type);

        if (entityData.nametag() != null && !entityData.nametag().isBlank()) {
            entity.setCustomName(
                    LegacyComponentSerializer
                            .legacyAmpersand()
                            .deserialize(entityData.nametag())
            );
        }

        PROPERTY_APPLIER.apply(
                entity,
                entityData.metadata()
        );

        for (EntityAIData aiData : entityData.associatedAI()) {
            if (aiData == null || aiData.id() == null
                    || aiData.id().isBlank()) {
                continue;
            }

            var ai = aiRegistry.get(aiData.id());

            if (ai != null) {
                AI_APPLIER.apply(entity, ai, aiData);
            } else {
                Main.getLogger().error(
                        "Attempted to load AI {} in mob {}, but AI did not exist or was not registered",
                        aiData.id(),
                        entityData.id()
                );
            }
        }

        applyEquipment(entity, entityData.equipment(), itemRegistry);

        return entity;
    }

    public static EntityCreature generateEntityCreatureFromData(
            EntityData entityData,
            EntityAIRegistry aiRegistry
    ) {
        if (entityData != null && entityData.equipment() != null) {
            throw new IllegalArgumentException(
                    "An item registry is required for entity equipment"
            );
        }

        return generateEntityCreatureFromData(
                entityData,
                aiRegistry,
                new ItemRegistry()
        );
    }

    private static void applyEquipment(
            EntityCreature entity,
            EntityEquipmentData equipment,
            ItemRegistry itemRegistry
    ) {
        if (equipment == null) {
            return;
        }

        Map<EquipmentSlot, String> slots = new LinkedHashMap<>();
        slots.put(EquipmentSlot.HELMET, equipment.helmet());
        slots.put(EquipmentSlot.CHESTPLATE, equipment.chestplate());
        slots.put(EquipmentSlot.LEGGINGS, equipment.leggings());
        slots.put(EquipmentSlot.BOOTS, equipment.boots());
        slots.put(EquipmentSlot.MAIN_HAND, equipment.mainHand());
        slots.put(EquipmentSlot.OFF_HAND, equipment.offHand());
        slots.put(EquipmentSlot.BODY, equipment.body());
        slots.put(EquipmentSlot.SADDLE, equipment.saddle());

        for (Map.Entry<EquipmentSlot, String> entry : slots.entrySet()) {
            String itemId = entry.getValue();

            if (itemId == null || itemId.isBlank()) {
                continue;
            }

            ItemData itemData = itemRegistry.getItem(itemId);

            if (itemData == null) {
                throw new IllegalArgumentException(
                        "Unknown item '" + itemId + "' in "
                                + entry.getKey().name().toLowerCase()
                                + " equipment slot"
                );
            }

            entity.setEquipment(
                    entry.getKey(),
                    ItemStackConstructor.constructItemFromData(itemData)
            );
        }
    }

}
