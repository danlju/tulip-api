package com.danlju.tulip.api.controller.model;

public record StartBuildRequest(
        String projectId,
        String branch,
        String commitSha,
        String cloneUrl
) {
}
