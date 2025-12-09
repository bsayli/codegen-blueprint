# 🚀 Codegen-Blueprint — Hexagonal Architecture Deep Dive

Welcome! This guide helps you explore how **Clean Architecture + Hexagonal (Ports & Adapters)** is implemented in a **fully-tested, production-grade project generation engine**.

This repository demonstrates a production-grade application of Hexagonal Architecture, designed to scale while remaining framework-agnostic.

---

## 📚 Table of Contents

* [🧱 Architectural Overview](#-architectural-overview)
* [🔌 Ports & Adapters](#-ports--adapters)

    * [💼 Domain → Outbound Ports](#-domain--outbound-ports)
    * [🧩 Application → Artifact Generation Ports](#-application--artifact-generation-ports)
    * [🛠️ Technology Adapters](#️-technology-adapters)
* [📦 Profiles: Externalized Architecture Rules](#-profiles-externalized-architecture-rules)
* [🧱 Source Layout Generation](#-source-layout-generation)
* [📄 Resource Model — Stronger than “Files”](#-resource-model--stronger-than-files)
* [🧪 Testing Strategy](#-testing-strategy)
* [🎯 What You Can Learn Here](#-what-you-can-learn-here)
* [🎮 Try It — CLI Adapter](#-try-it--cli-adapter)
* [🔍 Start Here](#-start-here)
* [⭐ Final Thoughts](#-final-thoughts)

---

## 🧱 Architectural Overview

Core layering is strictly controlled — **dependencies flow inward → toward the domain**:

```
bootstrap  (Spring wiring)
    ↓
adapter    (technology: CLI, FS, Maven, docs, templating)
    ↓
application (use cases, orchestration, profiles)
    ↓
domain       (pure logic, business rules)
```

Each layer has a single responsibility and remains independently testable.

---

## 🔌 Ports & Adapters

Behavior is driven by **ports (interfaces)** — independent of frameworks.

### 💼 Domain → Outbound Ports

Used by **application logic** to interact with external systems — without depending on them:

| Port                  | Responsibility                                        |
| --------------------- | ----------------------------------------------------- |
| `ProjectRootPort`     | Prepare the target project directory location         |
| `ProjectWriterPort`   | Persist generated resources (text/binary/directories) |
| `ProjectArchiverPort` | Package generated project (e.g., ZIP)                 |

These abstractions are **shared across all tech stacks**.

---

### 🧩 Application → Artifact Generation Ports

Application defines **artifact-driven generation contracts**.
Each port corresponds to a **single concrete project artifact**:

| Port                           | Artifact Responsibility                          |
| ------------------------------ | ------------------------------------------------ |
| `SourceLayoutPort`             | Create source tree + Java base package structure |
| `MainSourceEntrypointPort`     | Generate main application class                  |
| `TestSourceEntrypointPort`     | Generate sample test class                       |
| `ApplicationConfigurationPort` | `application.yml` (or equivalent config)         |
| `BuildConfigurationPort`       | Build configuration (e.g., `pom.xml`)            |
| `BuildToolFilesPort`           | Build tool metadata (wrapper scripts, etc.)      |
| `IgnoreRulesPort`              | `.gitignore` + VCS rules                         |
| `ProjectDocumentationPort`     | README + docs                                    |

Supporting pipeline ports:

| Port                       | Role                                      |
| -------------------------- | ----------------------------------------- |
| `ProjectArtifactsPort`     | Executes the ordered artifact pipeline    |
| `ProjectArtifactsSelector` | Chooses implementation based on TechStack |

---

### 🛠️ Technology Adapters

Adapters implement the above ports with real tooling:

* Filesystem
* FreeMarker templates
* Maven metadata
* Future: Gradle, REST API inbound, Kotlin, etc.

Adding new tech? → Plug in new adapters + templates.
No domain code changes.

---

## 📦 Profiles: Externalized Architecture Rules

Profiles define what and how artifacts are generated:

* Template namespace
* Enabled artifacts
* Strict ordering

📍 Example: `springboot-maven-java` profile

```
build-config → build-tool-metadata → ignore-rules
→ source-layout → app-config
→ main-source-entrypoint → test-source-entrypoint → project-documentation
```

This keeps the system **evolution-friendly**.

---

## 🧱 Source Layout Generation

`SOURCE_LAYOUT` adapter now generates:

### Standard Layout

```
src/main/java/<basepkg>/
src/main/resources/
src/test/java/<basepkg>/
src/test/resources/
```

### Hexagonal Layout (if selected)

```
src/main/java/<basepkg>/
 ├─ adapter/
 ├─ application/
 ├─ bootstrap/
 └─ domain/
```

Directories are **first-class resources** — not side effects.

---

## 📄 Resource Model — Stronger than “Files”

Domain now models output formally:

| Type      | Record                    | Use Case                 |
| --------- | ------------------------- | ------------------------ |
| Directory | `GeneratedDirectory`      | Create folder structure  |
| Text      | `GeneratedTextResource`   | Java sources, YAML, docs |
| Binary    | `GeneratedBinaryResource` | Wrapper JARs, images     |

Supports:

* Template-less generation
* Binary attachments
* Future architectural scaffolding

---

## 🧪 Testing Strategy

| Test Type                | Validates                                  |
| ------------------------ | ------------------------------------------ |
| **Unit Tests**           | Domain rules + adapter logic               |
| **Integration Tests**    | Spring profile wiring + artifact pipelines |
| **End-to-End CLI Tests** | Full generation = ZIP project output       |

CI includes:

* JaCoCo + Codecov reporting
* CodeQL static analysis
* Strict contract tests for adapters

---

## 🎯 What You Can Learn Here

| Goal                    | How this repo helps                       |
| ----------------------- | ----------------------------------------- |
| Hexagonal mastery       | Strict boundaries, framework independence |
| Build generator engines | Ordered profile-driven pipeline           |
| Maintainability         | Add tech stacks without refactoring core  |
| CI-ready engineering    | Full quality gates enforced               |

This is a **real** reference — not a superficial sample.

---

## 🎮 Try It — CLI Adapter

```bash
java -jar codegen-blueprint.jar \
  --spring.profiles.active=cli \
  springboot \
  --group-id com.example \
  --artifact-id demo-app \
  --name "Demo App" \
  --package-name com.example.demo \
  --dependency WEB
```

Outputs a fully generated Spring Boot project (zipped).

---

## 🔍 Start Here

Follow the data flow:

```
ProjectBlueprint
   ↓
ProjectArtifactsSelector
   ↓
ProjectArtifactsPort (ordered adapters)
   ↓
ProjectWriterPort → filesystem
```

This is **architecture in motion**.

---

## ⭐ Final Thoughts

This project is a **clear, modern, enterprise-grade reference**, showcasing:

* No framework leakage into the domain
* No accidental complexity
* Contracts fully enforced through testing

Happy exploring! 🧑‍🚀✨
