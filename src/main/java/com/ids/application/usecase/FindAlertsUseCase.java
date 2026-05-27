package com.ids.application.usecase;

import com.ids.application.dto.AlertDto;
import com.ids.application.mapper.AlertDtoMapper;
import com.ids.domain.model.AlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.domain.model.PageResult;
import com.ids.domain.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindAlertsUseCase {
    private final AlertRepository alertRepository;
    private final AlertDtoMapper alertDtoMapper;

    @Transactional(readOnly = true, transactionManager = "mainTransactionManager")
    public PageResult<AlertDto> execute(AlertSearchCriteria criteria, PageQuery pageQuery) {
        return alertRepository.search(criteria, pageQuery).map(alertDtoMapper::toDto);
    }
}
