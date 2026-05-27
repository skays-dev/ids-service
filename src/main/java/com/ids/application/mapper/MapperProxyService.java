package com.ids.application.mapper;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapperProxyService {
    private final List<DtoMapper<?, ?>> mappers;

    public MapperProxyService(List<DtoMapper<?, ?>> mappers) {
        this.mappers = mappers;
    }

    public List<DtoMapper<?, ?>> getMappers() {
        return mappers;
    }
}
