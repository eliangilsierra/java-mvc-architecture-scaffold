# java-mvc-architecture-scaffold

Reference scaffold: a classic **layered (MVC) architecture** in Spring Boot, meant to be read as much as run. Dummy domain (product catalog) on purpose — the point is the structure, not the business logic.

## Why each folder exists

| Package | Responsibility |
|---|---|
| `controller` | HTTP in/out only. No business logic — translates requests to service calls and results back to responses. |
| `dto/request`, `dto/response` | What crosses the HTTP boundary. Never the JPA entity itself — keeps the API contract independent of the persistence model. |
| `entity` | JPA-mapped persistence model. |
| `mapper` | MapStruct interfaces converting entity ↔ DTO. Generated at compile time — no hand-written mapping code to maintain. |
| `repository` | Spring Data JPA interfaces (`IXxxRepository`). |
| `service` / `service/impl` | Business rules and orchestration. Defined as an interface + a single implementation on purpose — see [ADR-0001](adr/0001-layered-architecture.md). |
| `exception` | Domain exceptions + one `@RestControllerAdvice` that is the single place deciding HTTP status codes. |
| `config` | Cross-cutting configuration (OpenAPI here). |

## Stack

- Java 17 · Spring Boot 3.2.4 · Maven
- Spring Data JPA + H2 in-memory (see [ADR-0002](adr/0002-h2-in-memory-database.md) for why not MySQL/Postgres)
- MapStruct · Bean Validation · springdoc-openapi

## Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/categories` | Create a category |
| `GET` | `/categories` | List categories |
| `GET` | `/categories/{id}` | Get a category |
| `POST` | `/products` | Create a product |
| `GET` | `/products?categoryId=` | List products (optionally by category) |
| `GET` | `/products/{id}` | Get a product |
| `PUT` | `/products/{id}` | Update a product |
| `DELETE` | `/products/{id}` | Delete a product |

Seed data (`data.sql`): categories `Electronics`/`Office Supplies`, three sample products.

OpenAPI UI: `/swagger-ui.html`. H2 console: `/h2-console` (JDBC URL `jdbc:h2:mem:catalog`).

## Run locally

```bash
mvn spring-boot:run
```

Or with Docker:

```bash
docker build -t mvc-architecture-scaffold .
docker run -p 8080:8080 mvc-architecture-scaffold
```

## Tests

```bash
mvn test
```

`ProductServiceImplTest` unit-tests the service layer in isolation (mocked repositories, real generated mapper) — no Spring context, no database.

## Companion repo

[`java-hexagonal-architecture-scaffold`](https://github.com/eliangilsierra/java-hexagonal-architecture-scaffold) shows the same kind of reference implementation with a hexagonal (ports & adapters) architecture instead, for comparison.
