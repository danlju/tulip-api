package com.danlju.tulip.infrastructure.dataaccess.entity;

import com.danlju.tulip.core.domain.Build;

public class BuildMapper {

    public static Build toDomain(BuildDbEntity buildDbEntity) {
        return Build.load(buildDbEntity.getId(), buildDbEntity.getPublicId(), buildDbEntity.getNumber(), buildDbEntity.getProject().getId(), buildDbEntity.getStatus()); // TODO: revert
    }

    public static BuildDbEntity toEntity(Build build) {
        return new BuildDbEntity(
                build.getId(),
                build.getPublicId(),
                build.getNumber(),
                ProjectDbEntity.load(build.getProjectId()),
                build.getStatus(),
                build.getBranch(),
                build.getCommitSha(),
                build.getCommitMessage(),
                build.getStartedAt(),
                build.getUpdatedAt()
        );
    }
}
