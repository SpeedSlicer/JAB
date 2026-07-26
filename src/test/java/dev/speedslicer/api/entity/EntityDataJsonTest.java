package dev.speedslicer.api.entity;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityDataJsonTest {

    @Test
    void acceptsLegacyAndConfiguredAiEntries() {
        String json = """
                {
                  "version": 1,
                  "id": "test:entity",
                  "baseEntity": "minecraft:zombie",
                  "associatedAI": [
                    "test:legacy",
                    {
                      "id": "test:configured",
                      "data": {
                        "range": 8.5
                      }
                    }
                  ],
                  "properties": {
                    "glowing": true
                  }
                }
                """;

        EntityData entityData = new Gson().fromJson(json, EntityData.class);

        assertEquals("test:legacy", entityData.associatedAI().get(0).id());
        assertTrue(entityData.associatedAI().get(0).data().isEmpty());
        assertEquals(
                8.5,
                entityData.associatedAI()
                        .get(1)
                        .data()
                        .get("range")
                        .getAsDouble()
        );
        assertTrue(entityData.metadata().get("glowing").getAsBoolean());
    }
}
