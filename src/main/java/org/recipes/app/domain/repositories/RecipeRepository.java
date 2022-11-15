package org.recipes.app.domain.repositories;

import org.recipes.app.domain.Recipe;

public interface RecipeRepository {
    Recipe save(Recipe recipe);
}
