package com.danlju.tulip.api.controller;

import com.danlju.tulip.api.controller.model.*;
import com.danlju.tulip.config.TulipConfig;
import com.danlju.tulip.core.domain.Project;
import com.danlju.tulip.application.repository.ProjectRepository;
import com.danlju.tulip.application.service.WorkflowRunsService;
import com.danlju.tulip.application.usecases.BuildUseCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
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
    private BuildUseCases buildService;

    @Autowired
    private TulipConfig tulipConfig;

    @GetMapping("/projects")
    public List<ProjectModel> list() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(ModelMapper::toModel).collect(Collectors.toList());
    }

    @PostMapping(value = "/projects", consumes = "application/json")
    private ResponseEntity<String> createProject(@RequestBody CreateProjectModel model) {
        projectRepository.save(new Project(UUID.randomUUID(), model.name(), model.githubName(), 10, Instant.ofEpochMilli(0), "unknown", Instant.ofEpochMilli(0)));
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/projects/{repo}")
    public List<BuildResponseModel> getWorkflowRuns(@PathVariable String repo) {
        var builds = buildService.getBuildsForProject(tulipConfig.getGithubProperties().getAccount(), repo);
        return ModelMapper.toBuildsResponseModel(builds);
    }

    @GetMapping("/projects/{repo}/builds/{buildId}")
    public BuildResponseModel refreshWorkflowRuns(@PathVariable String repo, @PathVariable String buildId) {
        return ModelMapper.toBuildResponseModel(
                buildService.getBuild(tulipConfig.getGithubProperties().getAccount(), repo, buildId)
        );
    }

    @PostMapping(value = "/projects/{repo}/run", consumes = "application/json")
    public String startWorkflowRun(@RequestBody StartWorkflowRequest startWorkflowRequest) {
        // TODO: fix parameters
        return workflowRunsService.startWorkflowRun(tulipConfig.getGithubProperties().getAccount(), startWorkflowRequest.projectId(), startWorkflowRequest.workflowId(), startWorkflowRequest.branch()).toString();
    }

}