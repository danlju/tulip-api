package com.danlju.tulip.api.controller.model;

import com.danlju.tulip.core.domain.Build;
import com.danlju.tulip.core.domain.Project;
import com.danlju.tulip.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ModelMapper {

    public static BuildResponseModel toBuildResponseModel(Build build) {
        return new BuildResponseModel(
                0L, "#" + build.getNumber().toString() + " [" + build.getCommitMessage() + "]", // TODO: fix
                build.getStatus().toString(),
                "STARTED_BY_USER REMOVE",
                build.getCommitSha(),
                build.getBranch(),
                build.getNumber().toString(),
                "TODO: displayTitle",
                Utils.calculateDuration(build.getStartedAt(), build.getUpdatedAt()),
                "");
    }

    public static ProjectModel toModel(Project project) {
        return new ProjectModel(project.getId().toString(), project.getPublicId().toString(), project.getName(), project.getCloneUrl());
    }

    public static List<BuildResponseModel> toBuildsResponseModel(List<Build> workflowRuns) {
        List<BuildResponseModel> builds = new ArrayList<>();
        for (Build build : workflowRuns) {
            builds.add(
                toBuildResponseModel(build)
            );
        }
        return builds;
    }

}
