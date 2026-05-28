package com.ids.domain.repository.ids;

import com.ids.domain.entity.ids.IdsRiskLevel;

import java.util.List;
import java.util.Optional;

public interface IdsRiskLevelRepository {
    Optional<IdsRiskLevel> findByCode(String code);
    List<IdsRiskLevel> findAllBySeverityOrderDesc();
}
