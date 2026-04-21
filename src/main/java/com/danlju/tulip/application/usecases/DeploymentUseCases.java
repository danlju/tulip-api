package com.danlju.tulip.application.usecases;

import com.danlju.tulip.application.usecases.model.StartDeploymentResult;

public interface DeploymentUseCases {
    StartDeploymentResult startDeploy(String externalId, String region, String environment);
}
