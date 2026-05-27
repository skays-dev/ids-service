package com.ids.presentation.controller;

import com.ids.application.dto.AlertDto;
import com.ids.application.dto.AlertStatsDto;
import com.ids.application.usecase.FindAlertsUseCase;
import com.ids.application.usecase.GetAlertStatsUseCase;
import com.ids.application.usecase.UpdateAlertStatusUseCase;
import com.ids.domain.model.AlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.presentation.request.StatusUpdateRequest;
import com.ids.presentation.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final FindAlertsUseCase findAlertsUseCase;
    private final GetAlertStatsUseCase getAlertStatsUseCase;
    private final UpdateAlertStatusUseCase updateAlertStatusUseCase;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public PageResponse<AlertDto> alerts(@RequestParam(required = false) String status,
                                         @RequestParam(required = false) String risk,
                                         @RequestParam(required = false) String search,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        return PageResponse.from(findAlertsUseCase.execute(
                new AlertSearchCriteria(status, risk, search),
                new PageQuery(page, size, "time", PageQuery.SortDirection.DESC)
        ));
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public AlertStatsDto stats() {
        return getAlertStatsUseCase.execute();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public AlertDto updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return updateAlertStatusUseCase.execute(id, request.statusCode());
    }
}
