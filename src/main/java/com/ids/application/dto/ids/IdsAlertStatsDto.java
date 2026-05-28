package com.ids.application.dto.ids;

import java.util.Map;

public record IdsAlertStatsDto(
        long total,
        long last24h,
        long newIdsAlerts,
        long highRiskNew,
        Map<String, Long> byRisk,
        Map<String, Long> byStatus
) {}
