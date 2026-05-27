package com.ids.application.dto;

import java.util.List;

public record CurrentUserDto(String username, List<String> roles) {}
