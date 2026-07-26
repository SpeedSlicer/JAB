package dev.speedslicer.api.entity.meta;

import com.google.gson.JsonElement;
import net.minestom.server.entity.EntityCreature;

import java.util.Map;

public class EntityPropertyApplier {
    public void apply(EntityCreature entity, Map<String, JsonElement> properties) {
        if (properties == null) {
            return;
        }
        if (properties.containsKey("glowing")) {
            boolean glowing = properties.get("glowing").getAsBoolean();
            entity.setGlowing(glowing);
        }
    }
}