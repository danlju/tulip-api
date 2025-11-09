package com.danlju.tulip.core.domain;

import java.time.Instant;
import java.util.UUID;

public class Project {

    private Integer id;
    private UUID publicId;
    private String name;
    private String githubName;
    private Integer totalRuns;
    private Instant mostRecentBuild;
    private String mostRecentBuildStatus;
    private Instant lastSyncedAt;

    public Project(Integer id, UUID publicId, String name, String githubName, Integer totalRuns, Instant mostRecentBuild, String mostRecentBuildStatus, Instant lastSyncedAt) {
        this.id = id;
        this.publicId = publicId;
        this.name = name;
        this.githubName = githubName;
        this.totalRuns = totalRuns;
        this.mostRecentBuild = mostRecentBuild;
        this.mostRecentBuildStatus = mostRecentBuildStatus;
        this.lastSyncedAt = lastSyncedAt;
    }

    public Project(UUID publicId, String name, String githubName, Integer totalRuns, Instant mostRecentBuild, String mostRecentBuildStatus, Instant lastSyncedAt) {
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
}
