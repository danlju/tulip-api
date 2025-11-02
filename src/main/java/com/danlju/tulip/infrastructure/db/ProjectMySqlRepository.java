package com.danlju.tulip.infrastructure.db;

import com.danlju.tulip.domain.Project;
import com.danlju.tulip.repo.ProjectRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ProjectMySqlRepository implements ProjectRepository {

    private final ProjectCrudRepository projectCrudRepository;

    public ProjectMySqlRepository(ProjectCrudRepository projectCrudRepository) {
        this.projectCrudRepository = projectCrudRepository;
    }

    @Override
    public Project getByPublicId(UUID id) {
        return ProjectDbEntity.toProject(
                projectCrudRepository.findByPublicId(id)
        );
    }

    @Override
    public Project save(Project project) {
        return ProjectDbEntity.toProject(
                projectCrudRepository.save(
                        ProjectDbEntity.fromProject(project)
                )
        );
    }

    public Project findByCategory(UUID publicId) {
        return ProjectDbEntity.toProject(
                projectCrudRepository.findByPublicId(publicId)
        );
    }

    @Override
    public List<Project> findAll() {
        return List.of();
    }
}
