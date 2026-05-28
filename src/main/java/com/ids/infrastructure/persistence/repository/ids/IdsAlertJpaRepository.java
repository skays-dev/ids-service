package com.ids.infrastructure.persistence.repository.ids;

import com.ids.infrastructure.persistence.entity.ids.IdsAlertJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IdsAlertJpaRepository extends JpaRepository<IdsAlertJpaEntity, Long>, JpaSpecificationExecutor<IdsAlertJpaEntity> {
    boolean existsByEventHash(String eventHash);

    @Query("select count(a) from IdsAlertJpaEntity a where a.time >= :from")
    long countSince(@Param("from") LocalDateTime from);

    @Query("select a.idsRiskLevel.code, count(a) from IdsAlertJpaEntity a group by a.idsRiskLevel.code")
    List<Object[]> countByRisk();

    @Query("select a.status.code, count(a) from IdsAlertJpaEntity a group by a.status.code")
    List<Object[]> countByStatus();

    @Query("select count(a) from IdsAlertJpaEntity a where a.status.code = :statusCode")
    long countByStatusCode(@Param("statusCode") String statusCode);

    @Query("select count(a) from IdsAlertJpaEntity a where a.idsRiskLevel.code = :riskCode and a.status.code = :statusCode")
    long countByRiskAndStatus(@Param("riskCode") String riskCode, @Param("statusCode") String statusCode);

    @Query("select count(a), max(coalesce(a.updatedAt, a.createdAt)) from IdsAlertJpaEntity a")
    List<Object[]> realtimeFingerprintParts();
}
