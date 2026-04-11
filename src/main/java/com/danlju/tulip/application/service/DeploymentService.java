package com.danlju.tulip.application.service;

import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.model.CreateStackRequest;
import com.amazonaws.services.cloudformation.model.CreateStackResult;
import com.amazonaws.services.cloudformation.model.Parameter;
import com.amazonaws.services.cloudformation.model.Tag;
import com.danlju.tulip.application.repository.BuildRepository;
import com.danlju.tulip.application.repository.DeploymentRepository;
import com.danlju.tulip.application.usecases.DeploymentUseCases;
import com.danlju.tulip.application.usecases.DestroyDeploymentResult;
import com.danlju.tulip.application.usecases.Result;
import com.danlju.tulip.application.usecases.StartDeploymentResult;
import com.danlju.tulip.config.CloudFormationClientProvider;
import com.danlju.tulip.core.domain.Deployment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
    public Result<StartDeploymentResult> startDeploy(String externalId, String region, String environment) {
        AmazonCloudFormation cloudFormationClient = cloudFormationClientProvider.cloudFormationClient(region);

        Tag tag = new Tag();
        tag.setKey("version"); // TODO: move tags to template
        tag.setValue("tulip-api-" + externalId);

        CreateStackRequest createStackRequest = new CreateStackRequest()
                .withStackName("tulip-api-" + environment) // TODO: unique name
                .withTags(tag)
                .withTemplateURL("http://tulip-cf-templates.s3.ap-northeast-1.amazonaws.com/tulip-api-0.1-cf.yml")
                .withParameters(readParametersFromFile("infrastructure/" + environment + "-params.json")) // TODO: fix
                .withCapabilities("CAPABILITY_IAM");

        CreateStackResult result = cloudFormationClient.createStack(createStackRequest);

        logger.info("Created stack: {}", result.getStackId());

        // TODO: what is result if createStack fails?
        if (result.getStackId() == null || result.getStackId().isBlank())  {
            return new Result<>(false, null, "Failed creating stack");
        }

        var build = buildRepository.findByExternalId(externalId);
        var deployment = deploymentRepository.save(new Deployment(UUID.randomUUID(), build.getId(), result.getStackId(), Instant.now(), "pending"));

        return new Result<>(new StartDeploymentResult(result.getStackId()));
    }

    @Override
    public Result<DestroyDeploymentResult> destroyDeployment(String stackId) {
        return null;
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
