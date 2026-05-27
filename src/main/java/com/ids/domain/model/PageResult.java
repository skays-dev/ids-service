package com.ids.domain.model;

import java.util.List;
import java.util.function.Function;

public record PageResult<T>(
        List<T> content,
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public <R> PageResult<R> map(Function<T, R> mapper) {
        return new PageResult<>(content.stream().map(mapper).toList(), number, size, totalElements, totalPages, first, last);
    }
}
