package com.danlju.tulip.github;

import com.danlju.tulip.rest.ProjectController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(@Value("${GITHUB_TOKEN}") String token) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("Accept", "application/vnd.github+json")
                .build();
    }

    public Mono<ProjectController.WorkflowRunsResponse> getWorkflowRuns(String owner, String repo) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}/actions/runs", owner, repo)
                .retrieve()
                .bodyToMono(ProjectController.WorkflowRunsResponse.class);
    }
}
