package com.danlju.tulip.rest.model;

public class ProjectModel {

    public String id;
    public String name;
    public String githubName;

    public ProjectModel(String id, String name, String githubName) {
        this.id = id;
        this.name = name;
        this.githubName = githubName;
    }
}
