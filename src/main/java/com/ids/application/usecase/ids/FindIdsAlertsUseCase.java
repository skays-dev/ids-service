package com.ids.application.usecase.ids;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.application.mapper.IdsAlertDtoMapper;
import com.ids.domain.model.IdsAlertSearchCriteria;
import com.ids.domain.model.PageQuery;
import com.ids.domain.model.PageResult;
import com.ids.domain.repository.ids.IdsAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindIdsAlertsUseCase {
    private final IdsAlertRepository idsAlertRepository;
    private final IdsAlertDtoMapper idsAlertDtoMapper;

    @Transactional(readOnly = true, transactionManager = "idsTransactionManager")
    public PageResult<IdsAlertDto> execute(IdsAlertSearchCriteria criteria, PageQuery pageQuery) {
        return idsAlertRepository.search(criteria, pageQuery).map(idsAlertDtoMapper::toDto);
    }
}
