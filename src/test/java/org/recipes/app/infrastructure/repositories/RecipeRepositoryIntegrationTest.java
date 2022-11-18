package org.recipes.app.infrastructure.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.RecipeIngredient;
import org.recipes.app.domain.RecipeType;
import org.recipes.app.domain.UnitOfMeasure;
import org.recipes.app.domain.repositories.RecipeSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class RecipeRepositoryIntegrationTest {
    @Autowired
    private RecipeDataRepository repository;

    private final List<Recipe> savedRecipes = List.of(
            new Recipe().name("A")
                    .servings(1)
                    .instructions("Preheat oven to 200")
                    .recipeType(RecipeType.NON_VEGETARIAN)
                    .ingredients(List.of(
                            new RecipeIngredient().name("One")
                                    .quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasure.CUPS),
                            new RecipeIngredient().name("Two")
                                    .quantity(BigDecimal.valueOf(1.0)).unitOfMeasure(UnitOfMeasure.CUPS)
                    )),
            new Recipe().name("B")
                    .servings(2)
                    .instructions("Preheat oven to 220")
                    .recipeType(RecipeType.VEGETARIAN)
                    .ingredients(List.of(
                            new RecipeIngredient().name("One")
                                    .quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasure.CUPS),
                            new RecipeIngredient().name("Three")
                                    .quantity(BigDecimal.valueOf(1.0)).unitOfMeasure(UnitOfMeasure.CUPS)
                    ))
    );

    @BeforeEach
    void setUp() {
        repository.saveAll(savedRecipes);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("ingredientsIncludedExpected")
    void shouldMatchIngredientsIncluded(List<String> expected, List<String> ingredientsIncludes) {
        RecipeSearch search = RecipeSearch.builder().ingredientsIncludes(ingredientsIncludes).build();
        List<Recipe> result = repository.search(search);
        assertEquals(expected, result.stream().map(Recipe::name).collect(toList()),
                String.format("When including %s the result %s is expected", ingredientsIncludes, expected));
    }

    static List<Arguments> ingredientsIncludedExpected() {
        return List.of(
                Arguments.of(List.of("A", "B"), List.of("one")),
                Arguments.of(List.of("A"), List.of("one", "two")),
                Arguments.of(List.of("A"), List.of("two", "one")),
                Arguments.of(List.of("A", "B"), List.of("one")),
                Arguments.of(List.of("B"), List.of("one", "three")),
                Arguments.of(List.of("B"), List.of("three")),
                Arguments.of(List.of(), List.of("something else"))
        );
    }


    @ParameterizedTest
    @MethodSource("ingredientsExcludedExpected")
    void shouldMatchIngredientsExcluded(List<String> expected, List<String> ingredientsExcludes) {
        RecipeSearch search = RecipeSearch.builder().ingredientsExcludes(ingredientsExcludes).build();
        List<Recipe> result = repository.search(search);
        assertEquals(expected, result.stream().map(Recipe::name).collect(toList()),
                String.format("When excluding %s the result %s is expected", ingredientsExcludes, expected));
    }

    static List<Arguments> ingredientsExcludedExpected() {
        return List.of(
                Arguments.of(List.of("A", "B"), List.of("something else")),
                Arguments.of(List.of(), List.of("one")),
                Arguments.of(List.of("B"), List.of("two")),
                Arguments.of(List.of("A"), List.of("three"))
        );
    }

    @ParameterizedTest
    @MethodSource("recipeTypeResultsExpected")
    void shouldMatchRecipeType(List<String> expected, RecipeType recipeType) {
        RecipeSearch search = RecipeSearch.builder().recipeType(recipeType).build();
        List<Recipe> result = repository.search(search);
        assertEquals(expected, result.stream().map(Recipe::name).collect(toList()),
                String.format("When searching by %s the result %s is expected", recipeType, expected));
    }

    static List<Arguments> recipeTypeResultsExpected() {
        return List.of(
                Arguments.of(List.of("A"), RecipeType.NON_VEGETARIAN),
                Arguments.of(List.of("B"), RecipeType.VEGETARIAN),
                Arguments.of(List.of("A", "B"), null)
        );
    }

    @ParameterizedTest
    @MethodSource("instructionsResultsExpected")
    void shouldMatchInstructions(List<String> expected, String instructions) {
        RecipeSearch search = RecipeSearch.builder().instructionsContains(instructions).build();
        List<Recipe> result = repository.search(search);
        assertEquals(expected, result.stream().map(Recipe::name).collect(toList()),
                String.format("When searching by %s the result %s is expected", instructions, expected));
    }

    static List<Arguments> instructionsResultsExpected() {
        return List.of(
                Arguments.of(List.of("A", "B"), "  oven"),
                Arguments.of(List.of("A", "B"), ""),
                Arguments.of(List.of("A", "B"), "   "),
                Arguments.of(List.of("A", "B"), null),
                Arguments.of(List.of(), "something else"),
                Arguments.of(List.of("A"), " oven to 200 "),
                Arguments.of(List.of("B"), "   oven to 220 ")
        );
    }


    @ParameterizedTest
    @MethodSource("servingsResultsExpected")
    void shouldMatchServings(List<String> expected, Integer servings) {
        RecipeSearch search = RecipeSearch.builder().servings(servings).build();
        List<Recipe> result = repository.search(search);
        assertEquals(expected, result.stream().map(Recipe::name).collect(toList()),
                String.format("When searching by %s the result %s is expected", servings, expected));
    }

    static List<Arguments> servingsResultsExpected() {
        return List.of(
                Arguments.of(List.of("A", "B"), null),
                Arguments.of(List.of("A"), 1),
                Arguments.of(List.of("B"), 2)
        );
    }

    @Test
    void shouldListAllIngredientNames() {
        var result = repository.findAllIngredientNames();
        assertEquals(List.of("One", "Three", "Two"), result);
    }
}