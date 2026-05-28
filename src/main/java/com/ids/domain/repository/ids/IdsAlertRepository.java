package com.ids.domain.repository.ids;

import com.ids.domain.entity.ids.IdsAlert;
import com.ids.domain.model.IdsAlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.domain.model.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IdsAlertRepository {
    PageResult<IdsAlert> search(IdsAlertSearchCriteria criteria, PageQuery pageQuery);
    Optional<IdsAlert> findById(Long id);
    IdsAlert save(IdsAlert idsAlert);
    boolean existsByEventHash(String eventHash);
    long count();
    long countSince(LocalDateTime from);
    long countByStatusCode(String statusCode);
    long countByRiskAndStatus(String riskCode, String statusCode);
    List<Object[]> countByRisk();
    List<Object[]> countByStatus();
    String realtimeFingerprint();
}
