# java-mvc-architecture-scaffold

Reference scaffold: a classic **layered (MVC) architecture** in Spring Boot, meant to be read as much as run. Dummy domain (product catalog) on purpose — the point is the structure, not the business logic. The [`java17-baseline`](https://github.com/eliangilsierra/java-mvc-architecture-scaffold/tree/java17-baseline) branch holds this same architecture exactly as it was first built; [`java21-upgrade`](https://github.com/eliangilsierra/java-mvc-architecture-scaffold/tree/java21-upgrade) is this branch's parent — identical `src/`, Maven instead of Gradle. See [ADR-0005](adr/0005-migrate-build-to-gradle.md).

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

- Java 21 · Spring Boot 3.3.5 · **Gradle (Kotlin DSL)** — see [ADR-0005](adr/0005-migrate-build-to-gradle.md)
- Spring Data JPA + H2 in-memory for local dev (see [ADR-0002](adr/0002-h2-in-memory-database.md) for why not MySQL/Postgres) — Testcontainers (real Postgres) for integration tests, see [ADR-0004](adr/0004-testcontainers-for-integration-tests.md)
- `ProblemDetail` (RFC 7807) error responses — see [ADR-0003](adr/0003-problemdetail-error-responses.md)
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

Requires a local Gradle install (no wrapper is committed in this branch — see [ADR-0005](adr/0005-migrate-build-to-gradle.md)):

```bash
gradle bootRun
```

Or with Docker (builds with a pinned `gradle:8.10-jdk21` image, no local Gradle needed):

```bash
docker build -t mvc-architecture-scaffold .
docker run -p 8080:8080 mvc-architecture-scaffold
```

## Tests

```bash
gradle test
```

- `ProductServiceImplTest` unit-tests the service layer in isolation (mocked repositories, real generated mapper) — no Spring context, no database.
- `CategoryProductIntegrationTest` exercises the full HTTP flow against a real Postgres (Testcontainers). Requires Docker.

## Companion repo

[`java-hexagonal-architecture-scaffold`](https://github.com/eliangilsierra/java-hexagonal-architecture-scaffold) shows the same kind of reference implementation with a hexagonal (ports & adapters) architecture instead, for comparison.
