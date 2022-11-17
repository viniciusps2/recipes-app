package org.recipes.app.domain.repositories;

import org.recipes.app.domain.Recipe;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository {
    Recipe save(Recipe recipe);

    Optional<Recipe> findById(UUID id);

    void delete(Recipe recipe);

    List<Recipe> search(RecipeSearch recipeSearch);
}
