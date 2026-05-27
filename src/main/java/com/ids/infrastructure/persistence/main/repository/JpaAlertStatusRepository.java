package com.ids.infrastructure.persistence.main.repository;

import com.ids.infrastructure.persistence.main.entity.AlertStatusJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAlertStatusRepository extends JpaRepository<AlertStatusJpaEntity, Long> {
    Optional<AlertStatusJpaEntity> findByCode(String code);
}
