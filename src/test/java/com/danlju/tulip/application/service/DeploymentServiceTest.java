package com.danlju.tulip.application.service;

import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.model.CreateStackResult;
import com.danlju.tulip.application.repository.BuildRepository;
import com.danlju.tulip.application.repository.DeploymentRepository;
import com.danlju.tulip.application.repository.InMemoryDeploymentRepository;
import com.danlju.tulip.config.CloudFormationClientProvider;
import com.danlju.tulip.core.domain.Build;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeploymentServiceTest {

    @Test
    public void should_return_invalid_result_when_deploy_fails() {

        CloudFormationClientProvider cloudFormationClientProvider = mock(CloudFormationClientProvider.class);
        AmazonCloudFormation mockCloudformation = mock(AmazonCloudFormation.class);
        when(mockCloudformation.createStack(any())).thenReturn(new CreateStackResult());
        when(cloudFormationClientProvider.cloudFormationClient("us-east-1")).thenReturn(mockCloudformation);

        DeploymentService deploymentService = new DeploymentService(new InMemoryDeploymentRepository(), new InMemoryBuildRepository(), cloudFormationClientProvider);
        var result = deploymentService.startDeploy("externalId", "us-east-1", "dev");

        assertFalse(result.isSuccess());
    }

    @Test
    public void should_return_valid_result_when_deploy_on_successful_deploy_request() {

        CloudFormationClientProvider cloudFormationClientProvider = mock(CloudFormationClientProvider.class);
        AmazonCloudFormation mockCloudformation = mock(AmazonCloudFormation.class);
        BuildRepository mockBuildRepository = mock(BuildRepository.class);
        DeploymentRepository mockDeploymentRepository = mock(DeploymentRepository.class);

        CreateStackResult createStackResult = new CreateStackResult();
        createStackResult.setStackId("dummyId");
        var build = new Build(1001, UUID.randomUUID(), "externalId", 1002, "user", 10, "commit", "commitMessage", "branch", "status", Instant.now(), Instant.now());

        when(mockCloudformation.createStack(any())).thenReturn(createStackResult);
        when(cloudFormationClientProvider.cloudFormationClient("us-east-1")).thenReturn(mockCloudformation);
        when(mockBuildRepository.findByExternalId("externalId")).thenReturn(build);

        DeploymentService deploymentService = new DeploymentService(mockDeploymentRepository, mockBuildRepository, cloudFormationClientProvider);
        var result = deploymentService.startDeploy("externalId", "us-east-1", "dev");

        assertTrue(result.isSuccess());
        verify(mockDeploymentRepository, times(1)).save(any());
        // TODO: compare result.data with build

    }
}