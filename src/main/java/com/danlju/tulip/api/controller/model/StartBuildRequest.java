package com.danlju.tulip.api.controller.model;

public record StartBuildRequest(
        String owner,
        String projectId,
        String branch,
        String commitSha,
        String cloneUrl
) {
}
