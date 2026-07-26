package dev.speedslicer.server.bootstrap.data.entity;

import dev.speedslicer.api.entity.ai.TemplateEntityAI;
import dev.speedslicer.server.main.ServerController;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;

import java.time.Duration;
import java.util.List;

public class EntityAIDataLoader {
    public void bootstrapLoad(ServerController serverController) {
        var reg = serverController.getEntityAIRegistry();
        reg.register("aggressive_attack_player",
                new TemplateEntityAI(
                        entity ->
                                List.of(new MeleeAttackGoal(entity, 4.0, Duration.ofSeconds(2))),
                        entity ->
                                List.of(
                                        new ClosestEntityTarget(entity, 10.0, target -> target instanceof Player)
                                )
                )
        );
    }
}
