package org.recipes.app.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class RecipeIngredient {
    @Id
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Setter
    @ManyToOne
    @JoinColumn(nullable = false)
    private Recipe recipe;

    private String name;
    private BigDecimal quantity;
    private UnitOfMeasure unitOfMeasure;
}
