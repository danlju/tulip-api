package com.danlju.tulip.api.controller;

import com.danlju.tulip.application.service.BuildService;
import com.danlju.tulip.core.domain.BuildStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
public class InternalBuildController {

    private static final Logger logger = LoggerFactory.getLogger(InternalBuildController.class);

    @Autowired
    private BuildService buildService;

    @Value("${internal.worker-token:}")
    private String workerToken;

    @PostMapping("/internal/builds/{buildId}/{status}")
    public ResponseEntity<?> updateBuildStatus(
            @PathVariable Integer buildId,
            @PathVariable BuildStatus status,
            @RequestHeader(name = "X-Tulip-Worker-Token", required = false) String suppliedToken,
            @org.springframework.web.bind.annotation.RequestBody(required = false) Map<String, Object> details) {
        if (workerToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "WORKER_AUTH_NOT_CONFIGURED"));
        }
        if (suppliedToken == null || !MessageDigest.isEqual(
                workerToken.getBytes(StandardCharsets.UTF_8),
                suppliedToken.getBytes(StandardCharsets.UTF_8))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "UNAUTHORIZED"));
        }
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
