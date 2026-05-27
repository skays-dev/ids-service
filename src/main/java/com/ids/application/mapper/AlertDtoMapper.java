package com.ids.application.mapper;

import com.ids.application.dto.AlertDto;
import com.ids.domain.entity.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AlertDtoMapper extends AbstractDtoMapper<Alert, AlertDto> {

    public AlertDtoMapper(ModelMapper modelMapper) {
        super(modelMapper, Alert.class, AlertDto.class);
    }

    @Override
    public AlertDto toDto(Alert entity) {
        if (entity == null) {
            return null;
        }
        return AlertDtoMapperMapStructMapper.INSTANCE.toDto(entity);
    }

    @Override
    protected void addConfiguration() {
        // MapStruct handles this mapper. Keep this method empty for AbstractDtoMapper compatibility.
    }

    @Mapper
    public interface AlertDtoMapperMapStructMapper {
        AlertDtoMapperMapStructMapper INSTANCE = Mappers.getMapper(AlertDtoMapperMapStructMapper.class);

        @Mapping(target = "riskCode", source = "riskLevel.code")
        @Mapping(target = "riskLabel", source = "riskLevel.label")
        @Mapping(target = "statusCode", source = "status.code")
        @Mapping(target = "statusLabel", source = "status.label")
        AlertDto toDto(Alert entity);
    }
}
