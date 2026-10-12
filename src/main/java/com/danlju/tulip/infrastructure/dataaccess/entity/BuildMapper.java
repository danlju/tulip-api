package com.danlju.tulip.infrastructure.dataaccess.entity;

import com.danlju.tulip.core.domain.Build;

public class BuildMapper {

    public static Build toDomain(BuildDbEntity buildDbEntity) {
        Build build = Build.load(buildDbEntity.getId(), buildDbEntity.getPublicId(), buildDbEntity.getNumber(), buildDbEntity.getProject().getId(), buildDbEntity.getStatus(), buildDbEntity.getCloneUrl(),
                buildDbEntity.getBranch(), buildDbEntity.getCommitSha(), buildDbEntity.getCommitMessage(), buildDbEntity.getStartedAt(), buildDbEntity.getUpdatedAt());
        build.setFailureReason(buildDbEntity.getFailureReason());
        return build;
    }

    public static BuildDbEntity toEntity(Build build) {
        BuildDbEntity entity = new BuildDbEntity(
                build.getId(),
                build.getPublicId(),
                build.getNumber(),
                ProjectDbEntity.load(build.getProjectId()),
                build.getStatus(),
                build.getCloneUrl(),
                build.getBranch(),
                build.getCommitSha(),
                build.getCommitMessage(),
                build.getStartedAt(),
                build.getUpdatedAt()
        );
        entity.setFailureReason(build.getFailureReason());
        return entity;
    }
}
