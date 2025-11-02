package com.danlju.tulip.infrastructure.db;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProjectCrudRepository extends CrudRepository<ProjectDbEntity, Integer> {
    ProjectDbEntity findByPublicId(UUID publicId);
}
