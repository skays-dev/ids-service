package com.ids.infrastructure.persistence.main.repository;

import com.ids.infrastructure.persistence.main.entity.AlertJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaAlertRepository extends JpaRepository<AlertJpaEntity, Long>, JpaSpecificationExecutor<AlertJpaEntity> {
    boolean existsByEventHash(String eventHash);

    @Query("select count(a) from AlertJpaEntity a where a.time >= :from")
    long countSince(@Param("from") LocalDateTime from);

    @Query("select a.riskLevel.code, count(a) from AlertJpaEntity a group by a.riskLevel.code")
    List<Object[]> countByRisk();

    @Query("select a.status.code, count(a) from AlertJpaEntity a group by a.status.code")
    List<Object[]> countByStatus();

    @Query("select count(a) from AlertJpaEntity a where a.status.code = :statusCode")
    long countByStatusCode(@Param("statusCode") String statusCode);

    @Query("select count(a) from AlertJpaEntity a where a.riskLevel.code = :riskCode and a.status.code = :statusCode")
    long countByRiskAndStatus(@Param("riskCode") String riskCode, @Param("statusCode") String statusCode);

    @Query("select count(a), max(coalesce(a.updatedAt, a.createdAt)) from AlertJpaEntity a")
    List<Object[]> realtimeFingerprintParts();
}
