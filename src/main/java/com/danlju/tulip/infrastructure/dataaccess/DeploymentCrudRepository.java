package com.danlju.tulip.infrastructure.dataaccess;

import com.danlju.tulip.infrastructure.dataaccess.entity.DeploymentDbEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeploymentCrudRepository extends CrudRepository<DeploymentDbEntity, Integer>{
}
