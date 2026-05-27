package com.ids.presentation.response;

import com.ids.domain.model.PageResult;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int size,
        int number,
        boolean first,
        boolean last
) {
    public static <T> PageResponse<T> from(PageResult<T> page) {
        return new PageResponse<>(page.content(), page.totalElements(), page.totalPages(), page.size(), page.number(), page.first(), page.last());
    }
}
