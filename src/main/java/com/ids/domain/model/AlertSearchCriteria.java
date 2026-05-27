package com.ids.domain.model;

public record AlertSearchCriteria(
        String statusCode,
        String riskCode,
        String search
) {}
