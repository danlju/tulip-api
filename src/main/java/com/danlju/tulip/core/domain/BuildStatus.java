package com.danlju.tulip.core.domain;

import java.util.Map;
import java.util.Set;

public enum BuildStatus {
    REQUESTED,
    QUEUED,
    RUNNING,
    COMPLETED,
    FAILED;

    private static final Map<BuildStatus, Set<BuildStatus>> transitions = Map.of(
            REQUESTED, Set.of(QUEUED, RUNNING),
            QUEUED,    Set.of(RUNNING),
            RUNNING,   Set.of(COMPLETED, FAILED),
            COMPLETED, Set.of(),
            FAILED,    Set.of()
    );

    public boolean canTransitionTo(BuildStatus target) {
        if (this == target) return true;

        return transitions
                .getOrDefault(this, Set.of())
                .contains(target);
    }
}