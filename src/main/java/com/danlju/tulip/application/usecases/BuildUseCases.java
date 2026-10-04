package com.danlju.tulip.application.usecases;

import com.danlju.tulip.application.usecases.model.RequestBuildResult;
import com.danlju.tulip.core.domain.Build;
import com.danlju.tulip.core.domain.BuildStatus;

import java.util.List;
import java.util.UUID;

public interface BuildUseCases {
    List<Build> getBuildsForProjectByPublicId(UUID publicId);
    void syncBuilds(String repo);
    Build getBuildByPublicId(UUID publicId);
    RequestBuildResult requestBuild(String projectId, String branch, String commitSha);
    void updateStatusForBuild(Integer buildId, BuildStatus status);
}
