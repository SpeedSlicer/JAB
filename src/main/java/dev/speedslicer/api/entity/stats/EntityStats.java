package dev.speedslicer.api.entity.stats;

import java.util.Map;

public record EntityStats(Map<EntityStatType, Double> stats) {
}
