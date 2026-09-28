# ADR-0008: GitHub Actions CI

Status: Accepted

## Context

None of this repo's other branches run tests automatically — every claim about a test passing has, until now, depended on someone actually running `mvn test`/`gradle test` locally.

## Decision

`.github/workflows/ci.yml` runs on every push/PR to `main` or `develop`: checks out the repo, sets up JDK 21, installs Gradle 8.10 via `gradle/actions/setup-gradle` (no wrapper is committed on this branch's build — see [ADR-0005](adr/0005-migrate-build-to-gradle.md) — so Gradle is installed rather than invoking `./gradlew`), and runs `gradle build`, which compiles, runs the unit tests, the `@DataJpaTest`/`@WebMvcTest` slices, and the Testcontainers integration test (GitHub-hosted runners have Docker available out of the box).

## Consequences

Gains: every push now gets an automatic, reproducible build+test run, including the real-container integration test.
Costs: without a committed wrapper, the exact Gradle version used to build this project is pinned in this workflow file rather than enforced by a wrapper — one more file to keep in sync.
