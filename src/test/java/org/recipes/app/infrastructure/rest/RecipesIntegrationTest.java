package org.recipes.app.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.recipes.app.infrastructure.rest.RecipeFixture.recipe;
import static org.recipes.app.infrastructure.rest.RecipeFixture.recipeDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RecipesIntegrationTest {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private ObjectMapper mapper;

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
	void shouldGetRecipes() throws Exception {
		mvc.perform(get("/recipes")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	void shouldGetOneRecipe() throws Exception {
		UUID id = savedRecipes.get(0).id();
		mvc.perform(get("/recipes/" + id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(id.toString())));
	}

	@Test
	void whenRecipeNotFoundShouldFail() throws Exception {
		mvc.perform(get("/recipes/" + UUID.randomUUID())).andExpect(status().isNotFound());
	}

	@Test
	void shouldCreateRecipe() throws Exception {
		mvc.perform(post("/recipes")
						.content(mapper.writeValueAsString(recipeDTO("d")))
						.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", equalTo("d")));
	}

	@Test
	void whenCreatingRecipeAndFieldsAreMissingShouldFail() throws Exception {
		mvc.perform(post("/recipes")
						.content(mapper.writeValueAsString(recipeDTO(null)))
						.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isBadRequest());
	}
	@Test
	void shouldUpdateRecipe() throws Exception {
		UUID id = savedRecipes.get(0).id();
		String newName = "new name";
		mvc.perform(put("/recipes/" + id)
						.content(mapper.writeValueAsString(recipeDTO(newName)))
						.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", equalTo(newName)));

		mvc.perform(get("/recipes/" + id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", equalTo(newName)));
	}

	@Test
	void whenUpdatingRecipeAndFieldsAreMissingShouldFail() throws Exception {
		UUID id = savedRecipes.get(0).id();
		mvc.perform(put("/recipes/" + id)
						.content(mapper.writeValueAsString(recipeDTO(null)))
						.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isBadRequest());
	}


}
