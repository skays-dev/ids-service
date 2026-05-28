package com.ids.infrastructure.persistence.adapter.ids;

import com.ids.domain.entity.ids.IdsAlert;
import com.ids.domain.model.IdsAlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.domain.model.PageResult;
import com.ids.domain.repository.ids.IdsAlertRepository;
import com.ids.infrastructure.persistence.entity.ids.IdsAlertJpaEntity;
import com.ids.infrastructure.persistence.mapper.ids.IdsAlertPersistenceMapper;
import com.ids.infrastructure.persistence.repository.ids.IdsAlertJpaRepository;
import com.ids.infrastructure.persistence.repository.ids.IdsAlertStatusJpaRepository;
import com.ids.infrastructure.persistence.repository.ids.IdsRiskLevelJpaRepository;
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
public class IdsAlertRepositoryAdapter implements IdsAlertRepository {

    private static final IdsAlertPersistenceMapper.IdsAlertPersistenceMapStructMapper MAPPER =
            IdsAlertPersistenceMapper.IdsAlertPersistenceMapStructMapper.INSTANCE;
    private final IdsAlertJpaRepository idsAlertJpaRepository;
    private final IdsAlertStatusJpaRepository idsAlertStatusJpaRepository;
    private final IdsRiskLevelJpaRepository idsRiskLevelJpaRepository;

    @Override
    public PageResult<IdsAlert> search(IdsAlertSearchCriteria criteria, PageQuery pageQuery) {
        Sort.Direction direction = pageQuery.direction() == PageQuery.SortDirection.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        String sortBy = switch (pageQuery.sortBy()) {
            case "time" -> "time";
            case "confidence" -> "confidence";
            default -> "time";
        };

        var page = idsAlertJpaRepository.findAll(
                toSpecification(criteria),
                PageRequest.of(pageQuery.page(), pageQuery.size(), Sort.by(direction, sortBy))
        );

        return new PageResult<>(
                page.getContent().stream()
                        .map(MAPPER::toDomain)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    @Override
    public Optional<IdsAlert> findById(Long id) {
        return idsAlertJpaRepository.findById(id)
                .map(MAPPER::toDomain);
    }

    @Override
    public IdsAlert save(IdsAlert idsAlert) {
        IdsAlertJpaEntity entity = MAPPER.toEntity(idsAlert);

        entity.setIdsRiskLevel(
                idsRiskLevelJpaRepository.getReferenceById(idsAlert.getIdsRiskLevel().getId())
        );
        entity.setStatus(
                idsAlertStatusJpaRepository.getReferenceById(idsAlert.getStatus().getId())
        );

        return MAPPER.toDomain(idsAlertJpaRepository.save(entity));
    }

    @Override
    public boolean existsByEventHash(String eventHash) {
        return idsAlertJpaRepository.existsByEventHash(eventHash);
    }

    @Override
    public long count() {
        return idsAlertJpaRepository.count();
    }

    @Override
    public long countSince(LocalDateTime from) {
        return idsAlertJpaRepository.countSince(from);
    }

    @Override
    public long countByStatusCode(String statusCode) {
        return idsAlertJpaRepository.countByStatusCode(statusCode);
    }

    @Override
    public long countByRiskAndStatus(String riskCode, String statusCode) {
        return idsAlertJpaRepository.countByRiskAndStatus(riskCode, statusCode);
    }

    @Override
    public List<Object[]> countByRisk() {
        return idsAlertJpaRepository.countByRisk();
    }

    @Override
    public List<Object[]> countByStatus() {
        return idsAlertJpaRepository.countByStatus();
    }

    @Override
    public String realtimeFingerprint() {
        List<Object[]> rows = idsAlertJpaRepository.realtimeFingerprintParts();
        if (rows == null || rows.isEmpty()) {
            return "0|";
        }

        Object[] row = rows.get(0);
        return String.valueOf(row[0]) + "|" + String.valueOf(row[1]);
    }

    private Specification<IdsAlertJpaEntity> toSpecification(IdsAlertSearchCriteria criteria) {
        return Specification
                .where(status(criteria.statusCode()))
                .and(risk(criteria.riskCode()))
                .and(search(criteria.search()));
    }

    private Specification<IdsAlertJpaEntity> status(String statusCode) {
        return (root, query, cb) -> StringUtils.isBlank(statusCode)
                ? cb.conjunction()
                : cb.equal(root.join("status").get("code"), statusCode.toUpperCase());
    }

    private Specification<IdsAlertJpaEntity> risk(String riskCode) {
        return (root, query, cb) -> StringUtils.isBlank(riskCode)
                ? cb.conjunction()
                : cb.equal(root.join("idsRiskLevel").get("code"), riskCode.toUpperCase());
    }

    private Specification<IdsAlertJpaEntity> search(String search) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(search)) {
                return cb.conjunction();
            }

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
