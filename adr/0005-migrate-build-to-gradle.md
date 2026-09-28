# ADR-0005: Migrate the build from Maven to Gradle

Status: Accepted

## Context

Every other branch of this repo uses Maven. This branch exists purely to demonstrate the other major JVM build tool — it is not trying to teach anything new about the architecture itself. Same reasoning as the equivalent branch in [`java-hexagonal-architecture-scaffold`](https://github.com/eliangilsierra/java-hexagonal-architecture-scaffold/blob/gradle-migration/adr/0008-migrate-build-to-gradle.md).

## Decision

Port `java21-upgrade` to Gradle (Kotlin DSL) with no changes to `src/` at all: same dependencies, same versions. The Spring Boot Gradle plugin's default `jar` task is disabled so only the Spring Boot fat jar (`bootJar`) is produced — without this, Gradle's default behavior also builds a dependency-less "plain" jar alongside it, which makes a Dockerfile's `*.jar` copy ambiguous.

## Consequences

Gains: a clean, isolated diff against `java21-upgrade` that touches only build files (`pom.xml` → `build.gradle.kts`/`settings.gradle.kts`, `Dockerfile`, `.gitignore`) — nothing under `src/` changed.
Costs: no Gradle wrapper (`gradlew`/`gradlew.bat`) is committed in this branch — generating one correctly requires running `gradle wrapper` with an actual Gradle installation, which wasn't available while building this branch. Anyone running this locally needs Gradle installed, or can use the Dockerfile, which builds with a pinned `gradle:8.10-jdk21` image instead.
