package com.ids.application.usecase.ids;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.application.mapper.IdsAlertDtoMapper;
import com.ids.application.service.ids.IdsAlertRealtimePublisher;
import com.ids.domain.entity.ids.IdsAlert;
import com.ids.domain.entity.ids.IdsAlertStatus;
import com.ids.domain.exception.ResourceNotFoundException;
import com.ids.domain.repository.ids.IdsAlertRepository;
import com.ids.domain.repository.ids.IdsAlertStatusRepository;
import com.ids.shared.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateIdsAlertStatusUseCase {
    private final IdsAlertRepository idsAlertRepository;
    private final IdsAlertStatusRepository idsAlertStatusRepository;
    private final IdsAlertRealtimePublisher realtimePublisher;
    private final IdsAlertDtoMapper idsAlertDtoMapper;

    @Transactional(transactionManager = "idsTransactionManager")
    public IdsAlertDto execute(Long idsAlertId, String statusCode) {
        IdsAlert idsAlert = idsAlertRepository.findById(idsAlertId)
                .orElseThrow(() -> new ResourceNotFoundException("IdsAlert not found: " + idsAlertId));
        IdsAlertStatus status = idsAlertStatusRepository.findByCode(StringUtils.upperOrNull(statusCode))
                .orElseThrow(() -> new ResourceNotFoundException("Unknown status: " + statusCode));
        idsAlert.changeStatus(status);
        IdsAlertDto dto = idsAlertDtoMapper.toDto(idsAlertRepository.save(idsAlert));
        realtimePublisher.publish(dto);
        return dto;
    }
}
