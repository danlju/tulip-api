package com.danlju.tulip.infrastructure.db;

import com.danlju.tulip.infrastructure.db.entity.UserDbEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserCrudRepository extends CrudRepository<UserDbEntity, Integer> {
    UserDbEntity findByPublicId(UUID publicId);
}
