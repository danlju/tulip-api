package com.danlju.tulip.application.usecases;

public interface DeploymentUseCases {
    Result<StartDeploymentResult> startDeploy(String externalId, String region, String environment);
    Result<DestroyDeploymentResult> destroyDeployment(String stackId);
}
