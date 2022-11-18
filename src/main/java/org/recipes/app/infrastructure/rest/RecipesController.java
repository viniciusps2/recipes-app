package org.recipes.app.infrastructure.rest;

import lombok.AllArgsConstructor;
import org.recipes.app.application.RecipeService;
import org.recipes.app.domain.RecipeType;
import org.recipes.app.domain.repositories.RecipeSearch;
import org.recipes.spec.api.RecipesApi;
import org.recipes.spec.model.RecipeDTO;
import org.recipes.spec.model.RecipeTypeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@AllArgsConstructor
public class RecipesController implements RecipesApi {
    private final RecipeService recipeService;

    @Override
    public ResponseEntity<RecipeDTO> createRecipe(RecipeDTO dto) {
        return status(HttpStatus.CREATED).body(recipeService.create(dto));
    }

    @Override
    public ResponseEntity<Void> deleteRecipe(UUID id) {
        recipeService.remove(id);
        return noContent().build();
    }

    @Override
    public ResponseEntity<RecipeDTO> getRecipe(UUID id) {
        return ok().body(recipeService.getRecipe(id));
    }

    @Override
    public ResponseEntity<List<RecipeDTO>> searchRecipes(RecipeTypeDTO recipeType, Integer servings, List<String> ingredientsIncludes, List<String> ingredientsExcludes, String instructionsContains) {
        RecipeType recipeTypeEnum = ofNullable(recipeType).map(r -> RecipeType.valueOf(r.getValue())).orElse(null);
        var recipeSearch = new RecipeSearch(recipeTypeEnum, servings, ingredientsIncludes, ingredientsExcludes, instructionsContains);
        return ok().body(recipeService.search(recipeSearch));
    }

    @Override
    public ResponseEntity<RecipeDTO> updateRecipe(UUID id, RecipeDTO dto) {
        var recipe = recipeService.update(id, dto);
        return ok().body(recipe);
    }
}
