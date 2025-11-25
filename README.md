# Codegen Spring Boot Initializr — Hexagonal, Template‑Driven, Zero‑Boilerplate Project Generator

[![Build](https://github.com/bsayli/codegen-springboot-initializr/actions/workflows/build.yml/badge.svg)](https://github.com/bsayli/codegen-springboot-initializr/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/bsayli/codegen-springboot-initializr?logo=github\&label=release)](https://github.com/bsayli/codegen-springboot-initializr/releases/latest)
[![CodeQL](https://github.com/bsayli/codegen-springboot-initializr/actions/workflows/codeql.yml/badge.svg)](https://github.com/bsayli/codegen-springboot-initializr/actions/workflows/codeql.yml)
[![codecov](https://codecov.io/gh/bsayli/codegen-springboot-initializr/branch/refactor/hexagonal-architecture/graph/badge.svg)](https://codecov.io/gh/bsayli/codegen-springboot-initializr/tree/refactor/hexagonal-architecture)
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

**Codegen Spring Boot Initializr** is a **hexagonal, template‑driven generator** that produces production‑ready Spring Boot project skeletons with:

* Strongly validated domain blueprint
* Profile‑based artifact pipelines (e.g., `springboot-maven-java`)
* FreeMarker template rendering
* Fully isolated and tested ports/adapters
* Zero boilerplate, consistent project layouts

It aims to eliminate repetitive setup steps (pom.xml, `.gitignore`, `application.yml`, test scaffolding, package structure) by generating them automatically.

---

## 💡 Problem Statement

Bootstrapping a new Spring Boot project often means:

* Creating folder structures by hand
* Copy‑pasting `pom.xml`, `.gitignore`, config files
* Writing the same starter and test classes repeatedly
* Maintaining consistency across multiple services

This leads to **time loss**, **inconsistencies**, and **onboarding friction**.

---

## 💡 Solution

This project provides:

* **Hexagonal core** — domain‑first, framework‑agnostic
* **Template‑driven artifact generation** via FreeMarker
* **Strictly validated domain blueprint** (name, groupId, artifactId, package, dependencies)
* **Profile‑based pipelines** — e.g. Spring Boot + Maven + Java 21
* **Full test coverage:** unit + integration
* **GitHub Actions** with CodeQL, JaCoCo, Codecov

Planned for 1.0.0:

* **CLI inbound adapter** — generate projects via command line
* **REST inbound adapter** — generate via HTTP POST

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
[https://github.com/bsayli/codegen-springboot-initializr](https://github.com/bsayli/codegen-springboot-initializr)

---

## 🛡 License

Licensed under **MIT** — see [LICENSE](LICENSE).

---

**Author:** Barış Saylı
GitHub: [https://github.com/bsayli](https://github.com/bsayli)
