package com.danlju.tulip.core.domain;

import java.util.UUID;

/** not really used at the moment */
public class User {

    private Integer id;
    private UUID publicId;
    private String username;
    private String password;

    public User(UUID publicId, String username) {
        this.publicId = publicId;
        this.username = username;
    }

    public User(Integer id, UUID publicId, String username) {
        this.id = id;
        this.publicId = publicId;
        this.username = username;
    }
}
