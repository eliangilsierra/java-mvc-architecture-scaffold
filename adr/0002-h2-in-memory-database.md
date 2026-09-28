# ADR-0002: H2 in-memory database instead of MySQL/Postgres

Status: Accepted

## Context

This is a reference/showcase repo, not a service meant to be deployed. Anyone reviewing it should be able to clone it and run it immediately.

## Decision

Use H2 in-memory (`spring.jpa.hibernate.ddl-auto: update`, seeded via `data.sql`) instead of a containerized MySQL/Postgres. No `docker-compose.yml` needed to run this project.

## Consequences

Gains: `mvn spring-boot:run` just works, no external dependency, no setup friction for someone browsing the code.
Costs: this is not representative of how the same architecture would be wired against a real database in production — swapping H2 for MySQL/Postgres only requires changing the `spring.datasource.*` properties and the JDBC driver dependency; nothing in `controller`/`service`/`repository` changes.
