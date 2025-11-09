package com.danlju.tulip.infrastructure.dataaccess;

import com.danlju.tulip.infrastructure.dataaccess.entity.ProjectDbEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProjectCrudRepository extends CrudRepository<ProjectDbEntity, Integer> {
    ProjectDbEntity findByPublicId(UUID publicId);
    ProjectDbEntity getByGithubName(String githubName);
}
