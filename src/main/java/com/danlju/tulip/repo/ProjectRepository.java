package com.danlju.tulip.repo;

import com.danlju.tulip.domain.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository {
    Project getById(UUID id);
    Project save(Project project);
    List<Project> findAll();
}
