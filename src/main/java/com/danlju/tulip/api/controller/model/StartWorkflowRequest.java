package com.danlju.tulip.api.controller.model;

public record StartWorkflowRequest(
        String projectId,
        String workflowId,
        String branch
) {
}
