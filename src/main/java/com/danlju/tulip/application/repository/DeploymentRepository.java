package com.danlju.tulip.application.repository;

import com.danlju.tulip.core.domain.Deployment;

public interface DeploymentRepository {
    Deployment save(Deployment deployment);
}
