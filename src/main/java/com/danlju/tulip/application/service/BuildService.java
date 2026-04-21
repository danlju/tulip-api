package com.danlju.tulip.application.service;

import com.danlju.tulip.application.usecases.model.BuildRequest;
import com.danlju.tulip.application.usecases.model.RequestBuildResult;
import com.danlju.tulip.core.domain.BuildStatus;
import com.danlju.tulip.application.usecases.BuildUseCases;
import com.danlju.tulip.core.domain.Build;
import com.danlju.tulip.github.GitHubClient;
import com.danlju.tulip.application.repository.BuildRepository;
import com.danlju.tulip.application.repository.ProjectRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.*;

@Service
public class BuildService implements BuildUseCases {

    private static final Logger logger = LoggerFactory.getLogger(BuildService.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GitHubClient gitHubClient;

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private SqsClient sqsClient;

    public BuildService(ProjectRepository projectRepository, GitHubClient gitHubClient, BuildRepository buildRepository) {
        this.projectRepository = projectRepository;
        this.gitHubClient = gitHubClient;
        this.buildRepository = buildRepository;
    }

//    @CachePut(value = "builds", key = "#repo")
    @Override
    public List<Build> getBuildsForProjectByPublicId(UUID publicId) {
        return buildRepository.findByProjectPublicId(publicId);
    }

    @Override
    public void syncBuilds(String repo) {
//        // TODO: move out from method and use as a parameter?
//        var project = projectRepository.findByGithubName(repo);
//
//        var runs = gitHubClient.getAllBuilds(owner, repo, project.getLastSyncedAt().minus(SyncBuildsTask.SYNC_RATE_MS, ChronoUnit.MILLIS));
//
//        logger.info("Found {} builds", runs.workflowRuns().size());
//
//        Instant lastSynced = Instant.now();
//        for (var run : runs.workflowRuns()) {
//            var build = buildRepository.findByExternalId(String.valueOf(run.id()));
//
//            if (build == null) {
//                buildRepository.save(mapRun(run, project));
//            } else if (Instant.parse(run.updatedAt()).isAfter(project.getLastSyncedAt())) {
//                logger.info("Syncing build in database for external ID: {}", build.getExternalId());
//                build.setStatus(Utils.mapGithubStatus(run.conclusion(), run.status()));
//                build.setUpdatedAt(Instant.parse(run.updatedAt()));
//                project.setLastSyncedAt(Instant.parse(run.updatedAt()));
//                buildRepository.save(build);
//            }
//            if (Instant.parse(run.updatedAt()).isAfter(project.getMostRecentBuild())) {
//                project.setMostRecentBuild(Instant.parse(run.updatedAt()));
//                project.setMostRecentBuildStatus(Utils.mapGithubStatus(run.conclusion(), run.status()));
//            }
//        }
//        projectRepository.save(project);
    }

    @Override
    public Build getBuild(String repo, String buildId) {
        var project = projectRepository.findByGithubName(repo);
        return null;
        //return mapRun(gitHubClient.getBuild(owner, repo, Long.parseLong(buildId)), project);
    }

    @Override
    @Transactional
    public RequestBuildResult requestBuild(String repo, String branch, String commit, String user) {

        var project = projectRepository.findByPublicIdForUpdate(UUID.fromString(repo));

        logger.info("Fetch Project with UUID={}", repo);

        var build = Build.create(
                UUID.randomUUID(),
                project.getNextBuildNumber(),
                project.getId(),
                branch,
                commit
        );

        build.setProjectId(project.getId());
        build.setNumber(project.getNextBuildNumber());

        project.setNextBuildNumber(project.getNextBuildNumber() + 1);
        projectRepository.save(project);

        var savedBuild = buildRepository.save(build);

        // TODO: get from elsewhere
        HashMap<String, String> envVars = new HashMap<>();
        envVars.put("ENV_VAR", "value");

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);;
        String payload;
        try {
            var buildRequest = BuildRequest.of(
                    Integer.toString(savedBuild.getId()),
                    project.getId().toString(), // TODO
                    build.getCloneUrl(),
                    "GENERIC", // TODO: ?
                    build.getBranch(),
                    build.getCommitSha(),
                    build.getCommitMessage(),
                    project.getConfigPath() == null || project.getConfigPath().isBlank() ? "tulip.yml" : project.getConfigPath(),
                    envVars
                    );

            payload = objectMapper.writeValueAsString(buildRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        logger.info("Payload: {}", payload);
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl("http://localhost:9324/tulip-queue")
                .messageBody(payload)
                .build());

        return new RequestBuildResult(build.getPublicId());
    }

    @Override
    @Transactional
    public void updateStatusForBuild(Integer buildId, String status) {

        var build = buildRepository.findById(buildId);
        build.transitionTo(BuildStatus.valueOf(status.toUpperCase()));

        buildRepository.save(build);
    }
}
