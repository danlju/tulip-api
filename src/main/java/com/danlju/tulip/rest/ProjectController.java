package com.danlju.tulip.rest;

import com.danlju.tulip.domain.Build;
import com.danlju.tulip.domain.Project;
import com.danlju.tulip.repo.ProjectRepository;
import com.danlju.tulip.rest.model.ProjectModel;
import com.danlju.tulip.service.BuildService;
import com.danlju.tulip.service.WorkflowRunsService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WorkflowRunsService workflowRunsService;

    @Autowired
    private BuildService buildService;

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectModel>> list() {
        List<Project> projects = projectRepository.findAll();
        var models = projects.stream().map(this::toModel).collect(Collectors.toList());
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    @GetMapping("/projects/{repo}")
    public List<BuildsResponseModelBuild> getWorkflowRuns(@PathVariable String repo) {
        var builds = buildService.getBuildsForProject("danlju", repo);

        return toBuildsResponseModel(builds);
    }

    @GetMapping("/projects/sync")
    public String sync() {
        buildService.syncBuilds("danlju", "tulip-api");

        return "ok";
    }

    @GetMapping("/projects/{repo}/refresh")
    public BuildsResponseModel refreshWorkflowRuns(@PathVariable String repo) {
        //return toBuildsResponseModel(
          //      workflowRunsService.refreshWorkflowRuns("danlju", repo)
   //     );
        return null;
    }

    @PostMapping(value = "/projects/{repo}/run", consumes = "application/json")
    public String startWorkflowRun(@RequestBody StartWorkflowRequest startWorkflowRequest) {
        return workflowRunsService.startWorkflowRun(startWorkflowRequest.owner, startWorkflowRequest.projectId, startWorkflowRequest.workflowId, startWorkflowRequest.branch).toString();
    }

    @PostMapping(value = "/projects", consumes = "application/json")
    private String createProject(@RequestBody CreateProjectModel model) {

        projectRepository.save(new Project(UUID.randomUUID(), model.name, model.githubName, 10, Instant.ofEpochMilli(0), "unknown", Instant.ofEpochMilli(0)));

        return "ok";
    }

    private ProjectModel toModel(Project project) {
        return new ProjectModel(project.getId().toString(), project.getPublicId().toString(), project.getName(), project.getGithubName());
    }

    private List<BuildsResponseModelBuild> toBuildsResponseModel(List<Build> workflowRuns) {
        List<BuildsResponseModelBuild> builds = new ArrayList<>();
        for (Build build : workflowRuns) {
            builds.add(
                    new BuildsResponseModelBuild(Long.parseLong(build.getExternalId()), "#" + build.getNumber().toString() + " [" + build.getCommitMessage() + "]", build.getStatus(), build.getStartedByUser(),build.getCommit(), build.getBranch(), build.getNumber().toString(), "TODO: displayTitle", calculateDuration(build.getStartedAt(), build.getUpdatedAt()))
            );
        }

        return builds;
    }

    private String calculateDuration(Instant startedAt, Instant updatedAt) {
        long seconds = updatedAt.getEpochSecond() - startedAt.getEpochSecond();
        if (seconds < 60) {
            return seconds + "s";
        }
        long h = seconds / 60;
        long m = seconds / 60 % 60;

        String duration = "";
        if (h >0) {
            duration += h + "h";
        }

        return duration + " " + m +"m";
    }

    private String mapStatus(String conclusion, String status) {
        if (conclusion == null || conclusion.isBlank()) {
            if (Objects.equals(status, "failed")) {
                return "failure";
            } else if (Objects.equals(status, "in_progress")) {
                return "running";
            } else if (
                    Objects.equals(status, "waiting")
                            || Objects.equals(status, "queued")
                            || Objects.equals(status, "requested")) {
                return "pending";
            }
        } else if (conclusion.equals("failure")) {
            return "failure";
        } else if (conclusion.equals("success")) {
            return "success";
        }

        return "unknown";
    }

    public record StartWorkflowRequest(
            String owner,
            String projectId,
            String workflowId,
            String branch
    ) {
    }

    public record CreateProjectModel(
            String owner,
            String name,
            String githubName
    ) {
    }

    public record BuildsResponseModel(
            @JsonProperty("total_count") int totalCount,
            @JsonProperty("builds") List<BuildsResponseModelBuild> builds
    ) {
    }

    public static class BuildsResponseModelBuild {
        public long id;
        public String name;
        public String status;
        public String startedByUser;
        public String commit;
        public String branch;
        public String runNumber;
        public String displayTitle;
        public String duration;

        public BuildsResponseModelBuild(long id,
                                        String name,
                                        String status,
                                        String startedByUser,
                                        String commit,
                                        String branch,
                                        String runNumber,
                                        String displayTitle,
                                        String duration) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.startedByUser = startedByUser;
            this.commit = commit;
            this.branch = branch;
            this.runNumber = runNumber;
            this.displayTitle = displayTitle;
            this.duration = duration;
        }
    }
}