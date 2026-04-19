package com.danlju.tulip.application.repository;

import com.danlju.tulip.core.domain.Build;

import java.util.List;
import java.util.UUID;

public interface BuildRepository {
    Build save(Build build);
    Build findById(Integer id);
    Build findByPublicId(UUID id);
    List<Build> findByProjectId(Integer id);
    List<Build> findByProjectPublicId(UUID publicId);
}
