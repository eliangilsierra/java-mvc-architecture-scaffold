# ADR-0001: Classic layered (MVC) architecture

Status: Accepted

## Context

This repo exists to show a clean, conventional layered architecture in Spring Boot — the style most teams actually use for services whose business logic is simple CRUD plus a handful of rules, where the ceremony of ports/adapters (see [`java-hexagonal-architecture-scaffold`](https://github.com/eliangilsierra/java-hexagonal-architecture-scaffold)) wouldn't pay for itself.

## Decision

`controller` → `service` (interface) → `service.impl` → `repository` (interface, Spring Data JPA), with `dto`, `entity`, `mapper` (MapStruct) and `exception` as supporting packages. Every repository and service is defined as an interface (`IXxxRepository`, `IXxxService`) implemented by a single concrete class — not because multiple implementations are expected, but because depending on an interface (not a concrete class) keeps controllers/services trivially mockable in tests and keeps the door open if a second implementation ever does show up.

## Consequences

Gains: obvious to navigate for anyone who has touched a Spring Boot app before; low ceremony; fast to extend with a new CRUD resource.
Costs: the `I`-prefixed interface for a service with exactly one implementation is arguably YAGNI in the strictest sense — it's a deliberate, opinionated trade-off favoring testability and consistency over minimalism, not a mistake.
