package com.danlju.tulip.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

//@PropertySource("classpath:/tulip.yml")
//@ConfigurationProperties(prefix = "tulip")
// TODO: read from config file
@Component
public class TulipConfig {

    private static final Logger logger = LoggerFactory.getLogger(TulipConfig.class);

    private GithubProperties githubProperties = new GithubProperties("danlju");

    public GithubProperties getGithubProperties() {
        return githubProperties;
    }

    public static class GithubProperties {
        private String account;

        public GithubProperties(String account) {
            this.account = account;
        }

        public String getAccount() {
            return account;
        }
    }


    // TODO: Move
    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create("http://localhost:9324"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("test", "test")
                        )
                )
                .build();
    }
}
