# ADR-0004: Testcontainers (real Postgres) for integration tests, H2 stays for local dev

Status: Accepted

## Context

`java17-baseline` runs entirely on H2 (see its ADR-0002) so the app starts with zero external dependencies — still the right default for `mvn spring-boot:run`. But H2's SQL dialect isn't identical to a real production database, and "all our tests pass on H2" has burned teams before when the real database behaves differently.

## Decision

Keep H2 as the default runtime datasource. Add `CategoryProductIntegrationTest`, which uses Testcontainers to run against a real, disposable Postgres container instead, overriding the datasource via `@DynamicPropertySource`. It exercises the full HTTP flow — create a category, create a product under it, fetch it back — and confirms a missing resource returns the new `ProblemDetail` shape from ADR-0003.

## Alternatives considered

- **Switch the whole app to Postgres, drop H2 entirely** — rejected for this scaffold specifically: it would mean `mvn spring-boot:run` needs Docker just to try the code out, which contradicts the "clone and run" goal from ADR-0002.

## Consequences

Gains: at least one test proves the app behaves correctly against the same kind of database it would run against in production, not just against H2.
Costs: running this one test requires Docker locally or in CI — everything else in this repo does not.
