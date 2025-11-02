package com.danlju.tulip.repo;

import com.danlju.tulip.domain.User;

import java.util.UUID;

public interface UserRepository {
    User findByPublicId(UUID uuid);
    User findByUsername(String username);
}
