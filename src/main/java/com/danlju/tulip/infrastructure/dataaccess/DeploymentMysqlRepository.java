package com.danlju.tulip.infrastructure.dataaccess;

import com.danlju.tulip.application.repository.DeploymentRepository;
import com.danlju.tulip.core.domain.Deployment;
import com.danlju.tulip.infrastructure.dataaccess.entity.DeploymentDbEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DeploymentMysqlRepository implements DeploymentRepository {

    private final DeploymentCrudRepository deploymentCrudRepository;

    @Autowired
    public DeploymentMysqlRepository(DeploymentCrudRepository deploymentCrudRepository) {
        this.deploymentCrudRepository = deploymentCrudRepository;
    }

    @Override
    public Deployment save(Deployment deployment) {
        var result = deploymentCrudRepository.save(DeploymentDbEntity.map(deployment));
        return DeploymentDbEntity.map(result);
    }

}
