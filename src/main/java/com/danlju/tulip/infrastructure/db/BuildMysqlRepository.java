package com.danlju.tulip.infrastructure.db;

import com.danlju.tulip.domain.Build;
import com.danlju.tulip.infrastructure.db.entity.BuildDbEntity;
import com.danlju.tulip.repo.BuildRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class BuildMysqlRepository implements BuildRepository {

    private static final Logger logger = LoggerFactory.getLogger(BuildMysqlRepository.class);

    private BuildCrudRepository buildCrudRepository;

    public BuildMysqlRepository(BuildCrudRepository buildCrudRepository) {
        this.buildCrudRepository = buildCrudRepository;
    }


    @Override
    public Build save(Build build) {
        var entityToSave = BuildDbEntity.toEntity(build);
        logger.info("Saving: {}", entityToSave.toString());
        var entity = buildCrudRepository.save(entityToSave);
        return BuildDbEntity.toBuild(entity);
    }

    @Override
    public Build findById(Integer id) {
        var entity = buildCrudRepository.findById(id);
        return BuildDbEntity.toBuild(entity.orElse(null));
    }

    @Override
    public Build findByPublicId(UUID id) {
        var entity = buildCrudRepository.findByPublicId(id);
        return BuildDbEntity.toBuild(entity);
    }

    @Override
    public Build findByExternalId(String externalId) {
        var entity = buildCrudRepository.findByExternalId(externalId);
        return BuildDbEntity.toBuild(entity);
    }

    @Override
    public List<Build> findByProjectId(Integer id) {
        var builds = buildCrudRepository.findByProjectId(id);
        return builds.stream().map(BuildDbEntity::toBuild).toList();
    }
}
