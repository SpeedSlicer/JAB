package dev.speedslicer.api.entity.ai;

import net.minestom.server.entity.EntityCreature;

public class EntityAIApplier {
    public void apply(
            EntityCreature entity,
            TemplateEntityAI templateAI,
            EntityAIData aiData
    ) {
        entity.addAIGroup(
                templateAI.createGoals(entity, aiData.data()),
                templateAI.createTargets(entity, aiData.data())
        );
    }

    public void apply(EntityCreature entity, TemplateEntityAI templateAI) {
        apply(entity, templateAI, new EntityAIData(""));
    }
}
