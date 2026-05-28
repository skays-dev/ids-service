package com.ids.shared.mapper;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractMapper<TTarget, TSource> {

    @Autowired
    private MapperProxyService proxyService;

    protected Class<TTarget> typeOfTarget;
    protected Class<TSource> typeOfSource;

    private Object realMapper;
    private Object realMapperFull;

    private Method toTargetMethod;
    private Method toSourceMethod;
    private Method toTargetFullMethod;
    private Method toSourceFullMethod;

    @SuppressWarnings("unchecked")
    public AbstractMapper() {
        try {
            Type[] types = ((ParameterizedType) getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments();

            typeOfTarget = (Class<TTarget>) types[0];
            typeOfSource = (Class<TSource>) types[1];
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to get generic types for mapper: " + getClass().getName(),
                    e
            );
        }
    }

    protected abstract String getToTargetMethodName();

    protected abstract String getToSourceMethodName();

    protected String getToTargetFullMethodName() {
        return getToTargetMethodName();
    }

    protected String getToSourceFullMethodName() {
        return getToSourceMethodName();
    }

    @PostConstruct
    public void initMapper() {
        if (proxyService != null) {
            this.realMapper = proxyService.getMapper(
                    typeOfSource,
                    typeOfTarget,
                    getToTargetMethodName(),
                    getToSourceMethodName(),
                    false
            );

            this.realMapperFull = proxyService.getMapper(
                    typeOfSource,
                    typeOfTarget,
                    getToTargetFullMethodName(),
                    getToSourceFullMethodName(),
                    true
            );

            cacheMethods();
        }

        addConfiguration();
        addConfigurationFull();
    }

    private void cacheMethods() {
        if (realMapper != null) {
            this.toTargetMethod = findMethod(
                    realMapper.getClass(),
                    getToTargetMethodName(),
                    typeOfSource
            );

            this.toSourceMethod = findMethod(
                    realMapper.getClass(),
                    getToSourceMethodName(),
                    typeOfTarget
            );
        }

        if (realMapperFull != null) {
            this.toTargetFullMethod = findMethod(
                    realMapperFull.getClass(),
                    getToTargetFullMethodName(),
                    typeOfSource
            );

            this.toSourceFullMethod = findMethod(
                    realMapperFull.getClass(),
                    getToSourceFullMethodName(),
                    typeOfTarget
            );
        }
    }

    private Method findMethod(Class<?> clazz, String methodName, Class<?> parameterType) {
        try {
            return clazz.getMethod(methodName, parameterType);
        } catch (NoSuchMethodException e) {
            try {
                return clazz.getMethod(methodName, Object.class);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(
                        "Method " + methodName + "(" + parameterType.getSimpleName() + ") not found in " + clazz.getName(),
                        e
                );
            }
        }
    }

    protected TTarget mapToTarget(TSource source) {
        if (source == null || toTargetMethod == null) {
            return null;
        }

        try {
            return typeOfTarget.cast(toTargetMethod.invoke(realMapper, source));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error mapping to target in " + getClass().getSimpleName(),
                    e
            );
        }
    }

    protected TSource mapToSource(TTarget source) {
        if (source == null || toSourceMethod == null) {
            return null;
        }

        try {
            return typeOfSource.cast(toSourceMethod.invoke(realMapper, source));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error mapping to source in " + getClass().getSimpleName(),
                    e
            );
        }
    }

    protected TTarget mapToTargetFull(TSource source) {
        if (source == null || toTargetFullMethod == null) {
            return null;
        }

        try {
            return typeOfTarget.cast(toTargetFullMethod.invoke(realMapperFull, source));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error mapping to target full in " + getClass().getSimpleName(),
                    e
            );
        }
    }

    protected TSource mapToSourceFull(TTarget source) {
        if (source == null || toSourceFullMethod == null) {
            return null;
        }

        try {
            return typeOfSource.cast(toSourceFullMethod.invoke(realMapperFull, source));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error mapping to source full in " + getClass().getSimpleName(),
                    e
            );
        }
    }

    public void addConfiguration() {
    }

    public void addConfigurationFull() {
    }
}