package dev.speedslicer.api.entity.ai;

import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;

import java.util.List;
import java.util.function.Function;

public class TemplateEntityAI {
    private final Function<EntityCreature, List<GoalSelector>> goals;
    private final Function<EntityCreature, List<TargetSelector>> targets;

    public TemplateEntityAI(
            Function<EntityCreature, List<GoalSelector>> goals,
            Function<EntityCreature, List<TargetSelector>> targets
    ) {
        this.goals = goals;
        this.targets = targets;
    }

    public List<GoalSelector> createGoals(EntityCreature entity) {
        return goals.apply(entity);
    }

    public List<TargetSelector> createTargets(EntityCreature entity) {
        return targets.apply(entity);
    }
}