package org.recipes.app.infrastructure.repositories;

import lombok.AllArgsConstructor;
import org.recipes.app.domain.Recipe;
import org.recipes.app.domain.RecipeIngredient;
import org.recipes.app.domain.repositories.RecipeSearch;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@AllArgsConstructor
public class RecipeSearchSpecification implements Specification<Recipe> {
    private final RecipeSearch search;

    @Override
    public Predicate toPredicate(Root<Recipe> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
        return hasServings()
                .and(hasRecipeType())
                .and(ingredientsExcludes())
                .and(ingredientsIncludes())
                .and(instructionsContains())
                .toPredicate(r,q,cb);
    }

    private Specification<Recipe> hasServings() {
        return (r, q, cb) -> search.servings() == null ? null :
                cb.equal(r.get("servings"), search.servings());
    }

    private Specification<Recipe> hasRecipeType() {
        return (r, q, cb) -> search.recipeType() == null ? null :
                cb.equal(r.get("recipeType"), search.recipeType());
    }

    private Specification<Recipe> instructionsContains() {
        return (r, q, cb) -> isBlank(search.instructionsContains()) ? null :
                cb.like(cb.lower(r.get("instructions")), searchText(search.instructionsContains()));
    }

    private Specification<Recipe> ingredientsExcludes() {
        return (r, q, cb) -> {
            if (CollectionUtils.isEmpty(search.ingredientsExcludes())) {
                return null;
            }
            Subquery<Recipe> sq = ingredientSubQuery(q, cb, search.ingredientsExcludes());
            return cb.not(cb.in(r.get("id")).value(sq));
        };
    }

    private Specification<Recipe> ingredientsIncludes() {
        return (r, q, cb) -> {
            if (CollectionUtils.isEmpty(search.ingredientsIncludes())) {
                return null;
            }
            Subquery<Recipe> sq = ingredientSubQuery(q, cb, search.ingredientsIncludes());
            return r.in(sq);
        };
    }

    private Subquery<Recipe> ingredientSubQuery(CriteriaQuery<?> q, CriteriaBuilder cb, List<String> ingredientNames) {
        Subquery<Recipe> sq = q.subquery(Recipe.class);
        Root<Recipe> sqRoot = sq.from(Recipe.class);
        Join<Recipe, RecipeIngredient> sqIngredient = sqRoot.join("ingredients");
        long havingSize = ingredientNames.size() - 1L;
        sq.select(sqRoot.get("id"))
                .where(cb.lower(sqIngredient.get("name")).in(ingredientNames))
                .groupBy(sqRoot.get("id"))
                .having(cb.greaterThan(cb.count(sqRoot.get("id")), havingSize));
        return sq;
    }

    private String searchText(String text) {
        return "%" + text.toLowerCase() + "%";
    }
}
