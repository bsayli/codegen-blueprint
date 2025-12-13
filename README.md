# Codegen Blueprint — Architecture-First Project Generator 🚀

[![Build](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/build.yml/badge.svg)](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/blueprint-platform/codegen-blueprint?logo=github\&label=release)](https://github.com/blueprint-platform/codegen-blueprint/releases/latest)
[![CodeQL](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/codeql.yml/badge.svg)](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/codeql.yml)
[![codecov](https://codecov.io/gh/blueprint-platform/codegen-blueprint/branch/main/graph/badge.svg)](https://codecov.io/gh/blueprint-platform/codegen-blueprint/tree/main)
[![Java](https://img.shields.io/badge/Java-21-red?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5%2B-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

* 🤝 [Contributing](#-contributing)
> 🚀 Executable Architecture Generator — Enforced by Design. Scalable by Default.  
🔗 Part of the **Blueprint Platform** → https://github.com/blueprint-platform

<p align="center">
  <img src="docs/images/cover/cover.png" alt="Executable Architecture — From Day Zero" width="700" />
</p>

---

## 🧭 Architecture as a Product

Modern teams rarely struggle to **start** a service.
They struggle to keep **architecture consistent** as they scale.

Most project generators create a folder structure and disappear.
**Codegen Blueprint** codifies and executes architectural intent:

* Enforces boundaries — layered and/or Hexagonal
* Prevents silent architecture drift
* Protects the domain from frameworks
* Standardizes early decisions across teams

> **Executable Architecture — Delivered from day zero**

<br/>

<p align="center">
  <img src="docs/images/architecture/value-proposition.png" width="780" />
  <br/>
  <em>Who benefits → Engine capabilities → What teams get</em>
</p>

---

### 🎯 Who is this for?

| Role                 | Problem Solved                                |
| -------------------- | --------------------------------------------- |
| Platform Engineering | Org‑wide standardization made enforceable     |
| Lead Architects      | Governance as Code — constraints auto‑applied |
| Developers           | No boilerplate — productivity from day zero   |
| New Team Members     | Architecture learning curve removed           |

---

### 🥇 What makes it different?

> **Initializr-like convenience** → **Architecture-first evolution**

| Capability Focus         | Spring Initializr / JHipster | Codegen Blueprint |
| ------------------------ | ---------------------------- | ----------------- |
| Folder scaffolding       | ✔                            | ✔                 |
| Architecture governance  | ⚠️                           | **✔**             |
| Framework‑free domain    | ❌                            | **✔**             |
| Profile‑driven evolution | ⚠️                           | **✔**             |
| Anti‑drift roadmap       | ❌                            | **✔**             |

---

### 🧩 Generate vs Enforce (Cross‑Cutting Concerns)

Most generators **generate code** for cross‑cutting concerns.
That approach does not scale.

**Codegen Blueprint makes a clear distinction:**

| Approach              | What happens                                               | Long‑term effect                             |
| --------------------- | ---------------------------------------------------------- | -------------------------------------------- |
| Generate code         | Copies security, logging, error handling into each service | ❌ Drift, copy‑paste, painful upgrades        |
| Enforce via libraries | Centralized, versioned, opt‑in behavior                    | **✔ Consistency, easy upgrades, governance** |

> **Not everything should be generated.**
> Cross‑cutting concerns should be **enforced**, not duplicated.

With Blueprint Platform:

* Architecture is generated once
* Behavior is enforced via **shared libraries / BOMs**
* Upgrades happen in **one place**, not 50 microservices

This is why Blueprint is not a template collection.

> **It is architecture as an executable product.**

---

> 🧩 Same simplicity — **better long‑term alignment**

---

## 📑 Table of Contents

* ⚡ [What is Codegen Blueprint (Today)?](#-what-is-codegen-blueprint-today)
* 🧭 [1.0.0 Release Scope](#-100-release-scope)
* 🧱 [Architecture Overview](#-architecture-overview)
* 🔌 [Inbound & Outbound Adapters](#-inbound--outbound-adapters)
* 🔄 [CLI Usage](#-cli-usage)
* 🧪 [Testing & CI](#-testing--ci)
* 🚀 [Vision & Roadmap](#-vision--roadmap)
* ⭐ [Support](#-support)
* 🛡 [License](#-license)

---

## ⚡ What is Codegen Blueprint (Today)?

A **CLI-driven**, **profile‑based**, **architecture‑aware** project generator.

📌 Current profile: **springboot‑maven‑java**

> Spring Boot 3.5+ · Java 21 · Maven — production‑ready baseline

Outputs include:

* Clean source layout (main + test)
* Domain isolation & enforcement
* Maven configuration & wrappers
* Application configuration
* Optional **sample Hexagonal service**

---

## 🧱 Architecture Overview

Architecture isn’t only drawn — it executes here ⬇

📌 Want the architectural deep dive?

- 🔒 **Executable Architecture Scope (1.0.0 GA)**  
  Defines what is *strictly enforced today* and output guarantees  
  → [docs/architecture/executable-architecture-scope.md](docs/architecture/executable-architecture-scope.md)

---

- 🧭 **How to Explore This Codebase (Hexagonal Guide)**  
  Understand ports/adapters, profiles, boundaries  
  → [docs/guides/how-to-explore-hexagonal-architecture.md](docs/guides/how-to-explore-hexagonal-architecture.md)

### 🧩 Part of the Blueprint Platform

`codegen-blueprint` is the first foundational module of the **Blueprint Platform** — an architecture‑first project generation ecosystem designed to enable consistent, enforceable, and scalable enterprise development.

Unlike traditional generators that simply scaffold code, Blueprint Platform aims to:

* **Standardize enterprise best practices** through opinionated architecture and code structure
* **Integrate reusable common libraries** that encapsulate cross‑cutting concerns (security, logging, error handling, tracing, OpenAPI clients, etc.)
* **Enforce architectural integrity** using built‑in validation and guardrails (e.g., hexagonal boundaries, naming rules, testable layout)

As the platform evolves, more modules will be introduced to complement `codegen-blueprint` with:

* Ready‑to‑use behavior‑driven libraries
* Consistent and generics‑aware OpenAPI client generation
* Support for multiple frameworks and technology stacks

🔗 Learn more at the [Blueprint Platform GitHub Organization](https://github.com/blueprint-platform)

> This aligns with the upcoming **Vision & Roadmap** section below.


> **Domain stays clean — Ports and adapters connect everything else.**

```
domain       // business logic only
application  // orchestrates ports
adapters     // inbound/outbound driven by use cases
bootstrap    // Spring wiring + config
```

<p align="center">
  <img src="docs/images/architecture/architecture-overview.png" width="860" />
  <br/>
  <em>CLI → Use case layer → Domain → Artifacts → Spring Boot project</em>
</p>

### Guarantees

* **No Spring annotations inside the domain**
* **Hexagonal from day zero — when opted‑in**
* Flexible to evolve with future profiles (CQRS, Layered…)

---

## 🧭 1.0.0 Release Scope

> 📌 `main` branch reflects the upcoming **1.0.0 GA**.

### Included — GA Ready

| Feature                            | Status |
| ---------------------------------- | ------ |
| CLI project generation             | ✔      |
| Optional Hexagonal structure       | ✔      |
| Spring Boot 3.5+ + Java 21 + Maven | ✔      |
| Main & Test entrypoints            | ✔      |
| Build + config artifacts           | ✔      |
| Optional greeting sample           | ✔      |
| MIT License                        | ✔      |

---

## 🔌 Inbound & Outbound Adapters

Adapters drive interactions **in** and **out** of the core domain — keeping domain logic isolated and testable.

### Inbound (Delivery) — How requests enter

| Adapter | Status     | Description                                             |
| ------- | ---------- | ------------------------------------------------------- |
| CLI     | ✔ GA Ready | Primary driver to generate services via command-line    |
| REST    | 🚧 Planned | Future interactive generation + developer onboarding UX |

### Outbound (Artifacts) — What the engine produces

Everything required to **build → run → extend** a real service:

* Maven POM + Wrapper
* Main & Test source structure
* Domain + Application + Adapter layout
* Application configuration (YAML)
* Optional Hexagonal sample slice
* README + project docs
* Filesystem writer for artifact creation

> The domain depends on nothing — adapters depend on the domain.

---

## 🔄 CLI Usage

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli \
  springboot \
  --group-id io.github.blueprintplatform.samples \
  --artifact-id greeting-service \
  --name "Greeting Service" \
  --package-name io.github.blueprintplatform.samples.greeting \
  --layout hexagonal \
  --sample-code basic \
  --dependency web \
  --dependency data_jpa \
  --target-dir /path/to/output
```

**Output (simplified)**

```
greeting-service/
 ├── pom.xml
 ├── src/main/java/.../GreetingServiceApplication.java
 ├── src/test/java/.../GreetingServiceApplicationTests.java
 ├── src/main/resources/application.yml
 └── .gitignore
```

> Hexagonal with optional sample = ready‑to‑run REST service

---

## 🧪 Testing & CI

```bash
mvn verify
```

Includes:

* ✔ Unit + integration tests
* ✔ JaCoCo coverage
* ✔ CodeQL security analysis
* ✔ Codecov reporting

---

## 🚀 Vision & Roadmap

> Architecture should **execute**, not merely be drawn.  
> And it should stay enforced — even 6, 12, 24 months later.

### 🌟 The Vision

**Blueprint Platform** =  
🔹 Architecture-as-a-Product  
🔹 Reusable Behavior Libraries  
🔹 Enforced Consistency Across Teams

From Day Zero to Production — architecture remains **intentional**, **testable**, and **aligned**.

---

### 🎯 Roadmap

#### 🔹 Phase 1 — Architecture-First Generation (Today)
* Hexagonal / Layered architecture enforcement
* Profile-driven CLI generation (Spring Boot · Maven · Java 21)
* Domain purity: **no Spring inside the core**
* End-to-end testable scaffolding

📌 **1.0.0 GA Objective** → Zero-drift architectural foundations

---

#### 🔹 Phase 2 — Reusable Enterprise Behavior (In Progress)
* 🔐 Security defaults (OAuth2 / Keycloak)
* 📡 Resilience / Retries / Standardized error handling
* 🔍 Observability: tracing + logs + metrics — auto-wired
* 🧩 Multi-module enterprise service kits (API + Domain + Infra)
* Generics-aware OpenAPI client generation

📌 Libraries become **switch-on features**, not generated boilerplate

---

#### 🔹 Phase 3 — Ecosystem Expansion (Roadmap)
* Gradle & Kotlin support
* Quarkus + future stack profiles
* Visual UI — configure → generate → download
* Drift detection & auto-remediation (governance at scale)
* Platform telemetry for architecture health

📌 Community-driven priorities →  
🔗 Participate: https://github.com/blueprint-platform/codegen-blueprint/discussions

---

### 🧩 Why this matters

| Without Blueprint | With Blueprint |
|------------------|----------------|
| Architecture drifts silently | Guardrails keep intent executable |
| Boilerplate everywhere | Cross-cutting concerns via libraries |
| Onboarding takes weeks | Day-zero productivity |
| Standards depend on discipline | Standards enforced by construction |

> 📌 The platform grows → Projects stay clean → Enterprise stays consistent

---

**Blueprint Platform isn’t just code generation —  
it is strategic architectural continuity.**

## 🤝 Contributing

We welcome:

* Architecture improvements
* Stack profiles & adapters
* Template & documentation enhancements

Start here → Discussions:
[https://github.com/blueprint-platform/codegen-blueprint/discussions](https://github.com/blueprint-platform/codegen-blueprint/discussions)

Issues:
[https://github.com/blueprint-platform/codegen-blueprint/issues](https://github.com/blueprint-platform/codegen-blueprint/issues)

---

## ⭐ Support

If this project saves your team time or headaches:
👉 **Please star the repo — it truly helps visibility!**

**Barış Saylı** — Creator & Maintainer

* GitHub — [https://github.com/bsayli](https://github.com/bsayli)
* LinkedIn — [https://www.linkedin.com/in/bsayli](https://www.linkedin.com/in/bsayli)
* Medium — [https://medium.com/@baris.sayli](https://medium.com/@baris.sayli)

---

## 🛡 License

MIT — free for commercial and personal use.
See: [LICENSE](LICENSE)
