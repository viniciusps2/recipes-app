package org.recipes.app.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recipes.app.application.mappers.RecipeMapper;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.RecipeIngredient;
import org.recipes.app.domain.RecipeType;
import org.recipes.app.domain.UnitOfMeasure;
import org.recipes.app.domain.repositories.RecipeRepository;
import org.recipes.app.domain.repositories.RecipeSearch;
import org.recipes.spec.model.RecipeDTO;
import org.recipes.spec.model.RecipeIngredientDTO;
import org.recipes.spec.model.RecipeTypeDTO;
import org.recipes.spec.model.UnitOfMeasureDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void shouldCreateAValidRecipe() {
        RecipeDTO dto = new RecipeDTO();
        Recipe recipe = new Recipe();
        RecipeDTO expected = new RecipeDTO();
        when(recipeMapper.toEntity(dto)).thenReturn(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(recipeMapper.toDTO(recipe)).thenReturn(expected);
        RecipeDTO result = recipeService.create(dto);
        assertEquals(expected, result);
    }

    @Test
    void whenRecipeIsPresentShouldSucceed() {
        UUID id = UUID.randomUUID();
        Recipe recipe = new Recipe();
        RecipeDTO expected = new RecipeDTO();
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(recipeMapper.toDTO(recipe)).thenReturn(expected);
        assertEquals(expected, recipeService.getRecipe(id));
    }

    @Test
    void whenRecipeIsNotPresentShouldThrow() {
        UUID id = UUID.randomUUID();
        when(recipeRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> recipeService.getRecipe(id));
    }

    @Test
    void shouldUpdateAnExistingRecipe() {
        UUID id = UUID.randomUUID();
        RecipeDTO dto = new RecipeDTO();
        Recipe recipe = new Recipe();
        RecipeDTO expected = new RecipeDTO();
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(recipeMapper.toDTO(recipe)).thenReturn(expected);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        RecipeDTO result = recipeService.update(id, dto);

        assertEquals(expected, result);
        verify(recipeMapper).toEntity(dto, recipe);
    }

    @Test
    void shouldRemoveRecipe() {
        UUID id = UUID.randomUUID();
        Recipe recipe = new Recipe();
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        recipeService.remove(id);
        verify(recipeRepository).delete(recipe);
    }

    @Test
    void shouldSearchRecipe() {
        List<Recipe> recipes = List.of(new Recipe());
        List<RecipeDTO> dtoList = List.of(new RecipeDTO());
        RecipeSearch search = recipeSearch();

        when(recipeRepository.search(search)).thenReturn(recipes);
        when(recipeMapper.toDTOList(recipes)).thenReturn(dtoList);
        List<RecipeDTO> result = recipeService.search(search);
        assertEquals(dtoList, result);
    }

    private static RecipeSearch recipeSearch() {
        return new RecipeSearch(RecipeType.NON_VEGETARIAN, null, List.of(), List.of(), "");
    }

    private static Recipe recipe() {
        return Recipe.builder()
                .name("Banana bread")
                .recipeType(RecipeType.NON_VEGETARIAN)
                .servings(8)
                .ingredients(asList(
                        RecipeIngredient.builder().name("All-purpose flour")
                                .quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasure.CUPS).build(),
                        RecipeIngredient.builder().name("Sugar")
                                .quantity(BigDecimal.valueOf(1.0)).unitOfMeasure(UnitOfMeasure.CUPS).build(),
                        RecipeIngredient.builder().name("Baking soda")
                                .quantity(BigDecimal.valueOf(0.5)).unitOfMeasure(UnitOfMeasure.TEASPOON).build(),
                        RecipeIngredient.builder().name("Eggs")
                                .quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasure.ITEM).build()
                ))
                .instructions("Preheat oven to 350°. In a large bowl, stir together flour, sugar, baking soda and salt. In another bowl, combine the eggs, bananas, oil, buttermilk and vanilla; add to flour mixture, stirring just until combined. Fold in nuts.\n" +
                        "Pour into a greased or parchment-lined 9x5-in. loaf pan. If desired, sprinkle with additional walnuts. Bake until a toothpick comes out clean, 1-1/4 to 1-1/2 hours. Cool in pan for 15 minutes before removing to a wire rack.")
                .build();
    }


    private static RecipeDTO recipeDTO() {
        return new RecipeDTO()
                .name("Banana bread")
                .recipeType(RecipeTypeDTO.NON_VEGETARIAN)
                .servings(8)
                .ingredients(asList(
                        new RecipeIngredientDTO().name("All-purpose flour")
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