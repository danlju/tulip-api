package com.danlju.tulip.infrastructure.dataaccess.entity;


import com.danlju.tulip.core.domain.Build;
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDbEntity project;
    private String startedByUser;
    @Column(unique = true)
    private String externalId;
    private Integer number;
    private String commit;
    private String commitMessage;
    private String branch;
    private String status;
    private Instant startedAt;
    private Instant updatedAt;

    public BuildDbEntity() {
    }

    public BuildDbEntity(Integer id, UUID publicId, ProjectDbEntity project, String startedByUser, String externalId, Integer number, String commit, String commitMessage, String branch, String status, Instant startedAt, Instant updatedAt) {
        this.id = id;
        this.publicId = publicId;
        this.project = project;
        this.startedByUser = startedByUser;
        this.externalId = externalId;
        this.number = number;
        this.commit = commit;
        this.commitMessage = commitMessage;
        this.branch = branch;
        this.status = status;
        this.startedAt = startedAt;
        this.updatedAt = updatedAt;
    }

    public BuildDbEntity(UUID publicId, ProjectDbEntity project, String startedByUser, String externalId, Integer number, String commit, String commitMessage, String branch, String status, Instant startedAt, Instant updatedAt) {
        this.publicId = publicId;
        this.project = project;
        this.startedByUser = startedByUser;
        this.externalId = externalId;
        this.externalId = externalId;
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

    public ProjectDbEntity getProject() {
        return project;
    }

    public void setProject(ProjectDbEntity project) {
        this.project = project;
    }

    public String getStartedByUser() {
        return startedByUser;
    }

    public void setStartedByUser(String startedByUser) {
        this.startedByUser = startedByUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setCommit(String commit) {
        this.commit = commit;
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

    public static Build toBuild(BuildDbEntity dbEntity) {
        if (dbEntity == null) {
            return null;
        }
        return new Build(
                dbEntity.id,
                dbEntity.publicId,
                dbEntity.externalId,
                dbEntity.project.getId(),
                dbEntity.startedByUser,
                dbEntity.number,
                dbEntity.commit,
                dbEntity.commitMessage,
                dbEntity.branch,
                dbEntity.status,
                dbEntity.startedAt,
                dbEntity.updatedAt
        );
    }
    public static BuildDbEntity toEntity(Build build) {

        if (build == null) {
            return null;
        }

        return new BuildDbEntity(
                build.getId(),
                build.getPublicId(),
                new ProjectDbEntity(build.getProjectId()),
                build.getStartedByUser(),
                build.getExternalId(),
                build.getNumber(),
                build.getCommit(),
                build.getCommitMessage(),
                build.getBranch(),
                build.getStatus(),
                build.getStartedAt(),
                build.getUpdatedAt()
        );
    }

    @Override
    public String toString() {
        return "BuildDbEntity{" +
                "id=" + id +
                ", publicId=" + publicId +
                ", project=" + project +
                ", externalId='" + externalId + '\'' +
                ", number=" + number +
                ", commit='" + commit + '\'' +
                ", branch='" + branch + '\'' +
                ", startedAt=" + startedAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
