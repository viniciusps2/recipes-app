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
}