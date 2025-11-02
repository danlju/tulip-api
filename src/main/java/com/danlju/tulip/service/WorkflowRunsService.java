package com.danlju.tulip.service;

import com.danlju.tulip.github.GitHubClient;
import com.danlju.tulip.rest.ProjectController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WorkflowRunsService {

    @Autowired
    private GitHubClient gitHubClient;

    @Cacheable(value = "workflowRuns", key = "#repo")
    public ProjectController.WorkflowRunsResponse getWorkflowRuns(String owner, String repo) {
        return gitHubClient.getWorkflowRuns(owner, repo);
    }

    @CachePut(value = "workflowRuns", key = "#repo")
    public ProjectController.WorkflowRunsResponse refreshWorkflowRuns(String owner, String repo) {
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
}

