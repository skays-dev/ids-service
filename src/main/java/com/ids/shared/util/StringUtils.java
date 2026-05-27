package com.ids.shared.util;

public final class StringUtils {
    private StringUtils() {}

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String upperOrNull(String value) {
        return isBlank(value) ? null : value.trim().toUpperCase();
    }

    public static String lowerLike(String value) {
        return "%" + value.trim().toLowerCase() + "%";
    }
}
