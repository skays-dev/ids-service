package com.ids.infrastructure.persistence.adapter.ids;

import com.ids.domain.entity.ids.IdsAlertStatus;
import com.ids.domain.repository.ids.IdsAlertStatusRepository;
import com.ids.infrastructure.persistence.mapper.ids.IdsAlertStatusPersistenceMapper;
import com.ids.infrastructure.persistence.repository.ids.IdsAlertStatusJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IdsAlertStatusRepositoryAdapter implements IdsAlertStatusRepository {

    private static final IdsAlertStatusPersistenceMapper.IdsAlertStatusPersistenceMapStructMapper MAPPER =
            IdsAlertStatusPersistenceMapper.IdsAlertStatusPersistenceMapStructMapper.INSTANCE;

    private final IdsAlertStatusJpaRepository idsAlertStatusJpaRepository;

    @Override
    public Optional<IdsAlertStatus> findByCode(String code) {
        return idsAlertStatusJpaRepository.findByCode(code)
                .map(MAPPER::toDomain);
    }
}
