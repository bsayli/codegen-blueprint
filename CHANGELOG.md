# Changelog

All notable changes to this project will be documented in this file.

This project follows a **contract-first** release discipline:

* **MAJOR** — contract reset
* **MINOR** — backward-compatible expansion
* **PATCH** — bug fixes only

For details, see: `docs/policies/release-discipline.md`.

---

## [1.0.0] - 2026-01-05

Codegen Blueprint **1.0.0 GA** marks the point where this repository becomes a **product contract** — not an experiment.

### Highlights

* **Executable proof included** — reproducible **GREEN → RED → GREEN** validation using **build-time guardrails only**.
* **Architecture guardrails (opt-in)** — generated **ArchUnit** tests that fail **deterministically** during `mvn verify`.
* **Two architecture models (opt-in)** — `hexagonal` (ports & adapters) and `standard` (layered).
* **Buildable-by-default output** — generated projects pass **`mvn verify`** immediately after generation.
* **Explicit GA contract docs** — guarantees, release discipline, and non-goals are written as authoritative policy.

### GA profile and compatibility

* Active GA profile: `springboot-maven-java`
* Compatibility targets:

    * **Java 21**
    * **Spring Boot 3.4–3.5** (default: **3.5**)
    * **Maven 3.9+**

### Output contract (generated project)

Generated projects are **single-module** and include:

* `pom.xml` (with Maven Wrapper)
    * `.mvn/wrapper/maven-wrapper.properties`
    * `mvnw`
    * `mvnw.cmd`
* `src/main/java/<basePackage>/...`
* `src/test/java/<basePackage>/...`
* `src/main/resources/application.yml`
* `.gitignore`
* `README.md` (part of the generated contract surface)


### References

* Full release notes: [Release Notes — Codegen Blueprint 1.0.0 GA](docs/releases/1.0.0-ga.md)
* Binding contract: [Executable Architecture Contract](docs/architecture/executable-architecture-contract.md)
* Guardrails semantics: [Architecture Guardrails Rulebook](docs/architecture/architecture-guardrails-rulebook.md)
* Reproducible demo: [Executable Architecture Proof (GREEN → RED → GREEN)](docs/demo/executable-architecture-proof.md)