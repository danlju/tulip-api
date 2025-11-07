package com.danlju.tulip.infrastructure.db;

import com.danlju.tulip.infrastructure.db.entity.ProjectDbEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProjectCrudRepository extends CrudRepository<ProjectDbEntity, Integer> {
    ProjectDbEntity findByPublicId(UUID publicId);

    @Query("from ProjectDbEntity p where p.githubName=?1 order by p.lastSyncedAt desc limit 1")
    ProjectDbEntity findMostRecentSync(String githubName);

    ProjectDbEntity getByGithubName(String githubName);
}
