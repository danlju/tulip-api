package com.danlju.tulip.core.domain.exceptions;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(String projectId) {
        super("Project not found: " + projectId);
    }
}
