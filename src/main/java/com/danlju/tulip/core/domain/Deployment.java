package com.danlju.tulip.core.domain;

import java.time.Instant;
import java.util.UUID;

public class Deployment {
    private Integer id;
    private UUID publicId;
    private Integer buildId;
    private String stackId;
    private Instant startedAt;
    private String result; // TODO: better result handling

    public Deployment(Integer id, UUID publicId, Integer buildId, String stackId, Instant startedAt, String result) {
        this.id = id;
        this.publicId = publicId;
        this.buildId = buildId;
        this.stackId = stackId;
        this.startedAt = startedAt;
        this.result = result;
    }

    public Deployment(UUID publicId, Integer buildId, String stackId, Instant startedAt, String result) {
        this.publicId = publicId;
        this.buildId = buildId;
        this.stackId = stackId;
        this.startedAt = startedAt;
        this.result = result;
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

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getStackId() {
        return stackId;
    }

    public void setStackId(String stackId) {
        this.stackId = stackId;
    }
}
