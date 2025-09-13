package com.danlju.tulip.rest;

import com.danlju.tulip.domain.Project;
import com.danlju.tulip.github.GitHubClient;
import com.danlju.tulip.repo.ProjectRepository;
import com.danlju.tulip.rest.model.ProjectModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GitHubClient gitHubClient;

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectModel>> list() {
        List<ProjectModel> models = projectRepository.findAll().stream().map(this::toModel).toList();
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    @GetMapping("/projects/{repo}")
    public Mono<WorkflowRunsResponse> getWorkflowRuns(@PathVariable String repo) {
        return gitHubClient.getWorkflowRuns("danlju", repo);
    }

    private ProjectModel toModel(Project project) {
        return new ProjectModel(project.getId().toString(), project.getName(), project.getGithubName());
    }
    // TODO:
    // createProject
    // deleteProject
    // caching


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
            Repository repository
    ) {}

    public record Repository(
       long id,
       String node_id,
       String name,
       String full_name
    ){}
}


