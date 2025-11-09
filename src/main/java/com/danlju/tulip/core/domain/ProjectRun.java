package com.danlju.tulip.core.domain;

import java.time.Instant;
import java.util.UUID;

public class ProjectRun {

    public static ProjectRun createProjectRun(UUID publicId, Project project, String externalId, String branch, String commit, String startedBy, Instant startedAt, Instant finishedAt, RunStatus status) {
        return new ProjectRun(publicId, project, externalId, branch, commit, startedBy, startedAt, finishedAt, status);
    }

    enum RunStatus {
        PENDING,
        IN_PROGRESS,
        SUCCESSFUL,
        FAILED
    }

    private ProjectRun(UUID publicId, Project project, String externalId, String branch, String commit, String startedBy, Instant startedAt, Instant finishedAt, RunStatus status) {
        this.publicId = publicId;
        this.project = project;
        this.externalId = externalId;
        this.branch = branch;
        this.commit = commit;
        this.startedBy = startedBy;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.status = status;
    }

    private Integer id;
    private UUID publicId;
    private Project project;
    private String externalId;
    private String branch;
    private String commit;
    private String startedBy;
    private Instant startedAt;
    private Instant finishedAt;
    private RunStatus status;

}
