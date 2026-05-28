package com.ids.infrastructure.persistence.repository.ids;

import com.ids.infrastructure.persistence.entity.ids.IdsRiskLevelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdsRiskLevelJpaRepository extends JpaRepository<IdsRiskLevelJpaEntity, Long> {
    Optional<IdsRiskLevelJpaEntity> findByCode(String code);
    List<IdsRiskLevelJpaEntity> findAllByOrderBySeverityOrderDesc();
}
