# ADR-0007: No Resilience4j in this scaffold

Status: Accepted

## Context

The companion [`java-hexagonal-architecture-scaffold`](https://github.com/eliangilsierra/java-hexagonal-architecture-scaffold)'s flagship wraps its SNS publish call in Resilience4j (retry + circuit breaker) because that call talks to an external broker that can fail independently of this application. This scaffold's flagship was evaluated for the same addition and deliberately does not get it.

## Decision

Do not add Resilience4j here. Every dependency this service has (H2/Postgres via Spring Data JPA) is either fully local or already covered by the JDBC driver's own connection handling — there is no outbound network call to a separate, independently-failing system for a retry/circuit-breaker to protect.

## Alternatives considered

- **Add it anyway, wrapped around the repository calls, for symmetry with the hexagonal repo** — rejected: wrapping a local H2/Postgres call in a circuit breaker with no real independent failure mode to protect against would be decoration, not a decision. This ADR exists specifically to make that reasoning explicit rather than silently doing less than the sibling repo for no stated reason.

## Consequences

Gains: no dependency, no configuration, no behavior added that doesn't correspond to an actual risk in this specific service.
Costs: none — this is the correct call for what this scaffold actually does. If a later version of this scaffold added a call to an external service (a payment gateway, a search index, another microservice), that would be the point to revisit this ADR.
