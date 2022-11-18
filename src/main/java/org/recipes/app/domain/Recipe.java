package org.recipes.app.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class Recipe {
    @Id
    @Setter(AccessLevel.NONE)
    private final UUID id;

    private String name;
    private RecipeType recipeType;
    private int servings;

    @Column(columnDefinition = "text")
    private String instructions;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe() {
         id = UUID.randomUUID();
    }

    public Recipe(UUID id) {
        this.id = id;
    }

    public Recipe ingredients(List<RecipeIngredient> ingredients) {
        this.ingredients.clear();
        if (ingredients == null) {
            return this;
        }
        this.ingredients.addAll(ingredients);
        this.ingredients.forEach(i -> i.recipe(this));
        return this;
    }

    public List<RecipeIngredient> ingredients() {
        return Collections.unmodifiableList(ingredients);
    }
}
