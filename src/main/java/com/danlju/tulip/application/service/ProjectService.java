package com.danlju.tulip.application.service;

import com.danlju.tulip.application.repository.ProjectRepository;
import com.danlju.tulip.application.usecases.ProjectUseCases;
import com.danlju.tulip.core.domain.Build;
import com.danlju.tulip.core.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService implements ProjectUseCases {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project createProject(UUID publicId, String name, String cloneUrl, String sourceProvider) {
        return projectRepository.save(new Project(UUID.randomUUID(), name, cloneUrl, 10, Instant.ofEpochMilli(0), "unknown", Instant.ofEpochMilli(0))); // TODO: Instant
    }

    @Override
    public List<Build> getBuildsForProject(String owner, String repo) {
        return List.of();
    }

    @Override
    public void syncBuilds(String owner, String repo) {

    }
}
