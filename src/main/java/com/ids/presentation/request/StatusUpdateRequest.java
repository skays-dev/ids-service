package com.ids.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateRequest(@NotBlank String statusCode) {}
