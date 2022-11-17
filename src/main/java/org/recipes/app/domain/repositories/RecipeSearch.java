package org.recipes.app.domain.repositories;

import org.recipes.app.domain.RecipeType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    }

    private List<String> sanitizeList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream().map(i -> i.trim().toLowerCase()).collect(Collectors.toList());
    }
}
