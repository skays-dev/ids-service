package com.ids.domain.repository;

import com.ids.domain.entity.RiskLevel;

import java.util.List;
import java.util.Optional;

public interface RiskLevelRepository {
    Optional<RiskLevel> findByCode(String code);
    List<RiskLevel> findAllBySeverityOrderDesc();
}
