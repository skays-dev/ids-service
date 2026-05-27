package com.ids.application.dto;

import java.util.Map;

public record AlertStatsDto(
        long total,
        long last24h,
        long newAlerts,
        long highRiskNew,
        Map<String, Long> byRisk,
        Map<String, Long> byStatus
) {}
