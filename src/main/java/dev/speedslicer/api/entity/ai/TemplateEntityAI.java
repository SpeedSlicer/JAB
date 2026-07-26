package dev.speedslicer.api.entity.ai;

import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TemplateEntityAI {
    private final BiFunction<
            EntityCreature,
            Map<String, com.google.gson.JsonElement>,
            List<GoalSelector>
            > goals;
    private final BiFunction<
            EntityCreature,
            Map<String, com.google.gson.JsonElement>,
            List<TargetSelector>
            > targets;

    public TemplateEntityAI(
            BiFunction<
                    EntityCreature,
                    Map<String, com.google.gson.JsonElement>,
                    List<GoalSelector>
                    > goals,
            BiFunction<
                    EntityCreature,
                    Map<String, com.google.gson.JsonElement>,
                    List<TargetSelector>
                    > targets
    ) {
        this.goals = goals;
        this.targets = targets;
    }

    public TemplateEntityAI(
            Function<EntityCreature, List<GoalSelector>> goals,
            Function<EntityCreature, List<TargetSelector>> targets
    ) {
        this(
                (entity, data) -> goals.apply(entity),
                (entity, data) -> targets.apply(entity)
        );
    }

    public List<GoalSelector> createGoals(
            EntityCreature entity,
            Map<String, com.google.gson.JsonElement> data
    ) {
        return List.copyOf(goals.apply(entity, data));
    }

    public List<TargetSelector> createTargets(
            EntityCreature entity,
            Map<String, com.google.gson.JsonElement> data
    ) {
        return List.copyOf(targets.apply(entity, data));
    }

    public List<GoalSelector> createGoals(EntityCreature entity) {
        return createGoals(entity, Map.of());
    }

    public List<TargetSelector> createTargets(EntityCreature entity) {
        return createTargets(entity, Map.of());
    }
}
