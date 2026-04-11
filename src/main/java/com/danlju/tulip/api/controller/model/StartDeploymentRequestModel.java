package com.danlju.tulip.api.controller.model;

public record StartDeploymentRequestModel(
        String externalId,
        String region,
        String environment
) {
}
