package com.danlju.tulip.infrastructure.db.entity;

import com.danlju.tulip.domain.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class UserDbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UUID publicId;
    private String username;
    private String password;

    public UserDbEntity(UUID publicId, String username) {
        this.publicId = publicId;
        this.username = username;
    }

    public UserDbEntity(Integer id, UUID publicId, String username) {
        this.id = id;
        this.publicId = publicId;
        this.username = username;
    }

    public static User toUser(UserDbEntity dbEntity) {
        if (dbEntity == null) {
            return null;
        }
        return new User(dbEntity.id, dbEntity.publicId, dbEntity.username);
    }
}
