package dev.speedslicer.server.data.entity;

import com.google.gson.JsonElement;
import dev.speedslicer.api.entity.ai.TemplateEntityAI;
import dev.speedslicer.server.main.ServerController;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class EntityAIDataLoader {
    public void bootstrapLoad(ServerController serverController) {
        var reg = serverController.getEntityAIRegistry();
        reg.register("chase_attack_player",
                new TemplateEntityAI(
                        (entity, data) ->
                                List.of(
                                        new MeleeAttackGoal(
                                                entity,
                                                getDouble(
                                                        data,
                                                        "attackRange",
                                                        4.0
                                                ),
                                                Duration.ofMillis(
                                                        getLong(
                                                                data,
                                                                "attackDelayMillis",
                                                                2_000
                                                        )
                                                )
                                        ),
                                        new FollowTargetGoal(
                                                entity,
                                                Duration.MAX
                                        )
                                ),
                        (entity, data) ->
                                List.of(
                                        new ClosestEntityTarget(
                                                entity,
                                                getDouble(
                                                        data,
                                                        "targetRange",
                                                        10.0
                                                ),
                                                target ->
                                                        target instanceof Player
                                        )
                                )
                )
        );
    }

    private static double getDouble(
            Map<String, JsonElement> data,
            String key,
            double defaultValue
    ) {
        JsonElement value = data.get(key);
        return value == null ? defaultValue : value.getAsDouble();
    }

    private static long getLong(
            Map<String, JsonElement> data,
            String key,
            long defaultValue
    ) {
        JsonElement value = data.get(key);
        return value == null ? defaultValue : value.getAsLong();
    }
}
