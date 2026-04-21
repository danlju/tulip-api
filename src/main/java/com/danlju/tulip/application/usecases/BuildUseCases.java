package com.danlju.tulip.application.usecases;

import com.danlju.tulip.application.usecases.model.RequestBuildResult;
import com.danlju.tulip.core.domain.Build;

import java.util.List;
import java.util.UUID;

public interface BuildUseCases {
    List<Build> getBuildsForProjectByPublicId(UUID publicId);
    void syncBuilds(String repo);
    Build getBuild(String repo, String buildId);
    RequestBuildResult requestBuild(String repo, String branch, String commit, String user);
    void updateStatusForBuild(Integer buildId, String status);
}
