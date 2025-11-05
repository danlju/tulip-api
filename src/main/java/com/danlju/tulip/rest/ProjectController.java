package com.danlju.tulip.rest;

import com.danlju.tulip.domain.Project;
import com.danlju.tulip.repo.ProjectRepository;
import com.danlju.tulip.rest.model.ProjectModel;
import com.danlju.tulip.service.WorkflowRunsService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WorkflowRunsService workflowRunsService;

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectModel>> list() {
        List<Project> projects = projectRepository.findAll();
        var models = projects.stream().map(this::toModel).collect(Collectors.toList());
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    @GetMapping("/projects/{repo}")
    public WorkflowRunsResponse getWorkflowRuns(@PathVariable String repo) {
        // TODO: owner?
        return workflowRunsService.getWorkflowRuns("danlju", repo);
    }

    @GetMapping("/projects/{repo}/refresh")
    public WorkflowRunsResponse refreshWorkflowRuns(@PathVariable String repo) {
        // TODO: owner?
        return workflowRunsService.refreshWorkflowRuns("danlju", repo);
    }

    @PostMapping(value = "/projects/{repo}/run", consumes = "application/json")
    public String startWorkflowRun(@RequestBody StartWorkflowRequest startWorkflowRequest) {
        // TODO: use path variable?
        return workflowRunsService.startWorkflowRun(startWorkflowRequest.owner, startWorkflowRequest.projectId, startWorkflowRequest.workflowId, startWorkflowRequest.branch).toString();
    }

    private ProjectModel toModel(Project project) {
        return new ProjectModel(project.getId().toString(), project.getPublicId().toString(),  project.getName(), project.getGithubName());
    }

    @PostMapping(value = "/projects", consumes = "application/json")
    private String createProject(@RequestBody CreateProjectModel model) {
        logger.info("Request: {}", model);
        // TODO: validation (including duplicate names/ids)

        projectRepository.save(new Project(UUID.randomUUID(), model.name, model.githubName));

        return "ok";
    }

    private Project modelToProject(CreateProjectModel model) {
        return new Project(null, UUID.randomUUID(), model.name, model.githubName);
    }

    public record StartWorkflowRequest(
        String owner,
        String projectId,
        String workflowId,
        String branch
    ) {}

    public record CreateProjectModel(
            String owner,
            String name,
            String githubName
    ) {}

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
    ) {}
}