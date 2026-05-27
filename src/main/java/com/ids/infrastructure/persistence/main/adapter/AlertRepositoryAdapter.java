package com.ids.infrastructure.persistence.main.adapter;

import com.ids.domain.entity.Alert;
import com.ids.domain.model.AlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.domain.model.PageResult;
import com.ids.domain.repository.AlertRepository;
import com.ids.infrastructure.persistence.main.entity.AlertJpaEntity;
import com.ids.infrastructure.persistence.main.mapper.AlertPersistenceMapper;
import com.ids.infrastructure.persistence.main.repository.JpaAlertRepository;
import com.ids.infrastructure.persistence.main.repository.JpaAlertStatusRepository;
import com.ids.infrastructure.persistence.main.repository.JpaRiskLevelRepository;
import com.ids.shared.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AlertRepositoryAdapter implements AlertRepository {
    private final JpaAlertRepository jpaAlertRepository;
    private final JpaRiskLevelRepository jpaRiskLevelRepository;
    private final JpaAlertStatusRepository jpaAlertStatusRepository;

    @Override
    public PageResult<Alert> search(AlertSearchCriteria criteria, PageQuery pageQuery) {
        Sort.Direction direction = pageQuery.direction() == PageQuery.SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortBy = switch (pageQuery.sortBy()) {
            case "time" -> "time";
            case "confidence" -> "confidence";
            default -> "time";
        };
        var page = jpaAlertRepository.findAll(toSpecification(criteria), PageRequest.of(pageQuery.page(), pageQuery.size(), Sort.by(direction, sortBy)));
        return new PageResult<>(
                page.getContent().stream().map(AlertPersistenceMapper::toDomain).toList(),
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isFirst(), page.isLast()
        );
    }

    @Override
    public Optional<Alert> findById(Long id) {
        return jpaAlertRepository.findById(id).map(AlertPersistenceMapper::toDomain);
    }

    @Override
    public Alert save(Alert alert) {
        AlertJpaEntity entity = AlertPersistenceMapper.toEntity(alert);
        entity.setRiskLevel(jpaRiskLevelRepository.getReferenceById(alert.getRiskLevel().getId()));
        entity.setStatus(jpaAlertStatusRepository.getReferenceById(alert.getStatus().getId()));
        return AlertPersistenceMapper.toDomain(jpaAlertRepository.save(entity));
    }

    @Override public boolean existsByEventHash(String eventHash) { return jpaAlertRepository.existsByEventHash(eventHash); }
    @Override public long count() { return jpaAlertRepository.count(); }
    @Override public long countSince(LocalDateTime from) { return jpaAlertRepository.countSince(from); }
    @Override public long countByStatusCode(String statusCode) { return jpaAlertRepository.countByStatusCode(statusCode); }
    @Override public long countByRiskAndStatus(String riskCode, String statusCode) { return jpaAlertRepository.countByRiskAndStatus(riskCode, statusCode); }
    @Override public List<Object[]> countByRisk() { return jpaAlertRepository.countByRisk(); }
    @Override public List<Object[]> countByStatus() { return jpaAlertRepository.countByStatus(); }

    @Override
    public String realtimeFingerprint() {
        List<Object[]> rows = jpaAlertRepository.realtimeFingerprintParts();
        if (rows == null || rows.isEmpty()) {
            return "0|";
        }
        Object[] row = rows.get(0);
        return String.valueOf(row[0]) + "|" + String.valueOf(row[1]);
    }

    private Specification<AlertJpaEntity> toSpecification(AlertSearchCriteria criteria) {
        return Specification
                .where(status(criteria.statusCode()))
                .and(risk(criteria.riskCode()))
                .and(search(criteria.search()));
    }

    private Specification<AlertJpaEntity> status(String statusCode) {
        return (root, query, cb) -> StringUtils.isBlank(statusCode)
                ? cb.conjunction()
                : cb.equal(root.join("status").get("code"), statusCode.toUpperCase());
    }

    private Specification<AlertJpaEntity> risk(String riskCode) {
        return (root, query, cb) -> StringUtils.isBlank(riskCode)
                ? cb.conjunction()
                : cb.equal(root.join("riskLevel").get("code"), riskCode.toUpperCase());
    }

    private Specification<AlertJpaEntity> search(String search) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(search)) return cb.conjunction();
            String like = StringUtils.lowerLike(search);
            return cb.or(
                    cb.like(cb.lower(root.get("attack")), like),
                    cb.like(cb.lower(root.get("reason")), like),
                    cb.like(cb.lower(root.get("srcIp")), like),
                    cb.like(cb.lower(root.get("dstIp")), like)
            );
        };
    }
}
