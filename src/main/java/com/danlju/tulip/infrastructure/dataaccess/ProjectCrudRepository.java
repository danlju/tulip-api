package com.danlju.tulip.infrastructure.dataaccess;

import com.danlju.tulip.infrastructure.dataaccess.entity.ProjectDbEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProjectCrudRepository extends CrudRepository<ProjectDbEntity, Integer> {
    ProjectDbEntity findByPublicId(UUID publicId);
    ProjectDbEntity getByGithubName(String githubName);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProjectDbEntity p WHERE p.publicId = :publicId")
    ProjectDbEntity findByPublicIdForUpdate(@Param("publicId") UUID publicId);
}
