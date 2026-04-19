package com.danlju.tulip.infrastructure.dataaccess.entity;


import com.danlju.tulip.core.domain.BuildStatus;
import com.danlju.tulip.core.domain.SourceProvider;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "builds")
public class BuildDbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UUID publicId;
    @Column(unique = true)
    private Integer number;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // TODO: lazy
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDbEntity project;
    @Enumerated(EnumType.STRING)
    private BuildStatus status;
    @Enumerated(EnumType.STRING)
    private SourceProvider sourceProvider;
    private String commitSha;
    private String commitMessage;
    private String branch;
    private Instant startedAt;
    private Instant updatedAt;

    public BuildDbEntity() {
    }

    public BuildDbEntity(Integer id, UUID publicId, Integer number, ProjectDbEntity project, BuildStatus status, String branch, String commitSha, String commitMessage, Instant startedAt, Instant updatedAt) {
        this.id = id;
        this.publicId = publicId;
        this.number = number;
        this.project = project;
        this.status = status;
        this.commitSha = commitSha;
        this.commitMessage = commitMessage;
        this.branch = branch;
        this.startedAt = startedAt;
        this.updatedAt = updatedAt;
    }

    public BuildDbEntity(UUID publicId, ProjectDbEntity project, Integer number, BuildStatus status, String branch, String commitSha, String commitMessage, Instant startedAt, Instant updatedAt) {
        this.publicId = publicId;
        this.project = project;
        this.number = number;
        this.status = status;
        this.commitSha = commitSha;
        this.commitMessage = commitMessage;
        this.branch = branch;
        this.startedAt = startedAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProjectDbEntity getProject() {
        return project;
    }

    public void setProject(ProjectDbEntity project) {
        this.project = project;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BuildStatus getStatus() {
        return status;
    }

    public void setStatus(BuildStatus status) {
        this.status = status;
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
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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
        return "BuildDbEntity{" +
                "id=" + id +
                ", publicId=" + publicId +
                ", project=" + project +
                ", number=" + number +
                ", commit='" + commitSha + '\'' +
                ", branch='" + branch + '\'' +
                ", startedAt=" + startedAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
