package com.danlju.tulip.service;

import com.danlju.tulip.github.GitHubClient;
import com.danlju.tulip.rest.ProjectController;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowRunsService {

    @Autowired
    private GitHubClient gitHubClient;

    @Cacheable(value = "workflowRuns", key = "#repo")
    public WorkflowRunsResponse getWorkflowRuns(String owner, String repo) {
        return gitHubClient.getWorkflowRuns(owner, repo);
    }

    @CachePut(value = "workflowRuns", key = "#repo")
    public WorkflowRunsResponse refreshWorkflowRuns(String owner, String repo) {
        return gitHubClient.getWorkflowRuns(owner, repo);
    }

    public ResponseEntity<HttpStatusCode> startWorkflowRun(String owner, String repo, String workflowId, String branch) {
        return gitHubClient.startWorkflowRun(
                owner,
                repo,
                workflowId,
                branch
        );
    }

    public record WorkflowRunsResponse(
            @JsonProperty("total_count") int totalCount,
            @JsonProperty("workflow_runs") List<WorkflowRun> workflowRuns
    ) {}

    public record WorkflowRun(
            long id,
            String name,
            String event,
            String status,
            String conclusion,
            @JsonProperty("workflow_id") long workflowId,
            @JsonProperty("created_at") String createdAt,
            @JsonProperty("updated_at") String updatedAt,
            String url,
            @JsonProperty("html_url") String htmlUrl,
            @JsonProperty("head_sha") String commitHash,
            @JsonProperty("head_branch") String headBranch,
            @JsonProperty("display_title") String displayTitle,
            @JsonProperty("run_number") String runNumber,
            ProjectController.Repository repository
    ) {}
}

