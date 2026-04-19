package com.danlju.tulip.api.controller;

import com.danlju.tulip.api.controller.model.StartBuildRequest;
import com.danlju.tulip.application.repository.ProjectRepository;
import com.danlju.tulip.application.usecases.BuildUseCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;


/**
 * To replace the workflow endpoints
 */
@RestController
public class BuildController {

    private static final Logger logger = LoggerFactory.getLogger(BuildController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BuildUseCases buildService;

    @PostMapping(value = "/projects/{repo}/build", consumes = "application/json")
    public ResponseEntity<?> requestBuild(@RequestBody StartBuildRequest startBuildRequest) {
        var build = buildService.requestBuild(startBuildRequest.owner(), startBuildRequest.projectId(),
                startBuildRequest.branch(), startBuildRequest.commitSha(), startBuildRequest.cloneUrl());

        return ResponseEntity
                .created(URI.create("/builds/" + build.id()))
                .body(Map.of(
                        "buildId", build.id()
                ));
    }

    @PostMapping(value = "/builds/{buildId}/{status}", consumes = "application/json")
    public String updateBuildStatus(@PathVariable String buildId, @PathVariable String status) {
        logger.info("Update build {} with status {}", buildId, status );

        buildService.updateStatusForBuild(buildId, status);

        return "OK";
    }
}
