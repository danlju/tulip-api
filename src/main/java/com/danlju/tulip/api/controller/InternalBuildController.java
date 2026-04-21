package com.danlju.tulip.api.controller;

import com.danlju.tulip.application.service.BuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InternalBuildController {

    private Logger logger = LoggerFactory.getLogger(InternalBuildController.class);

    @Autowired
    private BuildService buildService;

    @PostMapping(value = "/internal/builds/{buildId}/{status}", consumes = "application/json")
    public ResponseEntity<?> updateBuildStatus(@PathVariable String buildId, @PathVariable String status) {
        logger.info("Update build {} with status {}", buildId, status );
        buildService.updateStatusForBuild(Integer.parseInt(buildId), status);

        return ResponseEntity.ok()
                .body(Map.of(
                        "buildId", buildId,
                        "status", status
                ));
    }
}
