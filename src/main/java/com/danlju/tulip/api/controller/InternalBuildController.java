package com.danlju.tulip.api.controller;

import com.danlju.tulip.application.service.BuildService;
import com.danlju.tulip.core.domain.BuildStatus;
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

    private static final Logger logger = LoggerFactory.getLogger(InternalBuildController.class);

    @Autowired
    private BuildService buildService;

    @PostMapping("/internal/builds/{buildId}/{status}")
    public ResponseEntity<?> updateBuildStatus(
            @PathVariable Integer buildId,
            @PathVariable BuildStatus status,
            @org.springframework.web.bind.annotation.RequestBody(required = false) Map<String, Object> details) {
        logger.info("Update build {} with status {}", buildId, status );
        String failureReason = details == null || details.get("error") == null
                ? null : details.get("error").toString();
        BuildStatus actualStatus = buildService.updateStatusForBuild(buildId, status, failureReason);

        return ResponseEntity.ok()
                .body(Map.of(
                        "buildId", buildId,
                        "status", actualStatus.name()
                ));
    }
}
