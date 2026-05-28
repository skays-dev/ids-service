package com.ids.infrastructure.messaging;

import com.ids.application.dto.ids.IdsAlertDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

import static com.ids.testsupport.IdsTestFixtures.alertDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebSocketIdsAlertRealtimePublisherTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private WebSocketIdsAlertRealtimePublisher publisher;

    @Test
    void publish_shouldSendAlertUpdatedMessageToTopic() {
        IdsAlertDto dto = alertDto(1L, "HIGH", "NEW");

        publisher.publish(dto);

        ArgumentCaptor<Map<String, Object>> payloadCaptor = mapCaptor();
        verify(messagingTemplate).convertAndSend(eq("/topic/idsAlerts"), payloadCaptor.capture());
        assertThat(payloadCaptor.getValue()).containsEntry("type", "ALERT_UPDATED");
        assertThat(payloadCaptor.getValue()).containsEntry("idsAlert", dto);
        assertThat(payloadCaptor.getValue()).containsKey("at");
    }

    @Test
    void publishRefresh_shouldSendRefreshRequiredMessageToTopic() {
        publisher.publishRefresh();

        ArgumentCaptor<Map<String, Object>> payloadCaptor = mapCaptor();
        verify(messagingTemplate).convertAndSend(eq("/topic/idsAlerts"), payloadCaptor.capture());
        assertThat(payloadCaptor.getValue()).containsEntry("type", "ALERTS_REFRESH_REQUIRED");
        assertThat(payloadCaptor.getValue()).containsKey("at");
    }

    @SuppressWarnings("unchecked")
    private ArgumentCaptor<Map<String, Object>> mapCaptor() {
        return ArgumentCaptor.forClass(Map.class);
    }
}
