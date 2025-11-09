package com.danlju.tulip.api.controller.model;

public record CreateProjectModel(
        String owner,
        String name,
        String githubName
) {
}

