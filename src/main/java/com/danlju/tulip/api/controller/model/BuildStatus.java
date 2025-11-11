package com.danlju.tulip.api.controller.model;

public enum BuildStatus {
    SUCCESS("success"),
    FAILURE("failure"),
    PENDING("pending"),
    DEPLOYING("deploying"),
    LAUNCHING("launching"),
    UNKNOWN("unknown");

    private final String status;

    BuildStatus(String status) {
        this.status = status;
    }
}
