package org.recipes.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class Recipe {
    @Id
    private UUID id = UUID.randomUUID();

    private String name;
    private RecipeType recipeType;
    private int servings;
    private String instructions;

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe ingredients(List<Ingredient> ingredients) {
        Objects.requireNonNull(ingredients, "ingredients must not be null");
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        this.ingredients.forEach(i -> i.recipe(this));
        return this;
    }

    public List<Ingredient> ingredients() {
        return Collections.unmodifiableList(ingredients);
    }
}
