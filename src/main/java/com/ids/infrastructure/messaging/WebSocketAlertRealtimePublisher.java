package com.ids.infrastructure.messaging;

import com.ids.application.dto.AlertDto;
import com.ids.application.service.AlertRealtimePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAlertRealtimePublisher implements AlertRealtimePublisher {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publish(AlertDto alert) {
        messagingTemplate.convertAndSend("/topic/alerts", Map.of(
                "type", "ALERT_UPDATED",
                "at", LocalDateTime.now().toString(),
                "alert", alert
        ));
    }

    @Override
    public void publishRefresh() {
        messagingTemplate.convertAndSend("/topic/alerts", Map.of(
                "type", "ALERTS_REFRESH_REQUIRED",
                "at", LocalDateTime.now().toString()
        ));
    }
}
