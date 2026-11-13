package com.danlju.tulip.infrastructure.dataaccess;

import com.danlju.tulip.infrastructure.dataaccess.entity.BuildDbEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BuildCrudRepository extends CrudRepository<BuildDbEntity, Integer> {

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from BuildDbEntity b where b.id = ?1")
    java.util.Optional<BuildDbEntity> lockById(Integer id);

    BuildDbEntity findByPublicId(UUID id);
    @Query("from BuildDbEntity b where b.project.id=?1 order by b.number desc")
    List<BuildDbEntity> findByProjectId(Integer id);

    @Query("from BuildDbEntity b where b.project.publicId=?1 order by b.number desc")
    List<BuildDbEntity> findByProjectPublicId(UUID publicId);
}
