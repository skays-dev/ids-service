package com.ids.domain.repository;

import com.ids.domain.entity.Alert;
import com.ids.domain.model.AlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.domain.model.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AlertRepository {
    PageResult<Alert> search(AlertSearchCriteria criteria, PageQuery pageQuery);
    Optional<Alert> findById(Long id);
    Alert save(Alert alert);
    boolean existsByEventHash(String eventHash);
    long count();
    long countSince(LocalDateTime from);
    long countByStatusCode(String statusCode);
    long countByRiskAndStatus(String riskCode, String statusCode);
    List<Object[]> countByRisk();
    List<Object[]> countByStatus();
    String realtimeFingerprint();
}
