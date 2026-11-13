package com.danlju.tulip.infrastructure.dataaccess.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name = "build_attempts")
public class BuildAttempt {
    @Id public Integer buildId;
    @Column(nullable = false) public String token;
    @Column(nullable = false) public Instant leaseUntil;
    public BuildAttempt() {}
    public BuildAttempt(Integer buildId, String token, Instant leaseUntil) {
        this.buildId = buildId; this.token = token; this.leaseUntil = leaseUntil;
    }
}
