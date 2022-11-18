package org.recipes.app.application;

import lombok.AllArgsConstructor;
import org.recipes.app.application.errors.NotFoundException;
import org.recipes.app.application.mappers.RecipeMapper;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.repositories.RecipeRepository;
import org.recipes.app.domain.repositories.RecipeSearch;
import org.recipes.spec.model.IngredientDTO;
import org.recipes.spec.model.RecipeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RecipeService {
    private final RecipeMapper recipeMapper;
    private final RecipeRepository recipeRepository;

    public RecipeDTO create(RecipeDTO dto) {
        var recipe = recipeMapper.toEntity(dto);
        recipeRepository.save(recipe);
        return recipeMapper.toDTO(recipe);
    }

    public RecipeDTO getRecipe(UUID id) {
        var recipe = findById(id);
        return recipeMapper.toDTO(recipe);
    }

    @Transactional
    public RecipeDTO update(UUID id, RecipeDTO dto) {
        var recipe = findById(id);
        recipeMapper.updateEntity(dto, recipe);
        recipeRepository.save(recipe);
        return recipeMapper.toDTO(recipe);
    }

    public void remove(UUID id) {
        recipeRepository.delete(findById(id));
    }

    private Recipe findById(UUID id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));
    }

    public List<RecipeDTO> search(RecipeSearch recipeSearch) {
        return recipeMapper.toDTOList(recipeRepository.search(recipeSearch));
    }

    public List<IngredientDTO> findAllIngredientNames() {
        return recipeMapper.toIngredientNameDTO(recipeRepository.findAllIngredientNames());
    }
}
