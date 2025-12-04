# Codegen Blueprint — Profile-Driven Project Generator with Architecture Options

[![Build](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/build.yml/badge.svg)](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/blueprint-platform/codegen-blueprint?logo=github\&label=release)](https://github.com/blueprint-platform/codegen-blueprint/releases/latest)
[![CodeQL](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/codeql.yml/badge.svg)](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/codeql.yml)
[![codecov](https://codecov.io/gh/blueprint-platform/codegen-blueprint/branch/refactor/hexagonal-architecture/graph/badge.svg)](https://codecov.io/gh/blueprint-platform/codegen-blueprint/tree/refactor/hexagonal-architecture)
[![Java](https://img.shields.io/badge/Java-21-red?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📑 Table of Contents

* ⚡ [What is Codegen Blueprint (Today)?](#-what-is-codegen-blueprint-today)
* 🧭 [1.0.0 Scope & Status](#-100-scope--status)
* 💡 [Why This Project Matters](#-why-this-project-matters)
* 🔌 [Inbound Adapter](#-inbound-adapter-delivery)
* ⚙️ [Outbound Adapters & Artifacts](#-outbound-adapters--artifacts)
* 🧪 [Testing & CI](#-testing--ci)
* 🔄 [CLI Usage Example](#-cli-usage-example)
* 🚀 [Vision & Roadmap](#-vision--roadmap-beyond-100)
* 🤝 [Contributing](#-contributing)
* ⭐ [Support & Community](#-support--community)
* 🛡 [License](#-license)

---

## ⚡ What is Codegen Blueprint (Today)?

A **CLI-driven**, **architecture-aware** project generator.

📌 Current primary profile:
**springboot-maven-java**
(Spring Boot 3 + Maven + Java 21)

By default, it generates a **clean**, **ready-to-extend** Spring Boot project structure — similar to Spring Initializr, but with:

- Clear and predictable layout
- Standardized project metadata (name, groupId, package structure)
- Built-in test entry points from day zero
- Consistent defaults that avoid “starter chaos”

### Optional Architecture Layouts

📌 Hexagonal is an evolution path — not a barrier.

> Architecture should enable teams — not block them.
> You can start simple and progressively introduce ports/adapters later.

For teams embracing Clean/Hexagonal architecture,  
Codegen Blueprint provides an **optional** layout:

```
domain       // business rules (no Spring dependencies)
application  // use cases orchestrating ports
adapters      // inbound & outbound adapters
bootstrap    //  wiring and configuration
```

Short summary:

> “Spring Initializr — but **with best-practice architecture options built-in**, not bolted on later.”

---

## 🧭 1.0.0 Scope & Status

### What is included (1.0.0)

| Capability                                      | Status                    |
| ----------------------------------------------- | ------------------------- |
| CLI-based generation                            | ✔ Production-ready        |
| Standard Spring Boot service skeleton           | ✔ Stable                  |
| Optional architecture layout (hexagonal-basic)  | ✔ Available (opt-in)      |
| Spring Boot 3 / Java 21 / Maven support         | ✔                         |
| Build artifacts (pom, wrapper, .gitignore…)     | ✔                         |
| Main + test source entrypoints                  | ✔                         |
| Open-source licensing                           | ✔ MIT License             |

### What is planned next

| Feature                                       | Status           |
| --------------------------------------------- | ---------------- |
| REST inbound adapter generation               | Planned          |
| Advanced hexagonal variations (ports, CQRS)   | Planned          |
| Additional profiles (Gradle, Kotlin, Quarkus) | Planned          |
| Multi-module architecture generation          | Planned          |
| Foundation libraries (`blueprint-*`)          | Planned          |
| Developer UI / web console                    | Under evaluation |

> Strategy: **Deep quality for one profile first** → Expand profiles afterward.

---

## 💡 Why This Project Matters

Modern services deserve more than a bare `/src/main/java`:

**What you get:**
- ✔ Predictable, recognizable structure
- ✔ Testability from day zero
- ✔ Architectural integrity as a **standard**, not an afterthought
- ✔ Faster onboarding and reduced cognitive load

**What you avoid:**
- ❌ Copy-paste architecture
- ❌ Every repo looks different
- ❌ Best practices get lost over time
- ❌ Architecture silently degrades as systems evolve

> Codegen Blueprint = **consistency + correctness delivered automatically**

### 🧩 Strategic Impact (Why it matters at scale)

> 🔒 **Best practices become the default. Architecture stays intentional — not accidental.**

Codegen Blueprint gives teams a strong starting point that aligns with modern architectural
expectations — and reduces the risk of chaotic divergence as projects evolve.

Teams benefit from:
- Standardized setup across all services
- A clean structure where responsibilities are explicit
- Faster onboarding, even for junior developers
- A foundation that supports future enforcement if needed (e.g., ArchUnit, module boundaries)

Result:
**Every new service starts aligned — and can scale without losing its architectural intent.**

---

## 🔌 Inbound Adapter (Delivery)

| Adapter | Status           |
| ------- | ---------------- |
| CLI     | ✔ Primary driver |
| REST    | Planned          |

---

## ⚙️ Outbound Adapters & Artifacts

Active profile:

```
springboot‑maven‑java
```

Generated artifacts:

| Artifact               | Status |
| ---------------------- | ------ |
| Maven POM              | ✔      |
| Maven Wrapper          | ✔      |
| `.gitignore`           | ✔      |
| Application YAML       | ✔      |
| Main source entrypoint | ✔      |
| Test source entrypoint | ✔      |
| Project documentation  | ✔      |

---

## 🧪 Testing & CI

```bash
mvn verify
```

Includes:

- ✔ Unit + integration tests
- ✔ JaCoCo coverage reporting
- ✔ CodeQL security analysis
- ✔ Codecov metrics

---

## 🔄 CLI Usage Example

```bash
java -jar codegen-blueprint.jar \
  springboot \
  --group-id com.example \
  --artifact-id demo \
  --name "Demo Service" \
  --package-name com.example.demo \
  --dependency WEB
```

📁 Output (simplified)

```
demo/
 ├── pom.xml
 ├── src/main/java/com/example/demo/DemoApplication.java
 ├── src/test/java/com/example/demo/DemoApplicationTests.java
 ├── src/main/resources/application.yml
 └── .gitignore
```

---

## 🚀 Vision & Roadmap (Beyond 1.0.0)

> Best practices should **execute**, not just be documented.

Roadmap themes:

* Architecture variations (hexagonal / layered / CQRS)
* Observability defaults (logging, metrics, tracing)
* Security integrations (OAuth2 / Keycloak)
* Multi‑module architecture support
* Richer profile ecosystem:

    * Gradle
    * Kotlin
    * Quarkus
* Developer UI to configure + generate + download

Long‑term goal:

> **Executable architectural standards** for modern service development.

---

## 🤝 Contributing

Contributions are welcome!

💬 Discussions: [https://github.com/blueprint-platform/codegen-blueprint/discussions](https://github.com/blueprint-platform/codegen-blueprint/discussions)
🐛 Issues: [https://github.com/blueprint-platform/codegen-blueprint/issues](https://github.com/blueprint-platform/codegen-blueprint/issues)

---

## ⭐ Support & Community

If Codegen Blueprint helps you:
👉 Please star the repo — it really matters.

**Barış Saylı**
GitHub — [https://github.com/bsayli](https://github.com/bsayli)
LinkedIn — [https://www.linkedin.com/in/bsayli](https://www.linkedin.com/in/bsayli)
Medium — [https://medium.com/@baris.sayli](https://medium.com/@baris.sayli)

---

## 🛡 License

Licensed under MIT — free for personal and commercial use.
See: [LICENSE](LICENSE)
