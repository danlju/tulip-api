package com.danlju.tulip.application.usecases;

import com.danlju.tulip.core.domain.Build;
import com.danlju.tulip.application.service.BuildModel;

import java.util.List;

public interface BuildUseCases {
    List<Build> getBuildsForProject(String owner, String repo);
    void syncBuilds(String owner, String repo);
    Build saveBuild(BuildModel buildModel);
    Build getBuild(String owner, String repo, String buildId);
}
