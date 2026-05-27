package com.ids.infrastructure.persistence.main.mapper;

import com.ids.domain.entity.Alert;
import com.ids.domain.entity.AlertStatus;
import com.ids.domain.entity.RiskLevel;
import com.ids.infrastructure.persistence.main.entity.AlertJpaEntity;
import com.ids.infrastructure.persistence.main.entity.AlertStatusJpaEntity;
import com.ids.infrastructure.persistence.main.entity.RiskLevelJpaEntity;

public final class AlertPersistenceMapper {
    private AlertPersistenceMapper() {}

    public static Alert toDomain(AlertJpaEntity entity) {
        if (entity == null) return null;
        return new Alert(
                entity.getId(), entity.getTime(), entity.getReason(), entity.getAttack(), entity.getConfidence(),
                entity.getSrcIp(), entity.getDstIp(), entity.getSrcPort(), entity.getDstPort(), entity.getProto(),
                toDomain(entity.getRiskLevel()), toDomain(entity.getStatus()), entity.getEventHash(),
                entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    public static RiskLevel toDomain(RiskLevelJpaEntity entity) {
        if (entity == null) return null;
        return new RiskLevel(entity.getId(), entity.getCode(), entity.getLabel(), entity.getMinConfidence(), entity.getMaxConfidence(), entity.getSeverityOrder());
    }

    public static AlertStatus toDomain(AlertStatusJpaEntity entity) {
        if (entity == null) return null;
        return new AlertStatus(entity.getId(), entity.getCode(), entity.getLabel(), entity.getSortOrder());
    }

    public static AlertJpaEntity toEntity(Alert domain) {
        AlertJpaEntity entity = new AlertJpaEntity();
        entity.setId(domain.getId());
        entity.setTime(domain.getTime());
        entity.setReason(domain.getReason());
        entity.setAttack(domain.getAttack());
        entity.setConfidence(domain.getConfidence());
        entity.setSrcIp(domain.getSrcIp());
        entity.setDstIp(domain.getDstIp());
        entity.setSrcPort(domain.getSrcPort());
        entity.setDstPort(domain.getDstPort());
        entity.setProto(domain.getProto());
        entity.setRiskLevel(toEntity(domain.getRiskLevel()));
        entity.setStatus(toEntity(domain.getStatus()));
        entity.setEventHash(domain.getEventHash());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public static RiskLevelJpaEntity toEntity(RiskLevel domain) {
        RiskLevelJpaEntity entity = new RiskLevelJpaEntity();
        entity.setId(domain.getId());
        entity.setCode(domain.getCode());
        entity.setLabel(domain.getLabel());
        entity.setMinConfidence(domain.getMinConfidence());
        entity.setMaxConfidence(domain.getMaxConfidence());
        entity.setSeverityOrder(domain.getSeverityOrder());
        return entity;
    }

    public static AlertStatusJpaEntity toEntity(AlertStatus domain) {
        AlertStatusJpaEntity entity = new AlertStatusJpaEntity();
        entity.setId(domain.getId());
        entity.setCode(domain.getCode());
        entity.setLabel(domain.getLabel());
        entity.setSortOrder(domain.getSortOrder());
        return entity;
    }
}
