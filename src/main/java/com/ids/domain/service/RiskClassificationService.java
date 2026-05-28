package com.ids.domain.service;

import com.ids.domain.entity.ids.IdsRiskLevel;
import com.ids.domain.exception.DomainException;

import java.math.BigDecimal;
import java.util.List;

public class RiskClassificationService {
    public IdsRiskLevel classify(BigDecimal confidence, List<IdsRiskLevel> availableRisks) {
        return availableRisks.stream()
                .filter(risk -> risk.accepts(confidence))
                .findFirst()
                .orElseThrow(() -> new DomainException("No risk level found for confidence " + confidence));
    }
}
