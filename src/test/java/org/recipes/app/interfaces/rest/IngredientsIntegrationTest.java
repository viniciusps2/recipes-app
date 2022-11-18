package org.recipes.app.interfaces.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.recipes.app.interfaces.rest.RecipeFixture.recipe;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IngredientsIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecipeRepository recipeRepository;

    private final List<Recipe> savedRecipes = List.of(
            recipe("a"),
            recipe("b"),
            recipe("a"));

    @BeforeEach
    void setUp() {
        recipeRepository.saveAll(savedRecipes);
    }

    @AfterEach
    void tearDown() {
        recipeRepository.deleteAll();
    }

    @Test
    void shouldListIngredients() throws Exception {
        mvc.perform(get("/ingredients")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }
}
