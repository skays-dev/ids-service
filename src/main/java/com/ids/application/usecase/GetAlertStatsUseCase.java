package com.ids.application.usecase;

import com.ids.application.dto.AlertStatsDto;
import com.ids.domain.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAlertStatsUseCase {
    private final AlertRepository alertRepository;

    @Transactional(readOnly = true, transactionManager = "mainTransactionManager")
    public AlertStatsDto execute() {
        Map<String, Long> byRisk = alertRepository.countByRisk().stream()
                .collect(Collectors.toMap(row -> row[0].toString(), row -> (Long) row[1], (a, b) -> a, LinkedHashMap::new));
        Map<String, Long> byStatus = alertRepository.countByStatus().stream()
                .collect(Collectors.toMap(row -> row[0].toString(), row -> (Long) row[1], (a, b) -> a, LinkedHashMap::new));

        return new AlertStatsDto(
                alertRepository.count(),
                alertRepository.countSince(LocalDateTime.now().minusHours(24)),
                alertRepository.countByStatusCode("NEW"),
                alertRepository.countByRiskAndStatus("HIGH", "NEW"),
                byRisk,
                byStatus
        );
    }
}
