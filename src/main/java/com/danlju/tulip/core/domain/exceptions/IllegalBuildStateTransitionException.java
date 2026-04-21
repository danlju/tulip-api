package com.danlju.tulip.core.domain.exceptions;

import com.danlju.tulip.core.domain.BuildStatus;

// TODO: enrich with build id etc...
public class IllegalBuildStateTransitionException extends RuntimeException {
    public IllegalBuildStateTransitionException(BuildStatus fromStatus, BuildStatus toStatus) {
        super(String.format(
                "Invalid build state transition %s →  %s",
                fromStatus, toStatus
        ));
    }
}
