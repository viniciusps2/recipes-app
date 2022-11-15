package org.recipes.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class Ingredient {
    @Id
    private UUID id = UUID.randomUUID();

    @Setter
    @ManyToOne
    @JoinColumn(nullable = false)
    private Recipe recipe;

    private String name;
    private Double quantity;
    private UnitOfMeasure unitOfMeasure;
}
