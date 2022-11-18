package org.recipes.app.application.mappers;

import lombok.AllArgsConstructor;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.RecipeIngredient;
import org.recipes.app.domain.RecipeType;
import org.recipes.app.domain.UnitOfMeasure;
import org.recipes.spec.model.IngredientDTO;
import org.recipes.spec.model.RecipeDTO;
import org.recipes.spec.model.RecipeIngredientDTO;
import org.recipes.spec.model.RecipeTypeDTO;
import org.recipes.spec.model.UnitOfMeasureDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@AllArgsConstructor
public class RecipeMapper {

    public Recipe toEntity(RecipeDTO dto) {
        Recipe r = new Recipe();
        updateEntity(dto, r);
        return r;
    }

    public void updateEntity(RecipeDTO dto, Recipe r) {
        r.name(dto.getName())
                .recipeType(ofNullable(dto.getRecipeType())
                        .map(RecipeTypeDTO::getValue).map(RecipeType::valueOf).orElse(null))
                .servings(dto.getServings())
                .instructions(dto.getInstructions())
                .ingredients(toEntityList(dto.getIngredients()));
    }

    public RecipeDTO toDTO(Recipe r) {
        return new RecipeDTO()
            .id(r.id())
            .name(r.name())
            .recipeType(ofNullable(r.recipeType())
                .map(RecipeType::name).map(RecipeTypeDTO::valueOf).orElse(null))
            .servings(r.servings())
            .instructions(r.instructions())
            .ingredients(toRecipeIngredientDTOList(r.ingredients()));
    }

    private List<RecipeIngredientDTO> toRecipeIngredientDTOList(List<RecipeIngredient> ingredients) {
        return ingredients == null ? Collections.emptyList() :
                ingredients.stream().map(this::toDTO).toList();
    }

    private RecipeIngredientDTO toDTO(RecipeIngredient i) {
        return new RecipeIngredientDTO()
            .name(i.name())
            .quantity(i.quantity())
            .unitOfMeasure(ofNullable(i.unitOfMeasure())
                        .map(UnitOfMeasure::name).map(UnitOfMeasureDTO::valueOf).orElse(null));
    }

    private List<RecipeIngredient> toEntityList(List<RecipeIngredientDTO> ingredients) {
        return ingredients == null ? Collections.emptyList() :
                ingredients.stream().map(this::toEntity).toList();
    }

    private RecipeIngredient toEntity(RecipeIngredientDTO dto) {
        return new RecipeIngredient()
            .name(dto.getName())
            .quantity(dto.getQuantity())
            .unitOfMeasure(ofNullable(dto.getUnitOfMeasure())
                .map(UnitOfMeasureDTO::name).map(UnitOfMeasure::valueOf).orElse(null));
    }

    public List<RecipeDTO> toDTOList(List<Recipe> recipes) {
        return recipes == null ? Collections.emptyList() :
                recipes.stream().map(this::toDTO).toList();
    }

    public List<IngredientDTO> toIngredientNameDTO(List<String> names) {
        return names.stream()
                .map(name -> new IngredientDTO().name(name)).toList();
    }
}
