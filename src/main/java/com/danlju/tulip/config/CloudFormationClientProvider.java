package com.danlju.tulip.config;

import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class CloudFormationClientProvider {
    public AmazonCloudFormation cloudFormationClient(String region) {
        return AmazonCloudFormationClientBuilder.standard().withRegion(
                region
        ).build();
    }
}
