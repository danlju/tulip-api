package com.danlju.tulip.application.repository;

import com.danlju.tulip.core.domain.Deployment;

public class InMemoryDeploymentRepository implements DeploymentRepository {

    public InMemoryDeploymentRepository() {
    }

    @Override
    public Deployment save(Deployment deployment) {
        return null;
    }
}
