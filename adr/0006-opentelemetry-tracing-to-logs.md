# ADR-0006: OpenTelemetry tracing, exported to logs

Status: Accepted

## Context

None of this repo's other branches have any request tracing — correlating log lines for a single request across `controller` → `service` → `repository` by eye works fine for this scaffold's size, but it's the same gap a larger version of this same layered architecture would eventually need to close.

## Decision

Add `micrometer-tracing-bridge-otel` + `opentelemetry-exporter-logging`, and put `traceId`/`spanId` into every log line's format (`logging.pattern.level` in `application.yml`). Spring Boot auto-instruments the HTTP request, so a single `traceId` ties together every log line produced while handling one request.

## Alternatives considered

- **A real tracing backend (Jaeger, Zipkin, Tempo) via an OTLP exporter** — rejected for this scaffold: it would mean another container just to look at spans, for a repo whose job is to demonstrate architecture. Swapping the logging exporter for an OTLP one pointed at a real collector is a one-dependency, one-property change, deliberately left as the obvious next step.

## Consequences

Gains: `grep <traceId>` across logs reconstructs everything that happened while handling one request, with zero additional infrastructure to run.
Costs: this is a stand-in for real distributed tracing — no visualization, no span timing charts. Real production tracing needs a real backend.
