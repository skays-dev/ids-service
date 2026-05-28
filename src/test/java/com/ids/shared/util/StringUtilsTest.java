package com.ids.shared.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    void isBlank_shouldHandleNullEmptyAndWhitespace() {
        assertThat(StringUtils.isBlank(null)).isTrue();
        assertThat(StringUtils.isBlank("")).isTrue();
        assertThat(StringUtils.isBlank("   ")).isTrue();
        assertThat(StringUtils.isBlank("abc")).isFalse();
    }

    @Test
    void upperOrNull_shouldTrimAndUppercaseNonBlankValue() {
        assertThat(StringUtils.upperOrNull(null)).isNull();
        assertThat(StringUtils.upperOrNull("  new ")).isEqualTo("NEW");
    }

    @Test
    void lowerLike_shouldTrimLowercaseAndAddWildcards() {
        assertThat(StringUtils.lowerLike("  SQL Injection ")).isEqualTo("%sql injection%");
    }
}
