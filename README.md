# Codegen Blueprint — Architecture-First Project Generator 🚀

[![Build](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/build.yml/badge.svg)](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/blueprint-platform/codegen-blueprint?logo=github\&label=release)](https://github.com/blueprint-platform/codegen-blueprint/releases/latest)
[![CodeQL](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/codeql.yml/badge.svg)](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/codeql.yml)
[![codecov](https://codecov.io/gh/blueprint-platform/codegen-blueprint/branch/main/graph/badge.svg)](https://codecov.io/gh/blueprint-platform/codegen-blueprint/tree/main)
[![Java](https://img.shields.io/badge/Java-21-red?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5%2B-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

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
* 🤝 [Contributing](#-contributing)
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

<br/>

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

### Inbound (Delivery)

| Adapter | Status           |
| ------- | ---------------- |
| CLI     | ✔ Primary driver |
| REST    | 🚧 Planned       |

### Outbound (Artifacts)

* Maven POM + Wrapper
* Application YAML
* Main + Test sources
* Optional greeting sample
* Project documentation
* Filesystem writer

> Everything required to **build → run → extend**

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
  --dependency data_jpa
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

### Near‑term

* 🧱 Hexagonal evolution kits (ports / adapters / CQRS)
* 🔐 Secure defaults — OAuth2 / Keycloak
* 📈 Observability wiring — tracing + metrics
* 🧩 Multi‑module service generation

### Ecosystem Expansion

* Gradle profile
* Kotlin + Quarkus
* Developer UI — configure → generate → download

📌 Community votes influence priorities:
[https://github.com/blueprint-platform/codegen-blueprint/discussions](https://github.com/blueprint-platform/codegen-blueprint/discussions)

---

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
