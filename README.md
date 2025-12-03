# Codegen Blueprint — Enterprise‑Grade, Hexagonal, Architecture‑First Project Generator

[![Build](https://github.com/bsayli/codegen-blueprint/actions/workflows/build.yml/badge.svg)](https://github.com/bsayli/codegen-blueprint/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/bsayli/codegen-blueprint?logo=github\&label=release)](https://github.com/bsayli/codegen-blueprint/releases/latest)
[![CodeQL](https://github.com/bsayli/codegen-blueprint/actions/workflows/codeql.yml/badge.svg)](https://github.com/bsayli/codegen-blueprint/actions/workflows/codeql.yml)
[![codecov](https://codecov.io/gh/bsayli/codegen-blueprint/branch/refactor/hexagonal-architecture/graph/badge.svg)](https://codecov.io/gh/bsayli/codegen-blueprint/tree/refactor/hexagonal-architecture)
[![Java](https://img.shields.io/badge/Java-21-red?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📑 Table of Contents

* 🧭 [Project Status & Release Plan](#-project-status--release-plan)
* 💡 [Why This Project Matters](#-why-this-project-matters)
* 🚀 [Vision — Architecture as a Product](#-vision--architecture-as-a-product)
* 🧱 [Architectural Model](#-architectural-model-pure-hexagonal)
* 🔌 [Inbound Adapters](#-inbound-adapters-delivery-channels)
* ⚙️ [Outbound Adapters & Artifact Rendering](#-outbound-adapters--artifact-rendering)
* 🧪 [Testing & CI](#-testing--ci)
* 🔄 [Example CLI Usage](#-example-cli-usage)
* 🛣 [Roadmap](#-roadmap)
* 🤝 [Contributing](#-contributing)
* 🛡 [License](#-license)

---

## 🧭 Project Status & Release Plan

This repository is in **active development** toward **1.0.0 GA**.

✔ Hexagonal domain, pipeline engine, templating system, CI/CD, and test suite are complete.
✔ **CLI inbound adapter** is implemented — production‑ready project generation via terminal.
🔄 **REST inbound adapter** will follow shortly.

This is not a typical "initializr clone" — this is a **blueprint engine** with real architectural guarantees.

---

## 💡 Why This Project Matters

Modern applications deserve to begin with a strong architectural foundation — not a bare skeleton.

It should begin with:

✓ A clean architectural foundation  
✓ A consistent project structure that every developer recognizes  
✓ Testability and maintainability from day 0  
✓ A basis that scales when the system grows

Today, however:

❌ Every new repository starts differently  
❌ Best practices are manually copied — and often forgotten  
❌ Initial structure varies by team and developer  
❌ Architecture decisions drift over time

**Codegen Blueprint** brings structure, consistency, and architectural clarity right at the starting line.

It reduces setup time while ensuring every new service is built on **solid, modern engineering principles**.

---

## 🚀 Vision — Architecture as a Product

Codegen Blueprint is evolving into a platform where:

| Need | How this project helps |
|------|-----------------------|
| Standardization | Architecture becomes reusable — and enforceable |
| Flexibility | Choose stack, architecture style, and defaults at generation time |
| Future scaling | Support for multi-module and additional frameworks |
| Developer Experience | Faster onboarding and consistent tooling |
| Enterprise features | Security, resilience, and observability options (roadmap) |

The long-term mission:

> **Architectural excellence should be the default — not an afterthought.**

As the project grows, profiles will define not only **technology choices**, but **engineering quality** itself:

* Hexagonal vs layered vs CQRS options
* Security & Keycloak integration toggle
* Tracing + metrics + resilience toggles
* CI/CD + Docker artifacts (roadmap)

---

📌 *Status:* Today a strong architectural generator.  
🌱 *Vision:* A platform engineering accelerator.

## 🧱 Architectural Model (Pure Hexagonal)

*Domain is king — NO Spring dependencies inside.*

Layers:

```
domain
└─ model (aggregate, VOs, policies)
application
└─ use cases orchestrating ports
adapter
├─ outbound (renderers, build files, deps)
└─ inbound (CLI, REST)
bootstrap
└─ wiring (profiles → adapters → engine)
```

Ports define intent — adapters define technology.

Switching Spring Boot → Quarkus?

➡ Add adapter package + new templates
➡ Core engine **does not change**

---

## 🔌 Inbound Adapters (Delivery Channels)

| Adapter      | Status                                     |
| ------------ | ------------------------------------------ |
| **CLI**      | ✔ Complete (primary driver)                |
| **REST API** | 🔄 In progress (service‑driven automation) |

---

## ⚙ Outbound Adapters & Artifact Rendering

Current Profile:

```
springboot-maven-java
```

Implements ArtifactKeys:

* Maven POM
* Maven Wrapper
* `.gitignore`
* Application YAML
* Main Source Entrypoint
* Test Entrypoint
* Documentation

Upcoming adapters:

* Gradle
* Kotlin
* Multi‑module
* CI/CD
* Dockerfile

---

## 🧪 Testing & CI

```bash
mvn verify
```

✔ Full integration tests
✔ JaCoCo coverage
✔ CodeQL security scanning
✔ Codecov reporting

---

## 🔄 Example CLI Usage

```bash
java -jar codegen-blueprint.jar \
  springboot \
  --group-id com.example \
  --artifact-id demo \
  --name "Demo Service" \
  --package-name com.example.demo \
  --dependency WEB
```

Output:

```text
demo/
 ├── pom.xml
 ├── src/main/java/.../DemoApplication.java
 ├── src/main/resources/application.yml
 ├── src/test/java/.../DemoApplicationTests.java
 └── .gitignore
```

---

## 🛣 Roadmap

* **Architecture style selection (hexagonal, layered, CQRS, etc.)**
* Spring Security & Keycloak integration option
* Circuit breaker + retry + tracing + metrics options
* Multi‑module enterprise layouts
* Developer portal integration (Backstage etc.)

This is how platform engineering becomes **automated**.

---

## 🤝 Contributing

Contributions of all kinds are welcome — new ideas, bug reports, feature requests, and adapters for different stacks (Gradle, Kotlin, Keycloak, etc.)

💬 Start a conversation or ask a question:  
👉 Discussions: https://github.com/bsayli/codegen-blueprint/discussions

🐛 Found an issue or missing capability?  
👉 Issues: https://github.com/bsayli/codegen-blueprint/issues

---

## ⭐ Support & Community

If this project helped you or inspired you, please consider giving it a ⭐ —  
it helps others discover and benefit from the work.

Want to collaborate? Feel free to connect:

**Barış Saylı**  
🔗 GitHub — https://github.com/bsayli  
💼 LinkedIn — https://www.linkedin.com/in/bsayli  
📝 Medium — https://medium.com/@baris.sayli

---

## 🛡 License

Licensed under the **MIT License** — free for personal and commercial use.  
See: [LICENSE](LICENSE)