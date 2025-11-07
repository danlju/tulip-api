package com.danlju.tulip.service;

import com.danlju.tulip.domain.Build;
import com.danlju.tulip.domain.Project;
import com.danlju.tulip.github.GitHubClient;
import com.danlju.tulip.repo.BuildRepository;
import com.danlju.tulip.repo.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildServiceTest {


    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private BuildRepository buildRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private BuildService buildService;

    @Test
    public void should_sync_builds() {
        when(gitHubClient.getWorkflowRuns(eq("danlju"), eq("test-repo"), any(Instant.class))).thenReturn(
            mockResponse()
        );
        when(projectRepository.findMostRecentSync("test-repo")).thenReturn(
                new Project(
                        1001,
                        UUID.randomUUID(),
                        "A project",
                        "a-project",
                        10,
                        Instant.now(),
                        "success",
                        Instant.parse("2025-11-07T05:08:00Z")
                )
        );
        when(buildRepository.findByExternalId(eq("2001"))).thenReturn(
            new Build(10, UUID.randomUUID(), "2001", 1001, 1, "commit", "branch", "success", Instant.parse("2025-11-07T05:08:00Z"), Instant.parse("2025-11-07T05:07:00Z"))
        );
        when(buildRepository.findByExternalId(eq("2002"))).thenReturn(
            new Build(11, UUID.randomUUID(), "2002", 1001, 1, "commit", "branch", "in progress", Instant.parse("2025-11-07T05:08:00Z"), Instant.parse("2025-11-07T05:09:00Z"))
        );
        when(buildRepository.findByExternalId(eq("2003"))).thenReturn(
            new Build(12, UUID.randomUUID(), "2003", 1001, 1, "commit", "branch", "pending", Instant.parse("2025-11-07T05:08:00Z"), Instant.parse("2025-11-07T05:08:00Z"))
        );

        buildService.syncBuilds("danlju", "test-repo");

        verify(buildRepository, times(1)).save(any());
    }

    private WorkflowRunsService.WorkflowRunsResponse mockResponse() {
        List<WorkflowRunsService.WorkflowRun> runs = new ArrayList<>();

        runs.add(
                new WorkflowRunsService.WorkflowRun(
                        10L,
                        "Build",
                        "event",
                        "status",
                        "conclusion",
                        2001L,
                        "2025-11-07T05:00:00",
                        "2025-11-07T05:01:00",
                        "url",
                        "htmlUrl",
                        "commit",
                        "branch",
                        "display_title",
                        "10",
                        new WorkflowRunsService.Repository(
                                1L,
                                "rnode_id",
                                "rname",
                                "rfull_name"
                        )
                )
        );

        runs.add(
                new WorkflowRunsService.WorkflowRun(
                        11L,
                        "Build",
                        "event",
                        "success",
                        "conclusion",
                        2002L,
                        "2025-11-07T05:00:00",
                        "2025-11-07T05:10:00",
                        "url",
                        "htmlUrl",
                        "commit",
                        "branch",
                        "display_title",
                        "10",
                        new WorkflowRunsService.Repository(
                                1L,
                                "rnode_id",
                                "rname",
                                "rfull_name"
                        )
                )
        );

        runs.add(
                new WorkflowRunsService.WorkflowRun(
                        12L,
                        "Build",
                        "event",
                        "status",
                        "conclusion",
                        2003L,
                        "2025-11-07T05:10:00",
                        "2025-11-07T05:15:00",
                        "url",
                        "htmlUrl",
                        "commit",
                        "branch",
                        "display_title",
                        "10",
                        new WorkflowRunsService.Repository(
                                1L,
                                "rnode_id",
                                "rname",
                                "rfull_name"
                        )
                )
        );


        return new WorkflowRunsService.WorkflowRunsResponse(
                10, runs
        );
    }
}