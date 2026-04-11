package com.danlju.tulip.api.controller;

import com.danlju.tulip.api.controller.model.StartDeploymentRequestModel;
import com.danlju.tulip.application.usecases.DeploymentUseCases;
import com.danlju.tulip.config.TulipConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeploymentController {

    private static final Logger logger = LoggerFactory.getLogger(DeploymentController.class);

    @Autowired
    private TulipConfig tulipConfig;

    @Autowired
    private DeploymentUseCases deployUseCases;

    @PostMapping("/deploy")
    public ResponseEntity<StartDeploymentResponseModel> startDeployment(@RequestBody StartDeploymentRequestModel requestModel) {
        // repo, version, environment (dev, prod etc)
        logger.info("Received deployment request: {}", requestModel);

        var result = deployUseCases.startDeploy(requestModel.externalId(), requestModel.region(), requestModel.environment());

        if (!result.isSuccess()) {
            logger.error("Start Deploy not successful: {}", result.getErrorMessage());
            return ResponseEntity.badRequest().body(
                    StartDeploymentResponseModel.error(result.getErrorMessage())
            ); // TODO: fix
        }
        var response = new StartDeploymentResponseModel(result.getData().stackId());
        return ResponseEntity.ok(response);// TODO: fix
    }

}
