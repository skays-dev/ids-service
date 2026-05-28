package com.ids.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResultTest {

    @Test
    void map_shouldTransformContentAndPreserveMetadata() {
        PageResult<Integer> source = new PageResult<>(List.of(1, 2, 3), 0, 3, 10, 4, true, false);

        PageResult<String> result = source.map(value -> "A" + value);

        assertThat(result.content()).containsExactly("A1", "A2", "A3");
        assertThat(result.number()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.totalElements()).isEqualTo(10);
        assertThat(result.totalPages()).isEqualTo(4);
        assertThat(result.first()).isTrue();
        assertThat(result.last()).isFalse();
    }
}
