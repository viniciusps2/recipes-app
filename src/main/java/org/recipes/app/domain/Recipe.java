package org.recipes.app.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Accessors(fluent = true, chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String name;
    private RecipeType recipeType;
    private int servings;
    private String instructions;

    @Builder.Default
    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public void ingredients(List<RecipeIngredient> ingredients) {
        this.ingredients.clear();
        if (ingredients == null) {
            return;
        }
        this.ingredients.addAll(ingredients);
        this.ingredients.forEach(i -> i.recipe(this));
    }

    public List<RecipeIngredient> ingredients() {
        return Collections.unmodifiableList(ingredients);
    }
}
