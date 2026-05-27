package com.ids.infrastructure.persistence.main.adapter;

import com.ids.domain.entity.RiskLevel;
import com.ids.domain.repository.RiskLevelRepository;
import com.ids.infrastructure.persistence.main.mapper.AlertPersistenceMapper;
import com.ids.infrastructure.persistence.main.repository.JpaRiskLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RiskLevelRepositoryAdapter implements RiskLevelRepository {
    private final JpaRiskLevelRepository jpaRiskLevelRepository;

    @Override
    public Optional<RiskLevel> findByCode(String code) {
        return jpaRiskLevelRepository.findByCode(code).map(AlertPersistenceMapper::toDomain);
    }

    @Override
    public List<RiskLevel> findAllBySeverityOrderDesc() {
        return jpaRiskLevelRepository.findAllByOrderBySeverityOrderDesc().stream()
                .map(AlertPersistenceMapper::toDomain)
                .toList();
    }
}
