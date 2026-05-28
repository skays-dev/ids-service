package com.ids.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PageQueryTest {

    @Test
    void constructor_shouldNormalizeInvalidValues() {
        PageQuery query = new PageQuery(-5, 0, " ", null);

        assertThat(query.page()).isZero();
        assertThat(query.size()).isEqualTo(1);
        assertThat(query.sortBy()).isEqualTo("time");
        assertThat(query.direction()).isEqualTo(PageQuery.SortDirection.DESC);
    }

    @Test
    void constructor_shouldCapSizeToOneHundred() {
        PageQuery query = new PageQuery(2, 500, "confidence", PageQuery.SortDirection.ASC);

        assertThat(query.page()).isEqualTo(2);
        assertThat(query.size()).isEqualTo(100);
        assertThat(query.sortBy()).isEqualTo("confidence");
        assertThat(query.direction()).isEqualTo(PageQuery.SortDirection.ASC);
    }
}
