package com.danlju.tulip.repo;

import com.danlju.tulip.domain.Project;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class InMemoryProjectRepository implements ProjectRepository {

    private final Map<UUID, Project> projectMap;

    public InMemoryProjectRepository() {
        projectMap = new HashMap<>();

        Project project = new Project(UUID.randomUUID(), "kaado-srs", "kaado-srs");
        projectMap.put(project.getId(), project);
        project = new Project(UUID.randomUUID(), "kaado", "kaado");
        projectMap.put(project.getId(), project);
    }

    @Override
    public Project getById(UUID id) {
        return projectMap.get(id);
    }

    @Override
    public Project save(Project project) {
        return null;
    }

    @Override
    public List<Project> findAll() {
        return projectMap.values().stream().toList();
    }
}
