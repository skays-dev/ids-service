package com.ids.infrastructure.job;

import com.ids.application.service.ids.IdsAlertRealtimePublisher;
import com.ids.domain.repository.ids.IdsAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseIdsAlertRefreshNotifierJob {
    private final IdsAlertRepository idsAlertRepository;
    private final IdsAlertRealtimePublisher realtimePublisher;

    private String lastFingerprint;

    @Scheduled(fixedDelayString = "${ids.idsAlert-refresh-poll-delay-ms:5000}")
    @Transactional(readOnly = true, transactionManager = "idsTransactionManager")
    public void notifyFrontendWhenDatabaseChanges() {
        String currentFingerprint = idsAlertRepository.realtimeFingerprint();
        if (lastFingerprint == null) {
            lastFingerprint = currentFingerprint;
            return;
        }
        if (!lastFingerprint.equals(currentFingerprint)) {
            lastFingerprint = currentFingerprint;
            realtimePublisher.publishRefresh();
            log.debug("Published idsAlert refresh event after database change: {}", currentFingerprint);
        }
    }
}
