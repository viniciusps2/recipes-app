package org.recipes.app.infrastructure.repositories;

import org.recipes.app.domain.Recipe;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

interface RecipeJpaRepository extends CrudRepository<Recipe, UUID>, JpaSpecificationExecutor<Recipe> {
}
