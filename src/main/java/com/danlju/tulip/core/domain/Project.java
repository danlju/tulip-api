package com.danlju.tulip.core.domain;

import java.time.Instant;
import java.util.UUID;

public class Project {

    private Integer id;
    private UUID publicId;
    private String name;
    private String cloneUrl;
    private Integer totalRuns;
    private Integer nextBuildNumber;
    private Instant mostRecentBuild;
    private String mostRecentBuildStatus; // TODO: enum
    private Instant lastSyncedAt;
    private String configPath; // TODO: maybe move to separate config object

    public Project(Integer id, UUID publicId, String name, String cloneUrl, Integer totalRuns, Integer nextBuildNumber, Instant mostRecentBuild, String mostRecentBuildStatus, Instant lastSyncedAt) {
        this.id = id;
        this.publicId = publicId;
        this.name = name;
        this.cloneUrl = cloneUrl;
        this.totalRuns = totalRuns;
        this.nextBuildNumber = nextBuildNumber;
        this.mostRecentBuild = mostRecentBuild;
        this.mostRecentBuildStatus = mostRecentBuildStatus;
        this.lastSyncedAt = lastSyncedAt;
    }

    public Project(UUID publicId, String name, String cloneUrl, Integer totalRuns, Integer nextBuildNumber, Instant mostRecentBuild, String mostRecentBuildStatus, Instant lastSyncedAt) {
        this.publicId = publicId;
        this.name = name;
        this.cloneUrl = cloneUrl;
        this.totalRuns = totalRuns;
        this.nextBuildNumber = nextBuildNumber;
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

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public Integer getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(Integer totalRuns) {
        this.totalRuns = totalRuns;
    }

    public Integer getNextBuildNumber() {
        return nextBuildNumber;
    }

    public void setNextBuildNumber(Integer nextBuildNumber) {
        this.nextBuildNumber = nextBuildNumber;
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

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }
}
