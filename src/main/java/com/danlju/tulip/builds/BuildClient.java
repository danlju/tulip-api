package com.danlju.tulip.builds;

import com.danlju.tulip.service.WorkflowRunsService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public interface BuildClient {
    WorkflowRunsService.WorkflowRunsResponse getAllBuilds(String owner, String repo, Instant lastUpdated);
    ResponseEntity<HttpStatusCode> startBuild(String owner, String repo, String workflowId, String branch);
    WorkflowRunsService.WorkflowRun getBuild(String owner, String repo, Integer buildId);
}
