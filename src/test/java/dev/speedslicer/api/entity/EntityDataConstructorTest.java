package dev.speedslicer.api.entity;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import dev.speedslicer.MinestomTestBootstrap;
import dev.speedslicer.api.entity.ai.EntityAIData;
import dev.speedslicer.api.entity.ai.TemplateEntityAI;
import dev.speedslicer.api.entity.items.EntityEquipmentData;
import dev.speedslicer.api.entity.meta.EntityPropertyApplier;
import dev.speedslicer.api.entity.utils.EntityDataConstructor;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.api.item.data.ItemDisplayOptions;
import dev.speedslicer.server.bootstrap.registry.EntityAIRegistry;
import dev.speedslicer.server.bootstrap.registry.ItemRegistry;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.metadata.MobMeta;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityDataConstructorTest {

    @BeforeAll
    static void initializeMinestom() {
        MinestomTestBootstrap.ensureInitialized();
    }

    @Test
    void appliesBaseAndEntitySpecificMetadata() {
        EntityCreature entity = new EntityCreature(EntityType.ZOMBIE);

        new EntityPropertyApplier().apply(
                entity,
                Map.of(
                        "glowing", new JsonPrimitive(true),
                        "silent", new JsonPrimitive(true),
                        "noAi", new JsonPrimitive(true)
                )
        );

        assertTrue(entity.isGlowing());
        assertTrue(entity.isSilent());
        assertTrue(((MobMeta) entity.getEntityMeta()).isNoAi());
    }

    @Test
    void appliesMultiValueMetadataSetters() {
        EntityCreature entity = new EntityCreature(EntityType.BLOCK_DISPLAY);
        JsonArray brightness = new JsonArray();
        brightness.add(12);
        brightness.add(7);

        new EntityPropertyApplier().apply(
                entity,
                Map.of("brightness", brightness)
        );

        AbstractDisplayMeta metadata =
                (AbstractDisplayMeta) entity.getEntityMeta();
        assertEquals(12, metadata.getBlockLight());
        assertEquals(7, metadata.getSkyLight());
    }

    @Test
    void suppliesCustomDataToAiAndEquipsRegisteredItems() {
        AtomicReference<Double> receivedRange = new AtomicReference<>();
        EntityAIRegistry aiRegistry = new EntityAIRegistry();
        aiRegistry.register(
                "test:configured",
                new TemplateEntityAI(
                        (entity, data) -> {
                            receivedRange.set(
                                    data.get("range").getAsDouble()
                            );
                            return List.of();
                        },
                        (entity, data) -> List.of()
                )
        );

        ItemRegistry itemRegistry = new ItemRegistry();
        itemRegistry.register(
                "test:helmet",
                new ItemData(
                        1,
                        "test:helmet",
                        "Helmet",
                        List.of(),
                        "minecraft:iron_helmet",
                        1,
                        new ItemDisplayOptions(
                                false,
                                List.of(),
                                List.of(),
                                List.of(),
                                List.of()
                        ),
                        Map.of()
                )
        );

        EntityData data = new EntityData(
                1,
                "test:zombie",
                "&cZombie",
                "minecraft:zombie",
                List.of(
                        new EntityAIData(
                                "test:configured",
                                Map.of("range", new JsonPrimitive(12.5))
                        )
                ),
                Map.of(),
                new EntityEquipmentData(
                        "test:helmet",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );

        EntityCreature entity =
                EntityDataConstructor.generateEntityCreatureFromData(
                        data,
                        aiRegistry,
                        itemRegistry
                );

        assertEquals(12.5, receivedRange.get());
        assertEquals(
                Material.IRON_HELMET,
                entity.getEquipment(EquipmentSlot.HELMET).material()
        );
    }
}
