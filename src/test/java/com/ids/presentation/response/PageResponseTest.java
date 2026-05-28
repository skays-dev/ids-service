package com.ids.presentation.response;

import com.ids.domain.model.PageResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseTest {

    @Test
    void from_shouldCopyPageResultMetadata() {
        PageResult<String> page = new PageResult<>(List.of("A", "B"), 2, 10, 25, 3, false, true);

        PageResponse<String> response = PageResponse.from(page);

        assertThat(response.content()).containsExactly("A", "B");
        assertThat(response.number()).isEqualTo(2);
        assertThat(response.size()).isEqualTo(10);
        assertThat(response.totalElements()).isEqualTo(25);
        assertThat(response.totalPages()).isEqualTo(3);
        assertThat(response.first()).isFalse();
        assertThat(response.last()).isTrue();
    }
}
