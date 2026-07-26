package dev.speedslicer.api.entity.utils;

import dev.speedslicer.api.entity.EntityData;
import dev.speedslicer.api.entity.ai.EntityAIApplier;
import dev.speedslicer.api.entity.meta.EntityPropertyApplier;
import dev.speedslicer.server.bootstrap.data.entity.EntityAIDataLoader;
import dev.speedslicer.server.bootstrap.registry.EntityAIRegistry;
import dev.speedslicer.server.bootstrap.registry.EntityRegistry;
import dev.speedslicer.server.main.Main;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAI;

public class EntityDataConstructor {
    static EntityPropertyApplier propertyApplier = new EntityPropertyApplier();
    static EntityAIApplier aiApplier = new EntityAIApplier();
    public static EntityCreature generateEntityCreatureFromData(EntityData entityData, EntityAIRegistry aiRegistry) {
        EntityCreature entity = new EntityCreature(EntityType.fromKey(entityData.baseEntity()));
        entity.setCustomName(LegacyComponentSerializer.legacyAmpersand().deserialize(entityData.nametag()));
        // apply properties
        propertyApplier.apply(entity, entityData.properties());
        // add AI
        for (var x : entityData.associatedAI()) {
            var y = aiRegistry.get(x);
            if (y != null) {
                aiApplier.apply(entity, y);
            }
            else {
                Main.getLogger().error("Attempted to load AI {} in mob {}, but AI did not exist or was not registered", x, entityData.id());
            }
        }
        return entity;
    }
}
