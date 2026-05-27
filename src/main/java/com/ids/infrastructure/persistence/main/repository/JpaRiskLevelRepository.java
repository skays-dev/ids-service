package com.ids.infrastructure.persistence.main.repository;

import com.ids.infrastructure.persistence.main.entity.RiskLevelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaRiskLevelRepository extends JpaRepository<RiskLevelJpaEntity, Long> {
    Optional<RiskLevelJpaEntity> findByCode(String code);
    List<RiskLevelJpaEntity> findAllByOrderBySeverityOrderDesc();
}
