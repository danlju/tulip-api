package com.danlju.tulip.application.service;

import com.amazonaws.services.cloudformation.model.Parameter;
import com.danlju.tulip.application.repository.BuildRepository;
import com.danlju.tulip.application.repository.DeploymentRepository;
import com.danlju.tulip.application.usecases.DeploymentUseCases;
import com.danlju.tulip.application.usecases.model.StartDeploymentResult;
import com.danlju.tulip.config.CloudFormationClientProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DeploymentService implements DeploymentUseCases {

    private static final Logger logger = LoggerFactory.getLogger(DeploymentService.class);

    private final DeploymentRepository deploymentRepository;
    private final BuildRepository buildRepository;
    private CloudFormationClientProvider cloudFormationClientProvider;

    @Autowired
    public DeploymentService(DeploymentRepository deploymentRepository, BuildRepository buildRepository, CloudFormationClientProvider cloudFormationClientProvider) {
        this.deploymentRepository = deploymentRepository;
        this.buildRepository = buildRepository;
        this.cloudFormationClientProvider = cloudFormationClientProvider;
    }

    @Override
    public StartDeploymentResult startDeploy(String externalId, String region, String environment) {
        // TODO: rewrite this method
        return new StartDeploymentResult("");
    }

    private List<Parameter> readParametersFromFile(String paramsJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(
                    new File(paramsJson),
                    new TypeReference<>() {
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
