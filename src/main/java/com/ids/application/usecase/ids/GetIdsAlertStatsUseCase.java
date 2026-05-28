package com.ids.application.usecase.ids;

import com.ids.application.dto.ids.IdsAlertStatsDto;
import com.ids.domain.repository.ids.IdsAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetIdsAlertStatsUseCase {
    private final IdsAlertRepository idsAlertRepository;

    @Transactional(readOnly = true, transactionManager = "idsTransactionManager")
    public IdsAlertStatsDto execute() {
        Map<String, Long> byRisk = idsAlertRepository.countByRisk().stream()
                .collect(Collectors.toMap(row -> row[0].toString(), row -> (Long) row[1], (a, b) -> a, LinkedHashMap::new));
        Map<String, Long> byStatus = idsAlertRepository.countByStatus().stream()
                .collect(Collectors.toMap(row -> row[0].toString(), row -> (Long) row[1], (a, b) -> a, LinkedHashMap::new));

        return new IdsAlertStatsDto(
                idsAlertRepository.count(),
                idsAlertRepository.countSince(LocalDateTime.now().minusHours(24)),
                idsAlertRepository.countByStatusCode("NEW"),
                idsAlertRepository.countByRiskAndStatus("HIGH", "NEW"),
                byRisk,
                byStatus
        );
    }
}
