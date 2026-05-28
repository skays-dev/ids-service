package com.ids.infrastructure.messaging;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.application.service.ids.IdsAlertRealtimePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketIdsAlertRealtimePublisher implements IdsAlertRealtimePublisher {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publish(IdsAlertDto idsAlert) {
        messagingTemplate.convertAndSend("/topic/idsAlerts", Map.of(
                "type", "ALERT_UPDATED",
                "at", LocalDateTime.now().toString(),
                "idsAlert", idsAlert
        ));
    }

    @Override
    public void publishRefresh() {
        messagingTemplate.convertAndSend("/topic/idsAlerts", Map.of(
                "type", "ALERTS_REFRESH_REQUIRED",
                "at", LocalDateTime.now().toString()
        ));
    }
}
