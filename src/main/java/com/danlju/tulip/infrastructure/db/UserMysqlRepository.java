package com.danlju.tulip.infrastructure.db;

import com.danlju.tulip.domain.User;
import com.danlju.tulip.infrastructure.db.entity.UserDbEntity;
import com.danlju.tulip.repo.UserRepository;
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
