# Codegen Blueprint — Profile‑Driven Project Generator with Architecture Options

[![Build](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/build.yml/badge.svg)](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/blueprint-platform/codegen-blueprint?logo=github&label=release)](https://github.com/blueprint-platform/codegen-blueprint/releases/latest)
[![CodeQL](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/codeql.yml/badge.svg)](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/codeql.yml)
[![codecov](https://codecov.io/gh/blueprint-platform/codegen-blueprint/branch/main/graph/badge.svg)](https://codecov.io/gh/blueprint-platform/codegen-blueprint/tree/main)
[![Java](https://img.shields.io/badge/Java-21-red?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

<p align="center">
  <img src="docs/images/cover/cover.png"
       alt="Executable Architecture — From Day Zero"
       width="700"/>
</p>

---

## 🧠 Why Codegen Blueprint Exists

Modern engineering teams don’t struggle to **start** new services —
they struggle to keep them **architecturally consistent** as they scale.

Most generators produce a folder structure and walk away.
Codegen Blueprint safeguards **architectural integrity**:

* Starts clean — no framework dependencies in the domain
* Stays clean — structure guides every evolution
* Prevents silent architecture drift

Not just scaffolding.
Not just templates.

> **Executable Architecture — baked into the delivery pipeline.**

<br/>

<p align="center">
  <img src="docs/images/architecture/value-proposition.png"
       alt="Value Proposition: Why Codegen Blueprint Exists"
       width="760"/>
  <br/>
  <em>Who benefits ➜ What the engine delivers ➜ Generated services</em>
</p>

---

### 🎯 Who is this for?

| Role                 | Benefit                          |
| -------------------- | -------------------------------- |
| Platform Engineering | Org‑wide standardization         |
| Lead Architect       | Governance as Code               |
| Developers           | Clean architecture from day zero |
| New Team Members     | Instant productivity             |

---

### 🥇 What makes it different?

> **Initializr‑like simplicity** ➜ **Architecture‑first consistency**

| Capability Focus                  | Spring Initializr & JHipster | Codegen Blueprint |
| --------------------------------- | ---------------------------- | ----------------- |
| Generates folder layout           | ✔                            | ✔                 |
| Opinionated architecture defaults | ⚠️                           | **✔**             |
| Domain isolation by design        | ❌                            | **✔**             |
| Profile‑driven evolution paths    | ⚠️                           | **✔**             |
| Anti‑drift support (future‑ready) | ❌                            | **✔**             |

> 🧭 Same starting point — **better long‑term alignment**

---

## 📑 Table of Contents

* ⚡ [What is Codegen Blueprint (Today)?](#-what-is-codegen-blueprint-today)
* 🧭 [1.0.0 Scope & Status](#-100-scope--status)
* 💡 [Why This Project Matters](#-why-this-project-matters)
* 🔔 [Sample Code & Greeting Example](#-sample-code--greeting-example)
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

A **CLI‑driven**, **architecture‑aware** project generator.

📌 Current primary profile: **springboot‑maven‑java**
(✔ Spring Boot 3.5.x · ✔ Java 21 · ✔ Maven)

Produces a clean and predictable structure with:

* Standardized identifiers (groupId, artifactId, package)
* Clear boundaries for maintainability
* Tests ready from day zero
* No dependency overload

### Optional Architecture Layout

📌 Hexagonal is an **opt‑in structured evolution path**.

```
domain       // business logic only (no Spring)
application  // orchestrates ports
adapters     // inbound & outbound
bootstrap    // configuration & wiring
```

> “Framework‑free domain — intentional architecture from day zero.”

<br/>

<p align="center">
  <img src="docs/images/architecture/architecture-overview.png"
       alt="Hexagonal Architecture Overview"
       width="840"/>
  <br/>
  <em>Flow: Inputs ➜ Use Cases ➜ Domain Rules ➜ Artifacts ➜ Executable Service</em>
</p>

---

## 🧭 1.0.0 Scope & Status

> 📌 Note: The `main` branch reflects the upcoming **1.0.0 GA** release.
> A final tagged release will follow once CLI UX and docs stabilization are complete.

### Included in 1.0.0

| Feature                                | Status             |
| -------------------------------------- | ------------------ |
| CLI‑based generation                   | ✔ Production‑ready |
| Hexagonal architecture layout (opt‑in) | ✔ Available        |
| Spring Boot 3 / Java 21 / Maven        | ✔ Supported        |
| Main + test entrypoints                | ✔ Provided         |
| Required build + config artifacts      | ✔ Generated        |
| Greeting sample (optional sample‑code) | ✔ Included         |
| MIT License                            | ✔ Open‑source      |

### Up Next

| Feature                                        | Status     |
|------------------------------------------------| ---------- |
| REST inbound adapter                           | Planned    |
| Hexagonal evolution kit (ports + CQRS)         | Planned    |
| Additional profiles (Gradle, Kotlin, Quarkus)  | Planned    |
| Foundation libraries (`blueprint-*`)           | Planned    |
| Multi‑module services                          | Planned    |
| Developer UI                                   | Evaluating |

> ✔ Deep quality first → expand ecosystem next

📌 For more details:
- [Executable Architecture Scope (1.0.0)](docs/architecture/executable-architecture-scope.md)

---

## 💡 Why This Project Matters

Clean architecture shouldn’t be optional.

You gain:

* ✔ Predictable structure & boundaries
* ✔ Testability from day zero
* ✔ Faster onboarding & team scaling
* ✔ Architectural governance without friction

You avoid:

* ❌ Copy‑paste architecture
* ❌ Each repo reinventing patterns
* ❌ Best‑practice rot over time
* ❌ Architecture drift

📘 Explore design:
👉 [How to Explore This Project (Hexagonal Architecture Guide)](./docs/guides/how-to-explore-hexagonal-architecture.md)

### Strategic Impact

Architecture becomes **intentional — enforceable — repeatable**.

---

## 🔔 Sample Code & Greeting Example

📌 Minimal but meaningful reference sample:

* Domain model: **Greeting**
* Use case: generate greeting text
* Inbound REST adapter: `/api/v1/sample/greetings/default`
* Hexagonal structure illustrates **port‑driven design**

Enabled when flags include:

```
--layout hexagonal \
--sample-code basic
```

> Designed as a **teaching reference** and a **quick productivity boost**

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

| Category        | Includes                                                 |
| --------------- | -------------------------------------------------------- |
| Build system    | `pom.xml`, Maven Wrapper                                 |
| Runtime config  | `src/main/resources/application.yml`                     |
| Source skeleton | Main application & test bootstraps                       |
| Sample code     | Optional greeting sample (domain + ports + REST adapter) |
| Git hygiene     | `.gitignore`                                             |
| Docs (minimal)  | `README.md` inside generated project                     |

> Everything required to **build ▸ run ▸ extend** a clean service

---

## 🧪 Testing & CI

```bash
mvn verify
```

* Unit + integration tests ✔
* JaCoCo coverage ✔
* CodeQL security ✔
* Codecov reporting ✔

---

## 🔄 CLI Usage Example

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli \
  springboot \
  --group-id io.github.blueprintplatform.samples \
  --artifact-id greeting-service \
  --name "Greeting Service" \
  --description "Hexagonal greeting sample powered by Blueprint" \
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
 ├── src/main/java/io/github/blueprintplatform/samples/greeting/GreetingServiceApplication.java
 ├── src/test/java/io/github/blueprintplatform/samples/greeting/GreetingServiceApplicationTests.java
 ├── src/main/resources/application.yml
 └── .gitignore
```

> Hexagonal + sample code = ready‑to‑run REST greeting service

---

## 🚀 Vision & Roadmap (Beyond 1.0.0)

> Best practices should **execute**, not merely be documented.

* 🧱 Hexagonal evolution kits (ports / adapters / CQRS)
* 📈 Observability defaults (tracing / metrics)
* 🔐 Security (OAuth2 / Keycloak patterns)
* 🧩 Multi‑module service generation
* 🎯 Broader ecosystem: Gradle / Kotlin / Quarkus
* 💻 Developer UI → configure → generate → download

> **Executable Architecture for modern delivery**

---

## 🤝 Contributing

Discussions:
[https://github.com/blueprint-platform/codegen-blueprint/discussions](https://github.com/blueprint-platform/codegen-blueprint/discussions)

Issues:
[https://github.com/blueprint-platform/codegen-blueprint/issues](https://github.com/blueprint-platform/codegen-blueprint/issues)

---

## ⭐ Support & Community

If Codegen Blueprint helps you:  
👉 Please star the repo — it really matters.

**Barış Saylı**

GitHub — [bsayli](https://github.com/bsayli)  
LinkedIn — [linkedin.com/in/bsayli](https://www.linkedin.com/in/bsayli)  
Medium — [@baris.sayli](https://medium.com/@baris.sayli)

---

## 🛡 License

Licensed under MIT — free for personal and commercial use.
See: [LICENSE](LICENSE)
