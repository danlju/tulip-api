package com.danlju.tulip.infrastructure.dataaccess;

import com.danlju.tulip.core.domain.Build;
import com.danlju.tulip.application.repository.BuildRepository;
import com.danlju.tulip.infrastructure.dataaccess.entity.BuildDbEntity;
import com.danlju.tulip.infrastructure.dataaccess.entity.BuildMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BuildMysqlRepository implements BuildRepository {

    private static final Logger logger = LoggerFactory.getLogger(BuildMysqlRepository.class);

    private final BuildCrudRepository buildCrudRepository;

    @Autowired
    public BuildMysqlRepository(BuildCrudRepository buildCrudRepository) {
        this.buildCrudRepository = buildCrudRepository;
    }

    @Override
    public Build save(Build build) {
        var entityToSave = BuildMapper.toEntity(build);
        var entity = buildCrudRepository.save(entityToSave);
        return BuildMapper.toDomain(entity);
    }

    @Override
    public Build findById(Integer id) {
        Optional<BuildDbEntity> entity = buildCrudRepository.findById(id);
        return BuildMapper.toDomain(entity.get()); // TODO: fix
    }

    @Override
    public Build findByPublicId(UUID id) {
        var entity = buildCrudRepository.findByPublicId(id);
        return BuildMapper.toDomain(entity);
    }

    @Override
    public List<Build> findByProjectId(Integer id) {
//        var builds = buildCrudRepository.findByProjectId(id);
//        return builds.stream().map(BuildMapper::toDomain).toList();
        return List.of();
    }

    @Override
    public List<Build> findByProjectPublicId(UUID publicId) {
        var builds = buildCrudRepository.findByProjectPublicId(publicId);
        return builds.stream().map(BuildMapper::toDomain).toList();
    }
}
