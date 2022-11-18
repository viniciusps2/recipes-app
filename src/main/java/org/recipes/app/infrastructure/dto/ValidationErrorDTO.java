package org.recipes.app.infrastructure.dto;

import java.util.Map;

public record ValidationErrorDTO(
        String message,
        Map<String, String> fields
) {}
