package com.ids.application.service;

import com.ids.application.dto.AlertDto;

public interface AlertRealtimePublisher {
    void publish(AlertDto alert);

    void publishRefresh();
}
