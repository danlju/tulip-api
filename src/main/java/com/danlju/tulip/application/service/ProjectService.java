package com.danlju.tulip.application.service;

import com.danlju.tulip.application.repository.ProjectRepository;
import com.danlju.tulip.application.usecases.ProjectUseCases;
import com.danlju.tulip.core.domain.Build;
import com.danlju.tulip.core.domain.Project;
import com.danlju.tulip.core.domain.SourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class ProjectService implements ProjectUseCases {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project createProject(UUID publicId, String name, String cloneUrl, String sourceProvider) {
        SourceProvider provider = sourceProvider == null || sourceProvider.isBlank()
                ? SourceProvider.GENERIC
                : SourceProvider.valueOf(sourceProvider.trim().toUpperCase(Locale.ROOT));
        return projectRepository.save(new Project(publicId, name, cloneUrl, provider, 0, 1, Instant.EPOCH, "unknown", Instant.EPOCH));
    }

    @Override
    public List<Build> getBuildsForProject(String owner, String repo) {
        return List.of();
    }

    @Override
    public void syncBuilds(String owner, String repo) {

    }
}
