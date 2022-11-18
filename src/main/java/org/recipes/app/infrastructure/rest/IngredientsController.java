package org.recipes.app.infrastructure.rest;

import lombok.AllArgsConstructor;
import org.recipes.app.application.RecipeService;
import org.recipes.spec.api.IngredientsApi;
import org.recipes.spec.model.IngredientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@AllArgsConstructor
public class IngredientsController implements IngredientsApi {
    private final RecipeService recipeService;

    @Override
    public ResponseEntity<List<IngredientDTO>> listIngredients() {
        return ok(recipeService.findAllIngredientNames());
    }
}
