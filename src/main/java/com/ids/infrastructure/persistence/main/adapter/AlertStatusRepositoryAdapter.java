package com.ids.infrastructure.persistence.main.adapter;

import com.ids.domain.entity.AlertStatus;
import com.ids.domain.repository.AlertStatusRepository;
import com.ids.infrastructure.persistence.main.mapper.AlertPersistenceMapper;
import com.ids.infrastructure.persistence.main.repository.JpaAlertStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AlertStatusRepositoryAdapter implements AlertStatusRepository {
    private final JpaAlertStatusRepository jpaAlertStatusRepository;

    @Override
    public Optional<AlertStatus> findByCode(String code) {
        return jpaAlertStatusRepository.findByCode(code).map(AlertPersistenceMapper::toDomain);
    }
}
