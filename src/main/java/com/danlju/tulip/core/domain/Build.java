package com.danlju.tulip.core.domain;

import com.danlju.tulip.core.domain.exceptions.IllegalBuildStateTransitionException;

import java.time.Instant;
import java.util.UUID;

public class Build {

    private Integer id;
    private UUID publicId;
    private Integer number;
    private Integer projectId;
    private BuildStatus status;
    private String cloneUrl;
    private String branch;
    private String commitSha;
    private String commitMessage;
    private Instant startedAt;
    private Instant updatedAt;

    public Build(UUID publicId, int buildNumber, int projectId, BuildStatus status, String branch, String commitSha) {
        this.publicId = publicId;
        this.number = buildNumber;
        this.projectId = projectId;
        this.status = status;
        this.branch = branch;
        this.commitSha = commitSha;
        this.startedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Build(Integer id, UUID publicId, Integer number, Integer projectId, BuildStatus status, String cloneUrl, String branch, String commitSha, String commitMessage, Instant startedAt, Instant updatedAt) {
        this.id = id;
        this.publicId = publicId;
        this.number = number;
        this.projectId = projectId;
        this.status = status;
        this.cloneUrl = cloneUrl;
        this.branch = branch;
        this.commitSha = commitSha;
        this.commitMessage = commitMessage;
        this.startedAt = startedAt;
        this.updatedAt = updatedAt;
    }

    public Build(int id, UUID publicId, int number, int projectId, BuildStatus status) {
        this.id = id;
        this.publicId = publicId;
        this.number = number;
        this.projectId = projectId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public BuildStatus getStatus() {
        return status;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCommitSha() {
        return commitSha;
    }

    public void setCommitSha(String commitSha) {
        this.commitSha = commitSha;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Build create(UUID publicId, int buildNumber, int projectId, String branch, String commitSha) {
        return new Build(
                publicId,
                buildNumber,
                projectId,
                BuildStatus.REQUESTED,
                branch,
                commitSha
        );
    }

    public static Build load(int id, UUID publicId, int number, int projectId, BuildStatus status, String cloneUrl, String branch, String commitSha, String commitMessage, Instant startedAt, Instant updatedAt) {
        return new Build(id, publicId, number, projectId, status, cloneUrl, branch, commitSha, commitMessage, startedAt, updatedAt);
    }

    public void transitionTo(BuildStatus newStatus) throws IllegalBuildStateTransitionException {
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalBuildStateTransitionException(status, newStatus);
        }
        this.status = newStatus;
    }
}
