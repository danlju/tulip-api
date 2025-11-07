package com.danlju.tulip.infrastructure.db;

import com.danlju.tulip.infrastructure.db.entity.BuildDbEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BuildCrudRepository extends CrudRepository<BuildDbEntity, Integer> {
    BuildDbEntity findByPublicId(UUID id);
//    @Query("from BuildDbEntity b where b.externalId=?1")
//    @Query("select distinct b from BuildDbEntity b where b.externalId = :externalId")
    BuildDbEntity findByExternalId(String externalId);

}
