package org.recipes.app.infrastructure.repositories;

import lombok.AllArgsConstructor;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.repositories.RecipeRepository;
import org.recipes.app.domain.repositories.RecipeSearch;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RecipeDataRepository implements RecipeRepository {
    private final RecipeJpaRepository recipeJpaRepository;

    @Override
    public Recipe save(Recipe recipe) {
        return recipeJpaRepository.save(recipe);
    }

    @Override
    public Optional<Recipe> findById(UUID id) {
        return recipeJpaRepository.findById(id);
    }

    @Override
    public void delete(Recipe recipe) {
        recipeJpaRepository.delete(recipe);
    }

    @Override
    public List<Recipe> search(RecipeSearch search) {
        var searchSpec = new RecipeSearchSpecification(search);
        return recipeJpaRepository.findAll(searchSpec, Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public void saveAll(List<Recipe> recipes) {
        recipeJpaRepository.saveAll(recipes);
    }

    @Override
    public void deleteAll() {
        recipeJpaRepository.deleteAll();
    }

    @Override
    public List<String> findAllIngredientNames() {
        return recipeJpaRepository.findAllIngredientNames();
    }
}
