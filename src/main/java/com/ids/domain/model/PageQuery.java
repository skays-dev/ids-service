package com.ids.domain.model;

public record PageQuery(
        int page,
        int size,
        String sortBy,
        SortDirection direction
) {
    public PageQuery {
        page = Math.max(page, 0);
        size = Math.min(Math.max(size, 1), 100);
        sortBy = sortBy == null || sortBy.isBlank() ? "time" : sortBy;
        direction = direction == null ? SortDirection.DESC : direction;
    }

    public enum SortDirection {
        ASC,
        DESC
    }
}
