package com.ids.shared.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MapperProxyService {

    private final Map<String, Object> mapperCache = new ConcurrentHashMap<>();

    private static final List<String> BASE_PACKAGES = List.of(
            "com.ids.application.mapper",
            "com.ids.infrastructure.persistence.mapper"
    );

    @SuppressWarnings("unchecked")
    public <T> T getMapper(
            Class<?> sourceClass,
            Class<?> targetClass,
            String toTargetMethodName,
            String toSourceMethodName,
            boolean isFull
    ) {
        String key = sourceClass.getName()
                + "->"
                + targetClass.getName()
                + ":"
                + toTargetMethodName
                + ":"
                + toSourceMethodName
                + (isFull ? ":full" : "");

        return (T) mapperCache.computeIfAbsent(key, k -> {
            Object mapStructMapper = findMapStructMapper(
                    sourceClass,
                    targetClass,
                    toTargetMethodName,
                    toSourceMethodName
            );

            return Objects.requireNonNullElseGet(
                    mapStructMapper,
                    () -> new ReflectionMapper(sourceClass, targetClass)
            );
        });
    }

    private Object findMapStructMapper(
            Class<?> sourceClass,
            Class<?> targetClass,
            String toTargetMethodName,
            String toSourceMethodName
    ) {
        Set<Class<?>> mapperClasses = scanForMapperClasses();

        for (Class<?> mapperClass : mapperClasses) {
            for (Class<?> nested : mapperClass.getDeclaredClasses()) {
                if (isMapStructMapperInterface(nested)
                        && matchesSourceTarget(
                        nested,
                        sourceClass,
                        targetClass,
                        toTargetMethodName,
                        toSourceMethodName
                )) {
                    return getMapperInstance(nested);
                }
            }
        }

        return null;
    }

    private Set<Class<?>> scanForMapperClasses() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(
                new AnnotationTypeFilter(org.springframework.stereotype.Component.class)
        );

        Set<Class<?>> classes = new HashSet<>();

        for (String basePackage : BASE_PACKAGES) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);

            for (BeanDefinition bd : candidates) {
                try {
                    Class<?> clazz = Class.forName(bd.getBeanClassName());
                    classes.add(clazz);
                } catch (ClassNotFoundException ignored) {
                }
            }
        }

        return classes;
    }

    private boolean matchesSourceTarget(
            Class<?> mapperInterface,
            Class<?> sourceClass,
            Class<?> targetClass,
            String toTargetMethodName,
            String toSourceMethodName
    ) {
        boolean hasToTarget = false;
        boolean hasToSource = false;

        for (Method method : mapperInterface.getDeclaredMethods()) {
            if (method.getName().equals(toTargetMethodName)
                    && method.getParameterCount() == 1
                    && method.getParameterTypes()[0].equals(sourceClass)
                    && method.getReturnType().equals(targetClass)) {
                hasToTarget = true;
            }

            if (method.getName().equals(toSourceMethodName)
                    && method.getParameterCount() == 1
                    && method.getParameterTypes()[0].equals(targetClass)
                    && method.getReturnType().equals(sourceClass)) {
                hasToSource = true;
            }
        }

        return hasToTarget && hasToSource;
    }

    private boolean isMapStructMapperInterface(Class<?> clazz) {
        boolean isInterface = clazz.isInterface();
        boolean hasAnnotation = clazz.isAnnotationPresent(Mapper.class);
        boolean hasName = clazz.getSimpleName().contains("MapStructMapper")
                || clazz.getSimpleName().endsWith("Mapper");

        return isInterface && (hasAnnotation || hasName);
    }

    private Object getMapperInstance(Class<?> mapperInterface) {
        try {
            try {
                Field instanceField = mapperInterface.getDeclaredField("INSTANCE");
                return instanceField.get(null);
            } catch (NoSuchFieldException e) {
                Method getMapper = org.mapstruct.factory.Mappers.class
                        .getMethod("getMapper", Class.class);

                return getMapper.invoke(null, mapperInterface);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to get mapper instance for: " + mapperInterface.getName(),
                    e
            );
        }
    }

    private static class ReflectionMapper {

        private final Class<?> sourceClass;
        private final Class<?> targetClass;

        ReflectionMapper(Class<?> sourceClass, Class<?> targetClass) {
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
        }

        public Object toDto(Object source) {
            return copyToTarget(source, targetClass);
        }

        public Object toDtoFull(Object source) {
            return copyToTarget(source, targetClass);
        }

        public Object toDomain(Object source) {
            return copyToTarget(source, targetClass);
        }

        public Object toDomainFull(Object source) {
            return copyToTarget(source, targetClass);
        }

        public Object toEntity(Object source) {
            return copyToTarget(source, sourceClass);
        }

        public Object toEntityFull(Object source) {
            return copyToTarget(source, sourceClass);
        }

        private Object copyToTarget(Object source, Class<?> destinationClass) {
            if (source == null) {
                return null;
            }

            try {
                Object target = destinationClass.getDeclaredConstructor().newInstance();
                copyProperties(source, target);
                return target;
            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to map "
                                + source.getClass().getSimpleName()
                                + " to "
                                + destinationClass.getSimpleName(),
                        e
                );
            }
        }

        private void copyProperties(Object source, Object target) {
            for (Method getter : source.getClass().getMethods()) {
                if (!getter.getName().startsWith("get") || getter.getParameterCount() != 0) {
                    continue;
                }

                try {
                    String propName = getter.getName().substring(3);
                    Object value = getter.invoke(source);
                    String setterName = "set" + propName;

                    try {
                        Method setter = target.getClass()
                                .getMethod(setterName, getter.getReturnType());

                        setter.invoke(target, value);
                    } catch (NoSuchMethodException e) {
                        handleDtoSuffix(target, propName, value, getter.getReturnType());
                    }
                } catch (Exception ignored) {
                }
            }
        }

        private void handleDtoSuffix(
                Object target,
                String propName,
                Object value,
                Class<?> paramType
        ) {
            try {
                if (propName.endsWith("Dto")) {
                    String withoutDto = propName.substring(0, propName.length() - 3);

                    try {
                        Method setter = target.getClass()
                                .getMethod("set" + withoutDto, paramType);

                        setter.invoke(target, value);
                        return;
                    } catch (NoSuchMethodException ignored) {
                    }
                }

                String withDto = propName + "Dto";

                try {
                    Method setter = target.getClass()
                            .getMethod("set" + withDto, paramType);

                    setter.invoke(target, value);
                } catch (NoSuchMethodException ignored) {
                }
            } catch (Exception ignored) {
            }
        }
    }
}