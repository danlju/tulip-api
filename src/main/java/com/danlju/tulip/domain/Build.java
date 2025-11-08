package com.danlju.tulip.domain;

import java.time.Instant;
import java.util.UUID;

public class Build {

    private Integer id;
    private UUID publicId;
    private String externalId;
    private Integer projectId;
    private String startedByUser;
    private Integer number;
    private String commit;
    private String commitMessage;
    private String branch;
    private String status;
    private Instant startedAt;
    private Instant updatedAt;

    public Build(Integer id, UUID publicId, String externalId, Integer projectId, String startedByUser, Integer number, String commit, String commitMessage, String branch, String status, Instant startedAt, Instant updatedAt) {
        this.id = id;
        this.publicId = publicId;
        this.externalId = externalId;
        this.projectId = projectId;
        this.startedByUser = startedByUser;
        this.number = number;
        this.commit = commit;
        this.commitMessage = commitMessage;
        this.branch = branch;
        this.status = status;
        this.startedAt = startedAt;
        this.updatedAt = updatedAt;
    }

    public Build(UUID publicId, String externalId, Integer projectId, String startedByUser, Integer number, String commit, String commitMessage, String branch, String status, Instant startedAt, Instant updatedAt) {
        this.publicId = publicId;
        this.externalId = externalId;
        this.projectId = projectId;
        this.startedByUser = startedByUser;
        this.number = number;
        this.commit = commit;
        this.commitMessage = commitMessage;
        this.branch = branch;
        this.status = status;
        this.startedAt = startedAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getStartedByUser() {
        return startedByUser;
    }

    public void setStartedByUser(String startedByUser) {
        this.startedByUser = startedByUser;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCommit() {
        return commit;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Build{" +
                "id=" + id +
                ", publicId=" + publicId +
                ", externalId='" + externalId + '\'' +
                ", projectId=" + projectId +
                ", number=" + number +
                ", commit='" + commit + '\'' +
                ", branch='" + branch + '\'' +
                ", status='" + status + '\'' +
                ", startedAt=" + startedAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
