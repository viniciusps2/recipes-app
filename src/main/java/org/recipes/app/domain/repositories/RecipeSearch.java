package org.recipes.app.domain.repositories;

import lombok.Builder;
import org.recipes.app.domain.RecipeType;

import java.util.ArrayList;
import java.util.List;

@Builder
public record RecipeSearch(
        RecipeType recipeType,
        Integer servings,
        List<String> ingredientsIncludes,
        List<String> ingredientsExcludes,
        String instructionsContains
) {

    public RecipeSearch {
        ingredientsIncludes = sanitizeList(ingredientsIncludes);
        ingredientsExcludes = sanitizeList(ingredientsExcludes);
        instructionsContains = sanitizeString(instructionsContains);
    }

    private List<String> sanitizeList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream().map(RecipeSearch::sanitizeString).toList();
    }

    private static String sanitizeString(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }
}
