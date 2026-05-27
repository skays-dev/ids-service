package com.ids.application.mapper;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;

@Getter
public abstract class AbstractDtoMapper<E, D> implements DtoMapper<E, D>, InitializingBean {
    protected final ModelMapper modelMapper;
    private final Class<E> entityClass;
    private final Class<D> dtoClass;

    protected AbstractDtoMapper(ModelMapper modelMapper, Class<E> entityClass, Class<D> dtoClass) {
        this.modelMapper = modelMapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public void afterPropertiesSet() {
        addConfiguration();
    }

    protected void addConfiguration() {
        // MapStruct handles mapping configuration in concrete mapper classes.
    }
}
