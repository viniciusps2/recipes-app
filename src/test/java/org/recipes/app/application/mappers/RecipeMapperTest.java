package org.recipes.app.application.mappers;

import org.junit.jupiter.api.Test;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.RecipeIngredient;
import org.recipes.app.domain.RecipeType;
import org.recipes.app.domain.UnitOfMeasure;
import org.recipes.spec.model.RecipeDTO;
import org.recipes.spec.model.RecipeIngredientDTO;
import org.recipes.spec.model.RecipeTypeDTO;
import org.recipes.spec.model.UnitOfMeasureDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeMapperTest {

    private final RecipeMapper recipeMapper = new RecipeMapper();

    @Test
    void shouldMapToDTO() {
        var id = UUID.randomUUID();
        RecipeDTO dto = recipeMapper.toDTO(recipe(id));
        assertThat(dto).usingRecursiveComparison().isEqualTo(recipeDTO(id));
    }

    @Test
    void shouldMapToDTOList() {
        var id = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var dtoList = recipeMapper.toDTOList(List.of(recipe(id), recipe(id2)));
        assertThat(dtoList).usingRecursiveComparison().isEqualTo(List.of(recipeDTO(id), recipeDTO(id2)));
    }

    @Test
    void shouldMapToEntity() {
        var id = UUID.randomUUID();
        var recipe = recipeMapper.toEntity(recipeDTO(id));
        assertThat(recipe).usingRecursiveComparison()
                .ignoringFields("id", "ingredients.id", "ingredients.recipe.id")
                .isEqualTo(recipe(id));
    }

    @Test
    void shouldMapToExistingEntity() {
        var id = UUID.randomUUID();
        var dto = recipeDTO(id);
        var recipe = recipe(id);
        recipeMapper.toEntity(dto, recipe);
        assertThat(recipe).usingRecursiveComparison()
                .ignoringFields("ingredients.id", "ingredients.recipe.id")
                .isEqualTo(recipe(id));
    }

    private Recipe recipe(UUID id) {
        return new Recipe(id)
                .name("name")
                .recipeType(RecipeType.NON_VEGETARIAN)
                .servings(8)
                .ingredients(List.of(
                        new RecipeIngredient().name("Flour")
                                .quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasure.CUPS),
                        new RecipeIngredient().name("Sugar")
                                .quantity(BigDecimal.valueOf(1.0)).unitOfMeasure(UnitOfMeasure.CUPS),
                        new RecipeIngredient().name("Baking soda")
                                .quantity(BigDecimal.valueOf(0.5)).unitOfMeasure(UnitOfMeasure.TEASPOON),
                        new RecipeIngredient().name("Eggs")
                                .quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasure.ITEM)
                ))
                .instructions("Preheat oven to 350°. In a large bowl, stir together flour, sugar, baking soda and salt. In another bowl, combine the eggs, bananas, oil, buttermilk and vanilla; add to flour mixture, stirring just until combined. Fold in nuts.\n" +
                        "Pour into a greased or parchment-lined 9x5-in. loaf pan. If desired, sprinkle with additional walnuts. Bake until a toothpick comes out clean, 1-1/4 to 1-1/2 hours. Cool in pan for 15 minutes before removing to a wire rack.");
    }

    private RecipeDTO recipeDTO(UUID id) {
        return new RecipeDTO()
                .id(id)
                .name("name")
                .recipeType(RecipeTypeDTO.NON_VEGETARIAN)
                .servings(8)
                .ingredients(List.of(
                        new RecipeIngredientDTO().name("Flour")
                                .quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasureDTO.CUPS),
                        new RecipeIngredientDTO().name("Sugar")
                                .quantity(BigDecimal.valueOf(1.0)).unitOfMeasure(UnitOfMeasureDTO.CUPS),
                        new RecipeIngredientDTO().name("Baking soda")
                                .quantity(BigDecimal.valueOf(0.5)).unitOfMeasure(UnitOfMeasureDTO.TEASPOON),
                        new RecipeIngredientDTO().name("Eggs")
                                .quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasureDTO.ITEM)
                ))
                .instructions("Preheat oven to 350°. In a large bowl, stir together flour, sugar, baking soda and salt. In another bowl, combine the eggs, bananas, oil, buttermilk and vanilla; add to flour mixture, stirring just until combined. Fold in nuts.\n" +
                        "Pour into a greased or parchment-lined 9x5-in. loaf pan. If desired, sprinkle with additional walnuts. Bake until a toothpick comes out clean, 1-1/4 to 1-1/2 hours. Cool in pan for 15 minutes before removing to a wire rack.");
    }

}