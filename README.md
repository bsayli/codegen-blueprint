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

The core **domain**, **application layer**, **artifact pipeline**, **FreeMarker templating**, **CI/CD**, and **test suite** are complete.

🔄 **Inbound adapters (CLI & REST)** are under active development and will land before the **1.0.0 GA release**.

---

## 🚀 Overview

**Codegen Blueprint** is a **hexagonal, template‑driven generator** designed as a flexible **blueprint engine**.

Instead of supporting only one stack, it enables generating project structures for different combinations of:

* Frameworks (Spring Boot first, others later)
* Build tools (Maven now, Gradle later)
* Languages (Java now, Kotlin later)
* Profiles (fully configurable)

The first supported profile is:

```
springboot-maven-java
```

It produces production‑ready Spring Boot project skeletons featuring:

* Strictly validated domain blueprint
* **Profile‑defined artifact pipelines** (see ArtifactKeys)
* Fully isolated and tested **ports/adapters**
* Zero‑boilerplate, consistent project structures

Hexagonal architecture ensures new tech stacks can be added **without core changes**, only by supplying new
**templates + profile configuration + adapters**.

---

## 💡 Problem Statement

Engineering teams repeatedly perform the same manual setup when starting a new project:

* Create base structure
* Write/copy build files (`pom.xml`, `build.gradle`, ...)
* Configure `.gitignore`, `application.yml`, starter classes, test bootstrapping
* Ensure consistency across multiple services and teams

Most internal tools are limited to a **specific stack**, such as:

* Spring Boot + Maven + Java
* Kotlin + Gradle

But the real problem is universal:

> Given a **blueprint** (name, identity, tech stack, dependencies) —
> how do we generate a high‑quality, consistent skeleton **without coupling** to technology choices?

**Codegen Blueprint** solves this by acting as a **hexagonal, profile‑driven engine**.

---

## 💡 Solution

**Key architectural guarantees:**

* **Hexagonal core** — no framework/build‑tool dependencies
* **ArtifactPorts** defining generation behavior
* **Outbound adapters** per profile
* FreeMarker‑powered template rendering
* Profile‑based configuration determining:

  * Template base paths
  * Ordered **ArtifactKeys**
  * Template → generated file mapping
* Full coverage — unit + integration
* CI/CD automation — CodeQL, JaCoCo, Codecov, GitHub Actions

Planned inbound adapters for **1.0.0 GA**:

* **CLI** (command‑line invocation)
* **REST API** (service‑driven generation)

The engine is ready for future profiles:

* Kotlin, Gradle
* Multi‑module
* Other frameworks
* Organization‑specific stacks

The core never needs to change — profiles live entirely in adapters + template sets.

---

## 🧩 Current Architecture (Hexagonal)

The system follows pure **ports & adapters** design.

### Domain

* `ProjectBlueprint` (aggregate root)
* Value Objects (name, identity, package, dependencies)
* Policies & validation rules
* i18n domain errors

### Application

* `ProjectArtifactsPort` — executes ordered artifact pipeline
* `ArtifactPort` — one adapter per artifact type
* Artifact orchestration logic

### Outbound Adapters

Profile: `springboot-maven-java`

Implements ArtifactKeys:

* `BUILD_CONFIG` → MavenPomBuildConfigurationAdapter
* `BUILD_TOOL_METADATA` → MavenWrapperBuildToolFilesAdapter
* `IGNORE_RULES` → GitIgnoreAdapter
* `APP_CONFIG` → ApplicationYamlAdapter
* `MAIN_SOURCE_ENTRY_POINT` → MainSourceEntrypointAdapter
* `TEST_SOURCE_ENTRY_POINT` → TestSourceEntrypointAdapter
* `PROJECT_DOCUMENTATION` → ProjectDocumentationAdapter

### Inbound Adapters

* CLI (coming soon)
* REST (coming soon)

### Bootstrap

* Spring configuration for wiring profile → adapters → renderer

---

## 📦 Features (1.0.0 Core)

### ✅ Done

* Full hexagonal refactor
* FreeMarker templating support
* Strict domain validation
* Profile‑based artifact pipeline
* Integration test suite
* Codecov + CodeQL
* GitHub Actions pipeline

### 🔄 In Progress

* CLI + REST inbound adapters
* Additional profiles

---

## 🧪 Testing

Run full test suite:

```bash
mvn verify
```

Covers:

* Domain policies & selectors
* Outbound artifact adapters
* End‑to‑end pipeline verification (Failsafe)
* JaCoCo + Codecov

---

## 📂 Sample Output (Generated)

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

* Additional profiles (Kotlin, Gradle, multi‑module)
* Dockerfile + CI/CD artifact adapters
* Extensible dependency catalogs

---

## 📘 Contributing

PRs and ideas welcome 🎯
[https://github.com/bsayli/codegen-blueprint](https://github.com/bsayli/codegen-blueprint)

---

## 🛡 License

MIT — see [LICENSE](LICENSE)

---

**Author:** Barış Saylı
GitHub: [https://github.com/bsayli](https://github.com/bsayli)

