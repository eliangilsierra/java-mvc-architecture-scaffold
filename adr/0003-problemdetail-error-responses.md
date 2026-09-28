# ADR-0003: Use ProblemDetail (RFC 7807) for error responses

Status: Accepted

## Context

`java17-baseline`'s `GlobalExceptionHandler` built its own `Map<String, Object>` error body by hand (`timestamp`, `status`, `error`, `message`). It works, but it's boilerplate Spring has had a built-in, standard answer for since Spring 6 / Boot 3: `ProblemDetail`, implementing [RFC 7807](https://www.rfc-editor.org/rfc/rfc7807).

## Decision

Every `@ExceptionHandler` in `GlobalExceptionHandler` now returns `ProblemDetail` via `ProblemDetail.forStatusAndDetail(status, message)` instead of a hand-built map.

## Alternatives considered

- **Keep the hand-rolled body** — rejected: it's equivalent boilerplate that Spring now provides for free, with a standard shape other tools (API gateways, client SDKs) already know how to parse.

## Consequences

Gains: less code in `GlobalExceptionHandler`, a response shape that follows a published standard instead of an ad hoc one.
Costs: the exact JSON field names changed (`detail` instead of `message`, `title` instead of `error`) — any client written against `java17-baseline`'s shape would need updating. Acceptable here since this is a from-scratch reference scaffold, not a versioned public API with real consumers.
