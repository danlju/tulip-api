package com.danlju.tulip.application.usecases;

import com.danlju.tulip.core.domain.Build;
import com.danlju.tulip.core.domain.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectUseCases {
    Project createProject(UUID publicId, String name, String cloneUrl, String sourceProvider);
    List<Build> getBuildsForProject(String owner, String repo);
    void syncBuilds(String owner, String repo);
}
