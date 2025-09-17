package com.danlju.tulip.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("workflowRuns");
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofSeconds(30))
                        .maximumSize(100)
        );
        return cacheManager;
    }
}