package com.danlju.tulip.github;

import com.danlju.tulip.rest.ProjectController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Map;

@Component
public class GitHubClient {

    private static final Logger logger = LoggerFactory.getLogger(GitHubClient.class);

    private final RestTemplate restTemplate;
    private final String token;

    public GitHubClient(@Value("${GITHUB_TOKEN:}") String token) {
        this.token = token;
        this.restTemplate = new RestTemplate();
    }

    public ProjectController.WorkflowRunsResponse getWorkflowRuns(String owner, String repo) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/actions/runs";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ProjectController.WorkflowRunsResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, ProjectController.WorkflowRunsResponse.class);
        return response.getBody();
    }

    public ResponseEntity<HttpStatusCode> startWorkflowRun(String owner, String repo, String workflowId, String branch) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/actions/workflows/" + workflowId + "/dispatches";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(Map.of("ref", branch), headers);
        ResponseEntity<HttpStatusCode> response = restTemplate.exchange(url,
                HttpMethod.POST,
                entity,
                HttpStatusCode.class
                );
        return response; // TODO: how to handle response?
    }

    // TODO: create webhook for workflow status changes
    public String createWorkflowWebhook(String owner, String repo) {
        return "";
    }

}
