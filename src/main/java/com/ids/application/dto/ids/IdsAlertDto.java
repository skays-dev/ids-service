package com.ids.application.dto.ids;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IdsAlertDto(
        Long id,
        LocalDateTime time,
        String reason,
        String attack,
        BigDecimal confidence,

        String riskCode,
        String riskLabel,

        String statusCode,
        String statusLabel,

        String srcIp,
        String dstIp,
        Integer srcPort,
        Integer dstPort,
        Integer proto,

        // AI detection
        Boolean aiDetected,
        BigDecimal aiConfidence,
        String aiLabel,

        // Rule detection
        Boolean ruleDetected,
        String ruleId,
        String ruleCategory,
        String ruleMsg,

        // Correlation
        String correlationKey,
        String evidence,

        LocalDateTime createdAt
) {}