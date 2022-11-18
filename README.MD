# Recipes App

Application that allows users to manage their recipes.

# Requirements

- JDK 17

# Running the application locally

Execute this command in your terminal to start Postgres
```
docker-compose up
```

In another terminal execute this command:
```
./gradlew bootRun
```

Open your brower on: http://localhost:8080

This is a Swagger page where you can call the API.

# Testing

Execute in your terminal: `./gradlew build`

## Project structure

### Clean Architecture

This project was created following a Clean Architecture aproach, where the code is organised in such a way that it encapsulates the business logic to isolate it from external factors.

The domain logic goes in the inner part, and should not depend on other layers. The main rule is: code on the inner layers can have no knowledge of functions on the outer layers.

The following layers was created:

- Domain layer: 
    - entities, and value objects to represent the business model with business logic
    - domain services.
    - repositories to access data (as interface only).

- Application layer: 
    - application services/use cases: orchestrates flow of data to and from domain layer.
    - mappers: maps DTO to and from Entity

- Infrastructure: 
    - repository implementations: accessing data from DB or external source.
    - configurations
    - rest controllers

### Open API

The application is making use of Open API for API specification and to generate API models that are used in the code base. (DTOs and Controllers Interface)

## API Design

The application provides the following endpoints:

- /recipes: create, update, delete and search recipes
- /ingredients: loads only ingredients names that can be used on recipes search 
