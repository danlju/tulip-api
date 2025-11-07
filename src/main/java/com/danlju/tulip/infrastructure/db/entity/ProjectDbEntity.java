package com.danlju.tulip.infrastructure.db.entity;

import com.danlju.tulip.domain.Project;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class ProjectDbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UUID publicId;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String githubName;
    private Integer totalRuns;
    private Instant mostRecentBuild;
    private String mostRecentBuildStatus;
    private Instant lastSyncedAt;

    public ProjectDbEntity() {
    }

    public ProjectDbEntity(Integer id) {
        this.id = id;
    }

    public ProjectDbEntity(Integer id, UUID publicId, String name, String githubName, Integer totalRuns, Instant mostRecentBuild, String mostRecentBuildStatus, Instant lastSyncedAt) {
        this.id = id;
        this.publicId = publicId;
        this.name = name;
        this.githubName = githubName;
        this.totalRuns = totalRuns;
        this.mostRecentBuild = mostRecentBuild;
        this.mostRecentBuildStatus = mostRecentBuildStatus;
        this.lastSyncedAt = lastSyncedAt;
    }

    public ProjectDbEntity(UUID publicId, String name, String githubName, Integer totalRuns, Instant mostRecentBuild, String mostRecentBuildStatus, Instant lastSyncedAt) {
        this.publicId = publicId;
        this.name = name;
        this.githubName = githubName;
        this.totalRuns = totalRuns;
        this.mostRecentBuild = mostRecentBuild;
        this.mostRecentBuildStatus = mostRecentBuildStatus;
        this.lastSyncedAt = lastSyncedAt;
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

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithubName() {
        return githubName;
    }

    public void setGithubName(String githubName) {
        this.githubName = githubName;
    }

    public Integer getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(Integer totalRuns) {
        this.totalRuns = totalRuns;
    }

    public Instant getMostRecentBuild() {
        return mostRecentBuild;
    }

    public void setMostRecentBuild(Instant mostRecentBuild) {
        this.mostRecentBuild = mostRecentBuild;
    }

    public String getMostRecentBuildStatus() {
        return mostRecentBuildStatus;
    }

    public void setMostRecentBuildStatus(String mostRecentBuildStatus) {
        this.mostRecentBuildStatus = mostRecentBuildStatus;
    }

    public Instant getLastSyncedAt() {
        return lastSyncedAt;
    }

    public void setLastSyncedAt(Instant lastSyncedAt) {
        this.lastSyncedAt = lastSyncedAt;
    }

    public static ProjectDbEntity fromProject(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectDbEntity(
                project.getId(),
                project.getPublicId(),
                project.getName(),
                project.getGithubName(),
                project.getTotalRuns(),
                project.getMostRecentBuild(),
                project.getMostRecentBuildStatus(),
                project.getLastSyncedAt());
    }

    public static Project toProject(ProjectDbEntity dbEntity) {
        if (dbEntity == null)  {
            return null;
        }
        return new Project(dbEntity.id, dbEntity.publicId,
                dbEntity.name,
                dbEntity.githubName,
                dbEntity.totalRuns,
                dbEntity.mostRecentBuild,
                dbEntity.mostRecentBuildStatus,
                dbEntity.lastSyncedAt);
    }
}
