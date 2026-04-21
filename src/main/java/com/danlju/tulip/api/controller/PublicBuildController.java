package com.danlju.tulip.api.controller;

import com.danlju.tulip.api.controller.model.BuildResponseModel;
import com.danlju.tulip.api.controller.model.ModelMapper;
import com.danlju.tulip.api.controller.model.StartBuildRequest;
import com.danlju.tulip.application.usecases.BuildUseCases;
import com.danlju.tulip.core.domain.Build;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(PublicBuildController.BASE_PATH)
public class PublicBuildController {

    public static final String BASE_PATH = "/api/v1/builds";

    private static final Logger logger = LoggerFactory.getLogger(PublicBuildController.class);

    @Autowired
    private BuildUseCases buildService;

    @PostMapping(value = "/request", consumes = "application/json")
    public ResponseEntity<?> requestBuild(@RequestBody StartBuildRequest startBuildRequest) {
        var build = buildService.requestBuild(startBuildRequest.projectId(),
                startBuildRequest.branch(), startBuildRequest.commitSha(), startBuildRequest.cloneUrl());

        return ResponseEntity
                .created(URI.create(BASE_PATH + "/" + build.publicId()))
                .body(Map.of(
                        "buildId", build.publicId()
                ));
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<List<BuildResponseModel>> listBuildsForProject(@PathVariable UUID publicId) {
        List<Build> builds = buildService.getBuildsForProjectByPublicId(publicId);

        return ResponseEntity.ok().body(
                builds.stream().map(ModelMapper::toBuildResponseModel).toList()
        );
    }
}
