package com.danlju.tulip.api.controller;

import com.danlju.tulip.api.controller.model.*;
import com.danlju.tulip.application.usecases.ProjectUseCases;
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
    private ProjectUseCases projectUseCases;

    @GetMapping("/projects")
    public List<ProjectModel> list() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(ModelMapper::toModel).collect(Collectors.toList());
    }

    @PostMapping(value = "/projects", consumes = "application/json")
    private ResponseEntity<String> createProject(@RequestBody CreateProjectModel model) {
        var project = projectUseCases.createProject(UUID.randomUUID(), model.name(), model.cloneUrl(), model.sourceProvider());
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/projects/{publicId}")
    public List<BuildResponseModel> getWorkflowRuns(@PathVariable String publicId) {
        var builds = buildService.getBuildsForProjectByPublicId(UUID.fromString(publicId));
        return ModelMapper.toBuildsResponseModel(builds);
    }
}