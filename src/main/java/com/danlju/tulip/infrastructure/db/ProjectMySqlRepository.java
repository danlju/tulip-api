package com.danlju.tulip.infrastructure.db;

import com.danlju.tulip.domain.Project;
import com.danlju.tulip.infrastructure.db.entity.ProjectDbEntity;
import com.danlju.tulip.repo.ProjectRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public Project findByGithubName(String githubName) {
        return ProjectDbEntity.toProject(
                projectCrudRepository.getByGithubName(githubName)
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
        var projects = new ArrayList<ProjectDbEntity>();
        projectCrudRepository.findAll().iterator().forEachRemaining(projects::add);
        return projects.stream().map(ProjectDbEntity::toProject).toList();
    }
}
