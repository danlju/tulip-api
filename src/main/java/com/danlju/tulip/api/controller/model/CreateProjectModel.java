package com.danlju.tulip.api.controller.model;

public record CreateProjectModel(
        String owner,
        String name,
        String cloneUrl,
        String sourceProvider
) {
}

