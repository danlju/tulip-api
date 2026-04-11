package com.danlju.tulip.api.controller;

public record StartDeploymentResponseModel(String stackId, String errorMessage) {
    public StartDeploymentResponseModel(String stackId) {
        this(stackId, "");
    }

    public static StartDeploymentResponseModel error(String errorMessage) {
        return new StartDeploymentResponseModel(null, errorMessage);
    }
}
