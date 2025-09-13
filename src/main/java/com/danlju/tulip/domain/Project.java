package com.danlju.tulip.domain;

import java.util.UUID;

public class Project {
    private UUID id;
    private String name;
    private String githubName;

    public Project(UUID id, String name, String githubName) {
        this.id = id;
        this.name = name;
        this.githubName = githubName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
