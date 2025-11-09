package com.danlju.tulip.application.repository;

import com.danlju.tulip.core.domain.User;

import java.util.UUID;

public interface UserRepository {
    User findByPublicId(UUID uuid);
    User findByUsername(String username);
}
