package com.ids.application.mapper;

import com.ids.application.dto.ids.IdsAlertDto;
import com.ids.domain.entity.ids.IdsAlert;
import com.ids.shared.mapper.AbstractDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class IdsAlertDtoMapper extends AbstractDtoMapper<IdsAlertDto, IdsAlert> {

    @Mapper
    public interface IdsAlertDtoMapStructMapper {

        IdsAlertDtoMapStructMapper INSTANCE =
                Mappers.getMapper(IdsAlertDtoMapStructMapper.class);

        @Mapping(target = "riskCode", source = "idsRiskLevel.code")
        @Mapping(target = "riskLabel", source = "idsRiskLevel.label")
        @Mapping(target = "statusCode", source = "status.code")
        @Mapping(target = "statusLabel", source = "status.label")
        IdsAlertDto toDto(IdsAlert entity);

        @Mapping(target = "idsRiskLevel.code", source = "riskCode")
        @Mapping(target = "idsRiskLevel.label", source = "riskLabel")
        @Mapping(target = "status.code", source = "statusCode")
        @Mapping(target = "status.label", source = "statusLabel")
        IdsAlert toEntity(IdsAlertDto dto);
    }

    @Override
    public void addConfiguration() {
    }
}