package com.ids.testsupport;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.domain.entity.ids.IdsAlert;
import com.ids.domain.entity.ids.IdsAlertStatus;
import com.ids.domain.entity.ids.IdsRiskLevel;
import com.ids.domain.entity.usr.UsrRole;
import com.ids.domain.entity.usr.UsrUser;
import com.ids.infrastructure.persistence.entity.ids.IdsAlertJpaEntity;
import com.ids.infrastructure.persistence.entity.ids.IdsAlertStatusJpaEntity;
import com.ids.infrastructure.persistence.entity.ids.IdsRiskLevelJpaEntity;
import com.ids.infrastructure.persistence.entity.usr.UsrRoleJpaEntity;
import com.ids.infrastructure.persistence.entity.usr.UsrUserJpaEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public final class IdsTestFixtures {
    private IdsTestFixtures() {
    }

    public static IdsRiskLevel risk(Long id, String code, BigDecimal min, BigDecimal max, int severityOrder) {
        return new IdsRiskLevel(id, code, code + " label", min, max, severityOrder);
    }

    public static IdsAlertStatus status(Long id, String code) {
        return new IdsAlertStatus(id, code, code + " label", id == null ? 0 : id.intValue());
    }

    public static IdsAlert alert(Long id, String riskCode, String statusCode) {
        LocalDateTime now = LocalDateTime.of(2026, 5, 28, 10, 30);
        return new IdsAlert(
                id,
                now,
                "Suspicious traffic",
                "SQL Injection",
                new BigDecimal("91.25"),
                "10.0.0.1",
                "10.0.0.2",
                54321,
                443,
                6,
                risk(11L, riskCode, new BigDecimal("80.00"), new BigDecimal("100.00"), 3),
                status(21L, statusCode),
                "hash-001",
                now.minusMinutes(5),
                null
        );
    }

    public static IdsAlertDto alertDto(Long id, String riskCode, String statusCode) {
        return new IdsAlertDto(
                id,
                LocalDateTime.of(2026, 5, 28, 10, 30),
                "Suspicious traffic",
                "SQL Injection",
                new BigDecimal("91.25"),
                riskCode,
                riskCode + " label",
                statusCode,
                statusCode + " label",
                "10.0.0.1",
                "10.0.0.2",
                54321,
                443,
                6,
                LocalDateTime.of(2026, 5, 28, 10, 25)
        );
    }

    public static IdsRiskLevelJpaEntity riskJpa(Long id, String code) {
        IdsRiskLevelJpaEntity entity = new IdsRiskLevelJpaEntity();
        entity.setId(id);
        entity.setCode(code);
        entity.setLabel(code + " label");
        entity.setMinConfidence(new BigDecimal("80.00"));
        entity.setMaxConfidence(new BigDecimal("100.00"));
        entity.setSeverityOrder(3);
        return entity;
    }

    public static IdsAlertStatusJpaEntity statusJpa(Long id, String code) {
        IdsAlertStatusJpaEntity entity = new IdsAlertStatusJpaEntity();
        entity.setId(id);
        entity.setCode(code);
        entity.setLabel(code + " label");
        entity.setSortOrder(id == null ? 0 : id.intValue());
        return entity;
    }

    public static IdsAlertJpaEntity alertJpa(Long id, String riskCode, String statusCode) {
        IdsAlertJpaEntity entity = new IdsAlertJpaEntity();
        entity.setId(id);
        entity.setTime(LocalDateTime.of(2026, 5, 28, 10, 30));
        entity.setReason("Suspicious traffic");
        entity.setAttack("SQL Injection");
        entity.setConfidence(new BigDecimal("91.25"));
        entity.setSrcIp("10.0.0.1");
        entity.setDstIp("10.0.0.2");
        entity.setSrcPort(54321);
        entity.setDstPort(443);
        entity.setProto(6);
        entity.setIdsRiskLevel(riskJpa(11L, riskCode));
        entity.setStatus(statusJpa(21L, statusCode));
        entity.setEventHash("hash-001");
        entity.setCreatedAt(LocalDateTime.of(2026, 5, 28, 10, 25));
        return entity;
    }

    public static UsrRole role(Long id, String name) {
        return new UsrRole(id, name, name + " description");
    }

    public static UsrUser user(Long id, String username, boolean enabled, UsrRole... roles) {
        return new UsrUser(
                id,
                username,
                username + "@example.com",
                "encoded-password",
                enabled,
                LocalDateTime.of(2026, 5, 28, 9, 0),
                null,
                new HashSet<>(Set.of(roles))
        );
    }

    public static UsrRoleJpaEntity roleJpa(Long id, String name) {
        UsrRoleJpaEntity entity = new UsrRoleJpaEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setDescription(name + " description");
        return entity;
    }

    public static UsrUserJpaEntity userJpa(Long id, String username, boolean enabled, UsrRoleJpaEntity... roles) {
        UsrUserJpaEntity entity = new UsrUserJpaEntity();
        entity.setId(id);
        entity.setUsername(username);
        entity.setEmail(username + "@example.com");
        entity.setPassword("encoded-password");
        entity.setEnabled(enabled);
        entity.setCreatedAt(LocalDateTime.of(2026, 5, 28, 9, 0));
        entity.setRoles(new HashSet<>(Set.of(roles)));
        return entity;
    }
}
