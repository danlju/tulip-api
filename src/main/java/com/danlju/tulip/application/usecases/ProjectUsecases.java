package com.danlju.tulip.application.usecases;

import com.danlju.tulip.core.domain.Build;

import java.util.List;

public interface ProjectUsecases {
    List<Build> getBuildsForProject(String owner, String repo);
    void syncBuilds(String owner, String repo);
}
