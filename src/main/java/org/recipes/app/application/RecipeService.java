package org.recipes.app.application;

import lombok.AllArgsConstructor;
import org.recipes.app.application.mappers.RecipeMapper;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.RecipeType;
import org.recipes.app.domain.repositories.RecipeRepository;
import org.recipes.app.domain.repositories.RecipeSearch;
import org.recipes.spec.model.RecipeDTO;
import org.recipes.spec.model.RecipeTypeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static java.util.Optional.ofNullable;

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
        recipeMapper.toEntity(dto, recipe);
        recipeRepository.save(recipe);
        return recipeMapper.toDTO(recipe);
    }

    public void remove(UUID id) {
        recipeRepository.delete(findById(id));
    }

    private Recipe findById(UUID id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recipe not found"));
    }

    public List<RecipeDTO> search(RecipeSearch recipeSearch) {
        return recipeMapper.toDTOList(recipeRepository.search(recipeSearch));
    }
}
