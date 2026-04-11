package com.danlju.tulip.infrastructure.dataaccess.entity;

import com.danlju.tulip.core.domain.Deployment;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "deployments")
public class DeploymentDbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UUID publicId;
    private Integer buildId;
    private String stackId;
    private Instant startedAt;
    private String result; // TODO: better result handling

    public static DeploymentDbEntity map(Deployment deployment) {
        return new DeploymentDbEntity();
    }

    public static Deployment map(DeploymentDbEntity result) {
        return null; // TODO
    }
}
