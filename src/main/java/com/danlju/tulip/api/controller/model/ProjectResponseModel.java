package com.danlju.tulip.api.controller.model;

public class ProjectResponseModel {

    public String id;
    public String name;
    public String cloneUrl;
    public String description = "TODO"; // TODO
    public String lastBuild = "TODO";
    public String status = "success";
    public int buildsCount = 10;

    public ProjectResponseModel(String id, String name, String cloneUrl) {
        this.id = id;
        this.name = name;
        this.cloneUrl = cloneUrl;
    }
}
