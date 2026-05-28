package com.ids.infrastructure.job;

import com.ids.application.service.ids.IdsAlertRealtimePublisher;
import com.ids.domain.repository.ids.IdsAlertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseIdsAlertRefreshNotifierJobTest {

    @Mock
    private IdsAlertRepository idsAlertRepository;

    @Mock
    private IdsAlertRealtimePublisher realtimePublisher;

    @InjectMocks
    private DatabaseIdsAlertRefreshNotifierJob job;

    @Test
    void notifyFrontendWhenDatabaseChanges_shouldInitializeFingerprintWithoutPublishing() {
        when(idsAlertRepository.realtimeFingerprint()).thenReturn("1|A");

        job.notifyFrontendWhenDatabaseChanges();

        verify(realtimePublisher, never()).publishRefresh();
    }

    @Test
    void notifyFrontendWhenDatabaseChanges_shouldPublishOnlyWhenFingerprintChangesAfterInitialization() {
        when(idsAlertRepository.realtimeFingerprint()).thenReturn("1|A", "1|A", "2|B");

        job.notifyFrontendWhenDatabaseChanges();
        job.notifyFrontendWhenDatabaseChanges();
        job.notifyFrontendWhenDatabaseChanges();

        verify(realtimePublisher).publishRefresh();
    }
}
