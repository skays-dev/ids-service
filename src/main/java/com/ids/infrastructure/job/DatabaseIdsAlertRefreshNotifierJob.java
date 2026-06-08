package com.ids.infrastructure.job;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.application.mapper.IdsAlertDtoMapper;
import com.ids.application.service.ids.IdsAlertRealtimePublisher;
import com.ids.domain.entity.ids.IdsAlert;
import com.ids.domain.repository.ids.IdsAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseIdsAlertRefreshNotifierJob {

    private final IdsAlertRepository idsAlertRepository;
    private final IdsAlertRealtimePublisher realtimePublisher;
    private final IdsAlertDtoMapper idsAlertDtoMapper;

    private Long lastSeenAlertId;

    @Scheduled(fixedDelayString = "${ids.idsAlert-refresh-poll-delay-ms:5000}")
    @Transactional(readOnly = true, transactionManager = "idsTransactionManager")
    public void notifyFrontendWhenDatabaseChanges() {

        if (lastSeenAlertId == null) {
            lastSeenAlertId = idsAlertRepository.findLastAlert()
                    .map(IdsAlert::getId)
                    .orElse(0L);

            log.debug("Initialized ids alert notifier cursor with lastSeenAlertId={}", lastSeenAlertId);
            return;
        }

        List<IdsAlert> newAlerts = idsAlertRepository.findNewAlertsAfterId(lastSeenAlertId);

        if (newAlerts.isEmpty()) {
            return;
        }

        List<IdsAlertDto> newAlertDtos = newAlerts.stream()
                .map(idsAlertDtoMapper::toDto)
                .toList();

        for (IdsAlertDto dto : newAlertDtos) {
            realtimePublisher.publishCreated(dto);
        }

        lastSeenAlertId = newAlerts.getLast().getId();

        realtimePublisher.publishRefresh();

        log.debug("Published {} new ids alert websocket event(s), lastSeenAlertId={}",
                newAlerts.size(), lastSeenAlertId);
    }
}