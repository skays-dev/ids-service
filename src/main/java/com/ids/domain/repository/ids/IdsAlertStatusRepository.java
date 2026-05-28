package com.ids.domain.repository.ids;

import com.ids.domain.entity.ids.IdsAlertStatus;

import java.util.Optional;

public interface IdsAlertStatusRepository {
    Optional<IdsAlertStatus> findByCode(String code);
}
