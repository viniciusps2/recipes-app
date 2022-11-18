create table if not exists recipe
(
    id           uuid    not null primary key,
    instructions text,
    name         varchar(255),
    recipe_type  integer,
    servings     integer not null
);

create table if not exists recipe_ingredient
(
    id              uuid not null primary key,
    name            varchar(255),
    quantity        numeric(19, 2),
    unit_of_measure integer,
    recipe_id       uuid not null
        constraint fk_recipe_ingredient_to_recipe
            references recipe
);
