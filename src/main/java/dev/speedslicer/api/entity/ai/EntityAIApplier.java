package dev.speedslicer.api.entity.ai;

import com.google.gson.JsonElement;
import net.minestom.server.entity.EntityCreature;

import java.util.Map;

public class EntityAIApplier {
    public void apply(EntityCreature entity, TemplateEntityAI templateAI) {
        entity.addAIGroup(templateAI.createGoals(entity), templateAI.createTargets(entity));
    }
}
