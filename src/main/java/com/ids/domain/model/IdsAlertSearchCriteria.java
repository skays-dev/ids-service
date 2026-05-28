package com.ids.domain.model;

public record IdsAlertSearchCriteria(
        String statusCode,
        String riskCode,
        String search
) {}
