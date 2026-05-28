package com.ids.application.dto.usr;

import java.util.List;

public record CurrentUserDto(String username, List<String> roles) {}
