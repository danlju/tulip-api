package com.danlju.tulip.infrastructure.dataaccess;
import com.danlju.tulip.infrastructure.dataaccess.entity.BuildAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BuildAttemptRepository extends JpaRepository<BuildAttempt, Integer> {}
