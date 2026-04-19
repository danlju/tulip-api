package com.danlju.tulip.application.service;

import com.danlju.tulip.application.repository.BuildRepository;
import com.danlju.tulip.core.domain.Build;

import java.util.List;
import java.util.UUID;

public class InMemoryBuildRepository implements BuildRepository {
    @Override
    public Build save(Build build) {
        return null;
    }

    @Override
    public Build findById(Integer id) {
        return null;
    }

    @Override
    public Build findByPublicId(UUID id) {
        return null;
    }

    @Override
    public List<Build> findByProjectId(Integer id) {
        return List.of();
    }

    @Override
    public List<Build> findByProjectPublicId(UUID publicId) {
        return List.of();
    }
}
