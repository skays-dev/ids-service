package com.ids.domain.repository;

import com.ids.domain.entity.AlertStatus;

import java.util.Optional;

public interface AlertStatusRepository {
    Optional<AlertStatus> findByCode(String code);
}
