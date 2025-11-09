package com.danlju.tulip.application.repository;

import com.danlju.tulip.core.domain.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository {
    Project getByPublicId(UUID id);
    Project findByGithubName(String githubName);
    Project save(Project project);
    List<Project> findAll();
}
