package com.danlju.tulip.infrastructure.dataaccess;

import com.danlju.tulip.infrastructure.dataaccess.entity.BuildDbEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BuildCrudRepository extends CrudRepository<BuildDbEntity, Integer> {

    BuildDbEntity findByPublicId(UUID id);
    @Query("from BuildDbEntity b where b.project.id=?1 order by b.number desc")
    List<BuildDbEntity> findByProjectId(Integer id);

    @Query("from BuildDbEntity b where b.project.publicId=?1 order by b.number desc")
    List<BuildDbEntity> findByProjectPublicId(UUID publicId);
}
