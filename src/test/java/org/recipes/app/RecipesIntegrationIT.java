package org.recipes.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.RecipeIngredient;
import org.recipes.app.domain.RecipeType;
import org.recipes.app.domain.UnitOfMeasure;
import org.recipes.app.domain.repositories.RecipeRepository;
import org.recipes.spec.model.RecipeDTO;
import org.recipes.spec.model.RecipeIngredientDTO;
import org.recipes.spec.model.RecipeTypeDTO;
import org.recipes.spec.model.UnitOfMeasureDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RecipesIntegrationIT {
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
	void shouldCreateRecipe() throws Exception {
		mvc.perform(post("/recipes")
						.content(mapper.writeValueAsString(recipeDTO("d")))
						.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", equalTo("d")));
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

	private Recipe recipe(String name) {
		return new Recipe()
				.name(name)
				.recipeType(RecipeType.NON_VEGETARIAN)
				.servings(8)
				.ingredients(List.of(
						new RecipeIngredient().name("Flour")
								.quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasure.CUPS),
						new RecipeIngredient().name("Sugar")
								.quantity(BigDecimal.valueOf(1.0)).unitOfMeasure(UnitOfMeasure.CUPS),
						new RecipeIngredient().name("Baking soda")
								.quantity(BigDecimal.valueOf(0.5)).unitOfMeasure(UnitOfMeasure.TEASPOON),
						new RecipeIngredient().name("Eggs")
								.quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasure.ITEM)
				))
				.instructions("Preheat oven to 350°. In a large bowl, stir together flour, sugar, baking soda and salt. In another bowl, combine the eggs, bananas, oil, buttermilk and vanilla; add to flour mixture, stirring just until combined. Fold in nuts.\n" +
						"Pour into a greased or parchment-lined 9x5-in. loaf pan. If desired, sprinkle with additional walnuts. Bake until a toothpick comes out clean, 1-1/4 to 1-1/2 hours. Cool in pan for 15 minutes before removing to a wire rack.");
	}


	private RecipeDTO recipeDTO(String name) {
		return new RecipeDTO()
				.name(name)
				.recipeType(RecipeTypeDTO.NON_VEGETARIAN)
				.servings(8)
				.ingredients(List.of(
						new RecipeIngredientDTO().name("Flour")
								.quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasureDTO.CUPS),
						new RecipeIngredientDTO().name("Sugar")
								.quantity(BigDecimal.valueOf(1.0)).unitOfMeasure(UnitOfMeasureDTO.CUPS),
						new RecipeIngredientDTO().name("Baking soda")
								.quantity(BigDecimal.valueOf(0.5)).unitOfMeasure(UnitOfMeasureDTO.TEASPOON),
						new RecipeIngredientDTO().name("Eggs")
								.quantity(BigDecimal.valueOf(2.0)).unitOfMeasure(UnitOfMeasureDTO.ITEM)
				))
				.instructions("Preheat oven to 350°. In a large bowl, stir together flour, sugar, baking soda and salt. In another bowl, combine the eggs, bananas, oil, buttermilk and vanilla; add to flour mixture, stirring just until combined. Fold in nuts.\n" +
						"Pour into a greased or parchment-lined 9x5-in. loaf pan. If desired, sprinkle with additional walnuts. Bake until a toothpick comes out clean, 1-1/4 to 1-1/2 hours. Cool in pan for 15 minutes before removing to a wire rack.");
	}

}
