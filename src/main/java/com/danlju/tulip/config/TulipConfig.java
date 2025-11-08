package com.danlju.tulip.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
}
