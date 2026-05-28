package com.ids.application.service.ids;

import com.ids.application.dto.ids.IdsAlertDto;

public interface IdsAlertRealtimePublisher {
    void publish(IdsAlertDto idsAlert);

    void publishRefresh();
}
