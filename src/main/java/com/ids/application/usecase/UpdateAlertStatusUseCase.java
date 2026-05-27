package com.ids.application.usecase;

import com.ids.application.dto.AlertDto;
import com.ids.application.mapper.AlertDtoMapper;
import com.ids.application.service.AlertRealtimePublisher;
import com.ids.domain.entity.Alert;
import com.ids.domain.entity.AlertStatus;
import com.ids.domain.exception.ResourceNotFoundException;
import com.ids.domain.repository.AlertRepository;
import com.ids.domain.repository.AlertStatusRepository;
import com.ids.shared.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateAlertStatusUseCase {
    private final AlertRepository alertRepository;
    private final AlertStatusRepository alertStatusRepository;
    private final AlertRealtimePublisher realtimePublisher;
    private final AlertDtoMapper alertDtoMapper;

    @Transactional(transactionManager = "mainTransactionManager")
    public AlertDto execute(Long alertId, String statusCode) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found: " + alertId));
        AlertStatus status = alertStatusRepository.findByCode(StringUtils.upperOrNull(statusCode))
                .orElseThrow(() -> new ResourceNotFoundException("Unknown status: " + statusCode));
        alert.changeStatus(status);
        AlertDto dto = alertDtoMapper.toDto(alertRepository.save(alert));
        realtimePublisher.publish(dto);
        return dto;
    }
}
