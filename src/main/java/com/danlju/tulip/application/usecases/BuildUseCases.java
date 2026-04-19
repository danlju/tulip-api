package com.danlju.tulip.application.usecases;

import com.danlju.tulip.core.domain.Build;

import java.util.List;
import java.util.UUID;

public interface BuildUseCases {
    List<Build> getBuildsForProjectByPublicId(UUID publicId);
    void syncBuilds(String owner, String repo);
    Build getBuild(String owner, String repo, String buildId);
    RequestBuildResult requestBuild(String owner, String repo, String branch, String commit, String user);
    void updateStatusForBuild(String buildId, String status);
}
