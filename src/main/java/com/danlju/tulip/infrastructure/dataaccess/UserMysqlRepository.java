package com.danlju.tulip.infrastructure.dataaccess;

import com.danlju.tulip.core.domain.User;
import com.danlju.tulip.infrastructure.dataaccess.entity.UserDbEntity;
import com.danlju.tulip.application.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserMysqlRepository implements UserRepository {

    private final UserCrudRepository userCrudRepository;

    public UserMysqlRepository(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public User findByPublicId(UUID publicId) {
        return UserDbEntity.toUser(
                userCrudRepository.findByPublicId(publicId)
        );
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }
}
