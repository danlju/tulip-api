package com.danlju.tulip.repo;

import com.danlju.tulip.domain.Build;

import java.util.UUID;

public interface BuildRepository {
    Build save(Build build);
    Build findById(Integer id);
    Build findByPublicId(UUID id);
    Build findByExternalId(String externalId);
}
