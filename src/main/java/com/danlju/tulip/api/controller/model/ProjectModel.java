package com.danlju.tulip.api.controller.model;

public class ProjectModel {

    public String id;
    public String publicId;
    public String name;
    public String githubName;
    public String description = "A project";
    public String lastBuild = "10 minutes ago";
    public String status = "success";
    public int buildsCount = 10;

    public ProjectModel(String id, String publicId, String name, String githubName) {
        this.id = id;
        this.publicId = publicId;
        this.name = name;
        this.githubName = githubName;
    }
}
