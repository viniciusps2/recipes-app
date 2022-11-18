package org.recipes.app.infrastructure.repositories;

import org.recipes.app.domain.Recipe;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

interface RecipeJpaRepository extends CrudRepository<Recipe, UUID>, JpaSpecificationExecutor<Recipe> {
    @Query("SELECT DISTINCT i.name FROM Recipe r JOIN r.ingredients i ORDER BY i.name ASC")
    List<String> findAllIngredientNames();
}
