package com.ids.presentation.controller;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.application.dto.ids.IdsAlertStatsDto;
import com.ids.application.usecase.ids.FindIdsAlertsUseCase;
import com.ids.application.usecase.ids.GetIdsAlertStatsUseCase;
import com.ids.application.usecase.ids.UpdateIdsAlertStatusUseCase;
import com.ids.domain.model.IdsAlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.presentation.request.StatusUpdateRequest;
import com.ids.presentation.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/idsAlerts")
@RequiredArgsConstructor
public class IdsAlertController {
    private final FindIdsAlertsUseCase findIdsAlertsUseCase;
    private final GetIdsAlertStatsUseCase getIdsAlertStatsUseCase;
    private final UpdateIdsAlertStatusUseCase updateIdsAlertStatusUseCase;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public PageResponse<IdsAlertDto> idsAlerts(@RequestParam(required = false) String status,
                                         @RequestParam(required = false) String risk,
                                         @RequestParam(required = false) String search,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        return PageResponse.from(findIdsAlertsUseCase.execute(
                new IdsAlertSearchCriteria(status, risk, search),
                new PageQuery(page, size, "time", PageQuery.SortDirection.DESC)
        ));
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public IdsAlertStatsDto stats() {
        return getIdsAlertStatsUseCase.execute();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public IdsAlertDto updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return updateIdsAlertStatusUseCase.execute(id, request.statusCode());
    }
}
