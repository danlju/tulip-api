package com.danlju.tulip.service;

import com.danlju.tulip.domain.Project;
import com.danlju.tulip.utils.Utils;
import com.danlju.tulip.domain.Build;
import com.danlju.tulip.github.GitHubClient;
import com.danlju.tulip.repo.BuildRepository;
import com.danlju.tulip.repo.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class BuildService {

    private static final Logger logger = LoggerFactory.getLogger(BuildService.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GitHubClient gitHubClient;

    @Autowired
    private BuildRepository buildRepository;

    public BuildService(ProjectRepository projectRepository, GitHubClient gitHubClient, BuildRepository buildRepository) {
        this.projectRepository = projectRepository;
        this.gitHubClient = gitHubClient;
        this.buildRepository = buildRepository;
    }

    public Build saveBuild(BuildModel buildModel) {
        return null;
    }

    public void syncBuilds(String owner, String repo) {
        // TODO: move out from method and use as a parameter?
        var project = projectRepository.findByGithubName(repo);

        var runs = gitHubClient.getAllBuilds(owner, repo, project.getLastSyncedAt());

        logger.info("Found {} builds", runs.workflowRuns().size());

        Instant lastSynced = Instant.now();
        for (var run : runs.workflowRuns()) {

            var build = buildRepository.findByExternalId(String.valueOf(run.id()));

            if (build == null) {
                buildRepository.save(mapRun(run, project));
            } else if (Instant.parse(run.updatedAt()).isAfter(project.getLastSyncedAt())) {
                logger.info("Syncing build in database for external ID: {}", build.getExternalId());
                build.setStatus(run.status());
                build.setUpdatedAt(Instant.parse(run.updatedAt()));
                buildRepository.save(build);
            }
            if (Instant.parse(run.updatedAt()).isAfter(project.getMostRecentBuild())) {
                project.setMostRecentBuild(Instant.parse(run.updatedAt()));
                project.setMostRecentBuildStatus(Utils.mapGithubStatus(run.conclusion(), run.status()));
            }
        }
        project.setLastSyncedAt(lastSynced);
        projectRepository.save(project);
    }

    private Build mapRun(WorkflowRunsService.WorkflowRun run, Project project) {
        return new Build(
                UUID.randomUUID(),
                String.valueOf(run.id()),
                project.getId(),
                Integer.parseInt(run.runNumber()),
                run.commitHash(),
                run.headBranch(),
                run.status(),
                Instant.parse(run.createdAt()),
                Instant.parse(run.updatedAt())
        );
    }
}
