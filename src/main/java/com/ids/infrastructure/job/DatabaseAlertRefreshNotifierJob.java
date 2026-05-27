package com.ids.infrastructure.job;

import com.ids.application.service.AlertRealtimePublisher;
import com.ids.domain.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseAlertRefreshNotifierJob {
    private final AlertRepository alertRepository;
    private final AlertRealtimePublisher realtimePublisher;

    private String lastFingerprint;

    @Scheduled(fixedDelayString = "${ids.alert-refresh-poll-delay-ms:5000}")
    @Transactional(readOnly = true, transactionManager = "mainTransactionManager")
    public void notifyFrontendWhenDatabaseChanges() {
        String currentFingerprint = alertRepository.realtimeFingerprint();
        if (lastFingerprint == null) {
            lastFingerprint = currentFingerprint;
            return;
        }
        if (!lastFingerprint.equals(currentFingerprint)) {
            lastFingerprint = currentFingerprint;
            realtimePublisher.publishRefresh();
            log.debug("Published alert refresh event after database change: {}", currentFingerprint);
        }
    }
}
