package com.danlju.tulip.application.service;

import com.danlju.tulip.core.domain.Build;
import com.danlju.tulip.core.domain.BuildStatus;
import com.danlju.tulip.core.domain.Project;
import com.danlju.tulip.core.domain.SourceProvider;
import com.danlju.tulip.github.GitHubClient;
import com.danlju.tulip.application.repository.BuildRepository;
import com.danlju.tulip.application.repository.ProjectRepository;
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
        when(projectRepository.("test-repo")).thenReturn(
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

        // Existing and up to date
        when(buildRepository.findById(10)).thenReturn(
            new Build(10, UUID.randomUUID(), 1, 1001, BuildStatus.REQUESTED, SourceProvider.GENERIC, "commit", "commitMessage", "branch", "success", Instant.parse("2025-11-07T05:08:00Z"), Instant.parse("2025-11-07T05:07:00Z"))
        );

        buildService.updateStatusForBuild();

        verify(buildRepository, times(2)).save(any());
    }

    private WorkflowRunsService.WorkflowRunsResponse mockResponse() {
        List<WorkflowRunsService.WorkflowRun> runs = new ArrayList<>();

        runs.add(
                new WorkflowRunsService.WorkflowRun(
                        2001L,
                        "Build",
                        "event",
                        "status",
                        "conclusion",
                        1001L,
                        "2025-11-07T05:00:00Z",
                        "2025-11-07T05:01:00Z",
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
                        ),
                        new WorkflowRunsService.Actor("theUser")
                )
        );

        runs.add(
                new WorkflowRunsService.WorkflowRun(
                        2002L,
                        "Build",
                        "event",
                        "success",
                        "conclusion",
                        1001L,
                        "2025-11-07T05:00:00Z",
                        "2025-11-07T05:10:00Z",
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
                        ),
                        new WorkflowRunsService.Actor("theUser")
                )
        );

        runs.add(
                new WorkflowRunsService.WorkflowRun(
                        2003L,
                        "Build",
                        "event",
                        "status",
                        "conclusion",
                        1001L,
                        "2025-11-07T05:10:00Z",
                        "2025-11-07T05:15:00Z",
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
                        ),
                        new WorkflowRunsService.Actor("theUser")
                )
        );


        return new WorkflowRunsService.WorkflowRunsResponse(
                10, runs
        );
    }
}