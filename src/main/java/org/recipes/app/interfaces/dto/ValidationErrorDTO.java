package org.recipes.app.interfaces.dto;

import java.util.Map;

public record ValidationErrorDTO(
        String message,
        Map<String, String> fields
) {}
