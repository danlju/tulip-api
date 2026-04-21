package com.danlju.tulip.application.usecases.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public record BuildRequest(int version, Build build,
                           Repository repository,
                           Source source,
                           Execution execution,
                           Map<String, String> env, Instant sentAt) {
    public static final int CURRENT_VERSION = 1;

    @JsonCreator
    public BuildRequest(
            @JsonProperty("version") int version,
            @JsonProperty("build") Build build,
            @JsonProperty("repository") Repository repository,
            @JsonProperty("source") Source source,
            @JsonProperty("execution") Execution execution,
            @JsonProperty("env") Map<String, String> env,
            @JsonProperty("sentAt") Instant sentAt
    ) {
        this.version = version;
        this.build = build;
        this.repository = repository;
        this.source = source;
        this.execution = execution;
        this.env = env;
        this.sentAt = sentAt;
    }

    public static class Build {
        private final String id;
        private final String projectId;

        @JsonCreator
        public Build(
                @JsonProperty("id") String id,
                @JsonProperty("projectId") String projectId
        ) {
            this.id = id;
            this.projectId = projectId;
        }

        public String getId() {
            return id;
        }

        public String getProjectId() {
            return projectId;
        }
    }

    public enum SourceProvider {
        GENERIC,
        GITHUB,
        GITLAB
    }

    public static class Repository {
        private final String cloneUrl;
        private final SourceProvider provider;

        @JsonCreator
        public Repository(
                @JsonProperty("cloneUrl") String cloneUrl,
                @JsonProperty("provider") SourceProvider provider
        ) {
            this.cloneUrl = cloneUrl;
            this.provider = provider;
        }

        public String getCloneUrl() {
            return cloneUrl;
        }

        public SourceProvider getProvider() {
            return provider;
        }
    }

    public static class Source {
        private final String branch;
        private final String commitSha;
        private final String commitMessage;

        @JsonCreator
        public Source(
                @JsonProperty("branch") String branch,
                @JsonProperty("commitSha") String commitSha,
                @JsonProperty("commitMessage") String commitMessage
        ) {
            this.branch = branch;
            this.commitSha = commitSha;
            this.commitMessage = commitMessage;
        }

        public String getBranch() {
            return branch;
        }

        public String getCommitSha() {
            return commitSha;
        }

        public String getCommitMessage() {
            return commitMessage;
        }
    }

    public static class Execution {
        private final String configPath;

        @JsonCreator
        public Execution(
                @JsonProperty("configPath") String configPath
        ) {
            this.configPath = configPath;
        }

        public String getConfigPath() {
            return configPath;
        }
    }

    public static BuildRequest of(String buildId, String projectId,
                                  String cloneUrl, String provider,
                                  String branch, String commitSha, String commitMessage,
                                  String configPath,
                                  HashMap<String, String> envVars) {
        return new BuildRequest(
                BuildRequest.CURRENT_VERSION,
                new BuildRequest.Build(buildId, projectId),
                new BuildRequest.Repository(cloneUrl, SourceProvider.valueOf(provider.toUpperCase())),
                new BuildRequest.Source(branch, commitSha, commitMessage),
                new BuildRequest.Execution(configPath),
                envVars,
                Instant.now()
        );
    }

}