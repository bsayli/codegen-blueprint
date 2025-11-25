# Codegen Blueprint — Hexagonal, Template‑Driven, Zero‑Boilerplate Project Generator

[![Build](https://github.com/bsayli/codegen-blueprint/actions/workflows/build.yml/badge.svg)](https://github.com/bsayli/codegen-blueprint/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/bsayli/codegen-blueprint?logo=github\&label=release)](https://github.com/bsayli/codegen-blueprint/releases/latest)
[![CodeQL](https://github.com/bsayli/codegen-blueprint/actions/workflows/codeql.yml/badge.svg)](https://github.com/bsayli/codegen-blueprint/actions/workflows/codeql.yml)
[![codecov](https://codecov.io/gh/bsayli/codegen-blueprint/branch/refactor/hexagonal-architecture/graph/badge.svg)](https://codecov.io/gh/bsayli/codegen-blueprint/tree/refactor/hexagonal-architecture)
[![Java](https://img.shields.io/badge/Java-21-red?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## ⚠ Project Status (1.0.0 Refactor Branch)

This README reflects the ongoing **hexagonal architecture rewrite** for version **1.0.0**.

The core domain, application layer, artifact pipeline, FreeMarker templating, CI/CD, and test suite are complete.

🔄 **Inbound adapters (CLI & REST)** are under active development and will land before the **1.0.0 GA release**.

---

## 🚀 Overview

**Codegen Blueprint** is a **hexagonal, template‑driven generator** designed to act as a flexible **blueprint engine**.

Rather than focusing on a single tech stack, it defines a stable, framework‑agnostic core that can generate project structures for *any* combination of:

* Framework (e.g., Spring Boot today, others later)
* Build tool (Maven, Gradle, …)
* Language (Java today, Kotlin later)
* Generation profile (customizable via configuration)

The first shipped profile is:

```
springboot-maven-java
```

This profile produces production‑ready Spring Boot project skeletons featuring:

* Strongly validated domain blueprint
* Profile‑based artifact pipelines
* FreeMarker template rendering
* Fully isolated and tested ports/adapters
* Zero boilerplate, consistent project layouts

Hexagonal architecture ensures new profiles can be added **without changing core logic**, simply by supplying new templates + profile configuration.

---

## 💡 Problem Statement

Teams often repeat the same setup steps when starting a new service or application:

* Creating the initial folder layout
* Writing or copying build files (`pom.xml`, `build.gradle`, etc.)
* Adding `.gitignore`, configuration files, starter classes, tests
* Maintaining consistency across dozens of projects and multiple stacks

Most internal tooling solves this per stack — for example:

* "Spring Boot + Maven + Java"
* "Kotlin + Gradle"

But the structural problem is always the same:

> Given a **blueprint** (name, identity, tech stack, dependencies),
> how can we generate a consistent, production‑grade project skeleton
> **without hard‑wiring ourselves to a single framework, build tool, or language?**

`codegen-blueprint` addresses this by acting as a **hexagonal, profile‑driven blueprint engine**.

---

## 💡 Solution

This project provides:

* **Hexagonal core** — stable, framework‑agnostic, domain‑first design
* **Template‑driven artifact generation** powered by FreeMarker
* **Strict domain validation** for names, groupId/artifactId, package, dependencies
* **Profile‑based pipelines** that define:

  * Template base paths
  * Ordered artifact keys
  * Artifact→template mappings
* **Fully replaceable adapters** (templating, filesystem, archiving)
* **Full test coverage** — unit + integration
* **CI/CD ready** — CodeQL, JaCoCo, Codecov, GitHub Actions

**1.0.0 planned inbound adapters:**

* CLI (command‑line project generation)
* REST (HTTP‑based project generation)

By separating *domain*, *application*, *ports*, and *adapters*, the engine can evolve to support:

* Kotlin
* Gradle
* Multi‑module project generation
* Alternative frameworks
* Organization‑specific generation profiles

…with no changes to core logic.

Each generation profile ships with its own outbound adapters and its own template set  
(e.g. `adapter.out.profile.springboot.maven.java` + `templates/springboot/maven/java/**`).  
The core engine never depends on any specific framework, build tool, or language.

---

## 🧩 Current Architecture (Hexagonal)

This generator follows a clean **ports & adapters** architecture.

**Domain Layer**

* `ProjectBlueprint` (aggregate root)
* Value Objects (`ProjectName`, `PackageName`, `GroupId`, etc.)
* Policies (naming, reserved words, dependency rules)
* Errors with i18n (`DomainViolationException`, etc.)

**Application Layer**

* Ports for artifact generation (`ProjectArtifactsPort`, `ArtifactPort`)
* Application services orchestrating project generation

**Adapter Layer**

* **Outbound:**

  * FreeMarker templating
  * Artifact adapters (`pom`, `.gitignore`, `application.yml`, scaffolder, README)
  * Profile selection: `springboot-maven-java`

* **Inbound:**

  * CLI (coming soon)
  * REST (coming soon)

**Bootstrap Layer**

* Spring Boot configuration
* Template loader
* Profile bindings

---

## 📦 Features (1.0.0 Core)

### ✅ Completed

* Hexagonal refactor
* Domain-driven blueprint & policies
* FreeMarker template rendering
* Profile-based artifact pipeline
* Integration test suite (`SpringBootTest` + Failsafe)
* Codecov integration
* CodeQL + Security scanning
* GitHub Actions (build + test)

### 🔄 In Progress

* CLI adapter (inbound)
* REST adapter (inbound)
* Additional profiles

---

## 🧪 Testing

Run all tests:

```bash
mvn verify
```

Test suite includes:

* Domain unit tests (policies, value objects, selectors)
* Adapter unit tests
* Integration tests verifying `springboot-maven-java` pipeline end‑to‑end
* JaCoCo + Codecov reporting

---

## 📂 Project Structure (Generated Output Example)

```text
my-app/
 ├── pom.xml
 ├── .gitignore
 ├── src/
 │   ├── main/java/.../MyAppApplication.java
 │   ├── main/resources/application.yml
 │   ├── test/java/.../MyAppApplicationTests.java
 │   └── gen/java/... (reserved for future generators)
```

---

## 🛣 Roadmap (Post‑1.0.0)

* Additional generation profiles (Kotlin, Gradle, multi‑module)
* Dockerfile & CI/CD template artifacts
* Pluggable dependency catalogs

---

## 📘 Contributing

Contributions and discussions are welcome.
Open issues or PRs at:
[https://github.com/bsayli/codegen-blueprint](https://github.com/bsayli/codegen-blueprint)

---

## 🛡 License

Licensed under **MIT** — see [LICENSE](LICENSE).

---

**Author:** Barış Saylı
GitHub: [https://github.com/bsayli](https://github.com/bsayli)
