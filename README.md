# Codegen Blueprint — Architecture-First Project Generator

[![Build](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/build.yml/badge.svg)](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/blueprint-platform/codegen-blueprint?logo=github\&label=release)](https://github.com/blueprint-platform/codegen-blueprint/releases/latest)
[![CodeQL](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/codeql.yml/badge.svg)](https://github.com/blueprint-platform/codegen-blueprint/actions/workflows/codeql.yml)
[![codecov](https://codecov.io/gh/blueprint-platform/codegen-blueprint/branch/main/graph/badge.svg)](https://codecov.io/gh/blueprint-platform/codegen-blueprint/tree/main)
[![Java](https://img.shields.io/badge/Java-21-red?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

<p align="center">
  <img src="docs/images/cover/cover.png" alt="Executable Architecture — From Day Zero" width="700" />
</p>

> **Codegen Blueprint** is for teams who care less about *how fast a project starts*
> and more about *how well its architecture survives over time*.

---

## If you’ve ever…

* Your codebase started clean — then **architecture drifted silently** once things were “up and running”.
* A new developer (or a rushed change) shipped code into the **wrong layer** — and the only “rule” was tribal knowledge.
* Reviews turned into **“is this the right boundary?”** debates — because nothing was **executable**.

Codegen Blueprint exists for that exact moment:
when architecture needs to become **observable and testable** — not aspirational.

It is designed for teams who care about **architectural integrity over time**,
and want boundaries that remain **visible and verifiable** as systems, teams, and pressure evolve.

---

## Try it in 5 minutes

### Prerequisites

* Java 21
* Maven 3.9+
* macOS / Linux / WSL (the proof runner is a shell script)

> **Goal:** see **GREEN → RED → GREEN** using **build-time architecture guardrails (strict mode)**.
>
> No app startup required. Guardrails run during `mvn verify` and fail the build deterministically on boundary violations.

---

### 1) Clone the repository

```bash
git clone https://github.com/blueprint-platform/codegen-blueprint.git && cd codegen-blueprint
```

---

### 2) Build the generator JAR

```bash
mvn -q clean package
```

---

### 3) Run the executable architecture proof

```bash
cd docs/demo
chmod +x ./proof/proof-runner.sh
CODEGEN_JAR="$(ls -1 ../../target/codegen-blueprint-*.jar | head -n 1)" ./proof/proof-runner.sh
```

---

### What you should see

* ✅ A project is generated with **strict** guardrails
* ✅ `mvn verify` passes (baseline)
* ❌ An intentional architectural boundary violation is introduced
* ❌ `mvn verify` fails **deterministically** with a generated ArchUnit rule
* ✅ The violation is reverted and the build returns to green

**Executable evidence bundle (screenshots, exact failures, and proof artifacts):**

👉 [Executable Architecture Proof](docs/demo/executable-architecture-proof.md)

---

## 📑 Table of Contents

* 🤔 [Should you clone this repository?](#-should-you-clone-this-repository)
* 🛡 [1.0.0 GA promise (non-negotiable)](#-100-ga-promise-non-negotiable)
* ⚡ [What is Codegen Blueprint (today)?](#-what-is-codegen-blueprint-today)
* 🧱 [Architecture Overview](#-architecture-overview)
* 🧪 [Executable Architecture — proof](#-executable-architecture--proof)
* 📦 [Release & compatibility discipline](#-release--compatibility-discipline)
* 🚫 [What we explicitly do NOT guarantee](#-what-we-explicitly-do-not-guarantee)
* 🧾 [Project history (short)](#-project-history-short)
* 🧭 [Architecture as an executable product](#-architecture-as-an-executable-product)
* 🎯 [Who is this for?](#-who-is-this-for)
* 🥇 [What makes Codegen Blueprint different?](#-what-makes-codegen-blueprint-different)
* 🧩 [Generate vs deliver capabilities (cross-cutting concerns)](#-generate-vs-deliver-capabilities-cross-cutting-concerns)
* 🧩 [Part of the Blueprint Platform](#-part-of-the-blueprint-platform)
* 🧭 [1.0.0 Release Scope](#-100-release-scope)
* 🔌 [Inbound & Outbound Adapters](#-inbound--outbound-adapters)
* 🔄 [CLI Usage (Spring Boot)](#-cli-usage-spring-boot)
* 🧪 [Testing & CI (This Repository)](#-testing--ci-this-repository)
* 🚀 [Vision & Roadmap](#-vision--roadmap)
* 🤝 [Contributing](#-contributing)
* ⭐ [Support](#-support)
* 🛡 [License](#-license)

---

## 🤔 Should you clone this repository?

Clone this project if **architecture drift** has ever cost you time, quality, or trust — and you want boundaries that are **observable in the build**, not implied in docs.

**Codegen Blueprint is not a faster way to scaffold a project.**
It turns architectural intent into **executable guardrails** with **fast, deterministic feedback** during `mvn verify`.

### ✅ Best fit

* You optimize for **long‑term maintainability**, not day‑one scaffolding speed.
* You want **build‑time signals** when boundaries are crossed.
* You prefer **explicit contracts** over tribal knowledge and reviewer debates.

### 🚫 Not the best fit

* You only need a quick starter template without build-time guardrails.
* You expect cross‑cutting runtime behavior (security/logging/etc.) to be generated as boilerplate.

---

## 🛡 1.0.0 GA promise (non-negotiable)

**Every project generated by Codegen Blueprint 1.0.0 can include executable architecture guardrails — enabled by default (`basic`), with explicit opt-out (`--guardrails none`).**

* Guardrails are **generated ArchUnit rules** evaluated at **build time** (`mvn verify`).
* Boundary violations fail the build **deterministically**, while context is still fresh.
* Guardrails are **mode-based** (`basic` / `strict`): selecting the mode is explicit; **disabling is explicit**.
* The goal is not restriction — it’s **protecting architectural intent as the system evolves**.

> **GA contract source of truth**
>
> What is guaranteed (and only what is guaranteed) is defined in:
> 
> → [Executable Architecture Contract — 1.0.0 GA](docs/architecture/executable-architecture-contract.md)

---

## ⚡ What is Codegen Blueprint (today)?

A **CLI-driven**, **profile-based**, **architecture-aware** project generator that produces **buildable output** with **configurable architecture guardrails**.

📌 Current GA profile: **springboot-maven-java**

> Generated projects: Spring Boot **3.5 (default)** or **3.4** · Java **21 (GA baseline)** · Maven **3.9+**

It delivers:

* Deterministic, single-module project generation
* Clean `main`/`test` source layout with verified bootstrapping
* Layout options: **standard (layered)** / **hexagonal (ports & adapters)**
* Guardrails via generated ArchUnit tests (default: `--guardrails basic`; options: `none|basic|strict`)
* Maven wrapper + build baseline + `application.yml`
* Optional **basic sample code** (standard + hexagonal)

---

## 🧱 Architecture Overview

📘 **Canonical platform specification**

The canonical definition of **Architecture as a Product** is specified at the platform level.

→ [Architecture as a Product — Platform Specification](https://github.com/blueprint-platform/blueprint-platform-spec/blob/main/specs/architecture-as-a-product.md)

This repository provides **executable proof** of that specification
for the **Spring Boot · Maven · Java** profile.

Architecture isn’t only drawn — it **executes** here.

Codegen Blueprint (the generator itself) is built with **Hexagonal Architecture** — not as a stylistic preference,
but as a **structural foundation** that keeps the core engine isolated from technology choices
and stable as delivery surfaces evolve.

> Generate once.  
> Evolve across frameworks, runtimes, and languages — **without rewriting the core**.

This separation allows the engine to preserve its architectural contract
while enabling future stack expansion through replaceable adapters.

> This section covers the generator’s architecture (the engine itself).  
> For generated project layouts (`standard` / `hexagonal`), see the CLI documentation.

Spring Boot is the **first delivery adapter** — not the foundation.

### Architecture docs (from capability → GA contract → guide → collaboration)

* 📜 **Architecture Guardrails Rulebook** — full guardrails semantics and rule vocabulary *(descriptive reference; not a GA guarantee)*

  → [Architecture Guardrails Rulebook](docs/architecture/architecture-guardrails-rulebook.md)

* 🔒 **Executable Architecture Contract — 1.0.0 GA** — authoritative GA guarantee surface *(if it’s not listed, it’s not guaranteed)*

  → [Executable Architecture Contract — 1.0.0 GA](docs/architecture/executable-architecture-contract.md)

* 🧭 **How to Explore This Codebase (Hexagonal Guide)** — practical guide to ports/adapters, boundaries, and profile‑driven execution

  → [Hexagonal Architecture Guide](docs/guides/how-to-explore-hexagonal-architecture.md)

* 🧠 **Architecture Governance & AI Collaboration Protocol** — how decisions stay visible and consistent in multi‑contributor / AI‑assisted work

  → [Architecture Governance & AI Protocol](docs/architecture/architecture-governance-and-ai-protocol.md)

---

## 🧪 Executable Architecture — proof

A reproducible walkthrough that demonstrates **GREEN → RED → GREEN** purely through **build‑time guardrails**:

👉 [Executable Architecture Proof](docs/demo/executable-architecture-proof.md)

---

## 📦 Release & compatibility discipline

Versions represent **architectural and compatibility contracts**, not feature counts.

* Breaking changes are introduced **only in major versions**.
* Determinism, structure, and guardrails guarantees are **intentional and protected** starting from **1.0.0 GA**.

👉 [Release Discipline](docs/policies/release-discipline.md)

---

## 🚫 What we explicitly do NOT guarantee

Codegen Blueprint is intentionally **constrained by design**.
Some things are out of scope — not by accident, but to protect architectural integrity.

👉 [What We Do NOT Guarantee](docs/policies/what-we-do-not-guarantee.md)

---

## 🧾 Project history (short)

Codegen Blueprint started as an early experiment in Spring Boot bootstrapping (`codegen-springboot-initializr`).
As it evolved, the focus shifted from “scaffolding” to **Executable Architecture** — guardrails that can fail the build.

* `main` reflects the architecture‑first direction.
* Earlier `0.x` tags remain for historical context.
* **The compatibility and guarantee contract begins with 1.0.0 GA.**

---

## 🧭 Architecture as an executable product

Most teams don’t fail because they chose the wrong framework.
They fail because **architecture drifts** once a project is “up and running”.

Codegen Blueprint targets what happens **after** generation:

* Boundaries become **explicit** (structure + layout semantics)
* Drift becomes **observable** (guardrails evaluated during `mvn verify`)
* The domain stays **framework‑free by construction** (especially in hexagonal)
* Early decisions stay **consistent** across teams and projects

---

## 🎯 Who is this for?

| Role                 | What it unlocks                                     |
| -------------------- | --------------------------------------------------- |
| Platform Engineering | Org-wide standards made **explicit and verifiable** |
| Lead Architects      | Guardrails contracts made **observable**            |
| Developers           | Less debate, faster feedback loops                  |
| New Team Members     | Architecture learning curve reduced by structure    |

---

## 🥇 What makes Codegen Blueprint different?

Codegen Blueprint does **not** compete on scaffolding speed, template volume,
or framework convenience.

Its focus is **architectural continuity** —
making architectural boundaries **explicit, observable, and verifiable**
as systems evolve over time.

| Focus area                   | Traditional project generators | Codegen Blueprint |
| ---------------------------- | ------------------------------ | ----------------- |
| Primary goal                 | Fast project start             | Architectural continuity |
| Architecture boundaries      | Implicit or documented         | **Executable & verified** |
| Drift detection              | Manual (reviews, discipline)   | **Build-time feedback** |
| Domain isolation             | Optional / framework-led       | **By construction** |
| Long-term evolution strategy | Out of scope                   | **First-class concern** |

Codegen Blueprint exists for teams who have already felt the cost of
silent architecture drift — and want boundaries that remain
**visible and testable**, not dependent on tribal knowledge or reviews.

---

## 🧩 Generate vs deliver capabilities (cross-cutting concerns)

Most generators **generate code** for cross-cutting concerns. That approach does not scale.

Codegen Blueprint makes a deliberate distinction:

| Approach                | What happens                                                 | Long-term effect                              |
| ----------------------- | ------------------------------------------------------------ | --------------------------------------------- |
| Generate code           | Copies security/logging/error handling into each service     | ❌ Drift, copy-paste, painful upgrades         |
| Deliver as capabilities | Centralized, versioned, opt-in behavior via shared libraries | **✔ Consistency, easier upgrades, alignment** |

> Not everything should be generated.
> Cross-cutting concerns should be **delivered as capabilities**, not duplicated as code.

---

## 🧩 Part of the Blueprint Platform

`codegen-blueprint` is the first foundational module of the **Blueprint Platform** — an ecosystem designed to keep architecture **explicit, testable, and evolvable**.

🔗 Learn more at the [Blueprint Platform GitHub organization](https://github.com/blueprint-platform)

---

## 🧭 1.0.0 Release Scope

> 📌 The `main` branch reflects the **1.0.0 GA line**.

### Included — GA Guarantees (1.0.0)

The following guarantees define the **1.0.0 GA contract surface**.

| Guarantee                        | Description                                                                 |
| -------------------------------- | --------------------------------------------------------------------------- |
| Deterministic project generation | The same inputs produce the same structure and files.                       |
| CLI-driven generation            | The CLI is the source of truth—no UI-only behavior or hidden defaults.      |
| Layout options                   | Standard (layered) and Hexagonal (ports & adapters) layouts can be generated on demand. |
| Architecture guardrails          | Generated ArchUnit rules (default: `basic`; opt-out: `none`; `strict` supported). |
| Test-ready output                | Generated projects are designed to pass `mvn verify` on the GA baseline.    |
| Profile-driven stack selection   | Technology choices are expressed via profiles (not ad-hoc flags).           |
| Framework-free domain core       | No Spring dependencies or annotations in the domain.                        |

> **GA target**
>
> The 1.0.0 GA profile targets **Spring Boot 3.4 / 3.5**, **Java 21**, and **Maven**.
> The generator runtime baseline is **Java 21**. Generated output may target newer JDKs (e.g., **Java 25**),
> but that is **not** part of the GA contract.

---

## 🔌 Inbound & Outbound Adapters

Adapters drive interactions **into** and **out of** the core—keeping domain logic isolated, explicit, and testable.

### Inbound (Delivery) — How requests enter

| Adapter | Status     | Description                                           |
| ------- | ---------- | ----------------------------------------------------- |
| CLI     | ✔ GA Ready | Primary driver to generate projects via command line. |
| REST    | 🚧 Planned | Future interactive generation + onboarding UX.        |

### Outbound (Artifacts) — What the engine produces

> Architecture guardrails are generated as **output artifacts**—not hard-wired into the engine.

The generator produces everything required to **build → run → extend** a real service:

* Maven POM + Maven Wrapper
* Main & test source structure
* Domain + Application + Adapter layout
* Application configuration (`application.yml`)
* Optional **basic sample code**, depending on selected layout:

  * **standard (layered)** sample slice
  * **hexagonal (ports & adapters)** sample slice
* Optional **architecture checks (ArchUnit tests)** *(enabled via `--guardrails basic|strict`)*
* README + project documentation
* Filesystem writer that materializes generated artifacts

> The core remains clean: **domain depends on nothing**; adapters depend on the domain.

---
## 🔄 CLI Usage (Spring Boot)

This section documents the **current CLI contract** for Codegen Blueprint **1.0.0**.
It reflects the *actual generated output*—no aspirational flags or placeholder examples.

> **How to run the CLI**
>
> Codegen Blueprint is a Spring Boot executable JAR. The CLI is activated via `--cli`, then the subcommand (e.g. `springboot`) and its options follow.
>
> Example pattern:
>
> `java -jar <jar> --cli springboot <options...>`

---

### Basic Usage

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli springboot \
  --group-id io.github.blueprintplatform \
  --artifact-id greeting \
  --name "Greeting" \
  --description "Greeting sample built with hexagonal architecture" \
  --package-name io.github.blueprintplatform.greeting \
  --layout hexagonal \
  --guardrails strict \
  --sample-code basic \
  --dependency web \
  --target-dir /path/to/output
```

> Tip: If you built locally, the typical path is:
>
> `java -jar target/codegen-blueprint-1.0.0.jar --cli springboot ...`

---

### Available Options (`springboot`)

| Option           | Required | Default    | Description                                                                     |
| ---------------- | -------- | ---------- |---------------------------------------------------------------------------------|
| `--group-id`     | ✔        | –          | Maven `groupId`.                                                                |
| `--artifact-id`  | ✔        | –          | Maven `artifactId` (also becomes the output folder name).                       |
| `--name`         | ✔        | –          | Human-readable project name.                                                    |
| `--description`  | ✔        | –          | Project description (**min 10 characters**).                                    |
| `--package-name` | ✔        | –          | Base Java package name.                                                         |
| `--build-tool`   | ✖        | `maven`    | Build tool (**currently only** `maven`).                                        |
| `--language`     | ✖        | `java`     | Programming language (**currently only** `java`).                               |
| `--java`         | ✖        | `21`       | **Generated project** Java version: `21`, `25` — **GA target: 21**.             |
| `--boot`         | ✖        | `3.5`      | **Generated project** Spring Boot version: `3.4`, `3.5` — (GA baseline: `3.5`)  |
| `--layout`       | ✖        | `standard` | Project layout: `standard` or `hexagonal`.                                      |
| `--guardrails`   | ✖        | `basic`    | Guardrails mode: `none`, `basic`, `strict` (**default: basic; opt-out: none**). |
| `--sample-code`  | ✖        | `none`     | Sample code level: `none`, `basic`.                                             |
| `--dependency`   | ✖        | –          | Dependency alias (repeatable; controlled set).                                  |
| `--target-dir`   | ✖        | `.`        | Target directory for generated output.                                          |

---

### Dependency Aliases (Controlled)

Dependency aliases are **intentionally constrained** and mapped internally to well-known Spring Boot starters.
This prevents uncontrolled dependency sprawl and keeps generated projects aligned with the architecture-first intent.

**Available aliases in 1.0.0:**

```text
web
data_jpa
validation
actuator
security
devtools
```

* Unknown aliases fail fast during CLI execution.
* `--dependency` is **repeatable**, e.g.:

```bash
--dependency web --dependency actuator --dependency data_jpa
```

---

### Why This Matters

Codegen Blueprint is **not** a free-form dependency injector.
Dependencies are:

* explicitly modeled
* version-aligned with the selected platform
* constrained by design

This ensures generated projects start with:

* a clean dependency graph
* predictable behavior
* architecture-safe defaults

> Dependency freedom is a runtime concern — **architectural intent is a generation-time concern**.

---

### Generated Output (Simplified)

> The output directory name always equals `--artifact-id`.

```text
greeting/
 ├── pom.xml
 ├── .gitignore
 ├── .mvn/
 │   └── wrapper/
 │       └── maven-wrapper.properties
 ├── src/
 │   ├── main/
 │   │   ├── java/io/github/blueprintplatform/greeting/...
 │   │   └── resources/application.yml
 │   └── test/
 │       └── java/io/github/blueprintplatform/greeting/...
```

The output is:

* buildable (`mvn verify`)
* testable (unit + integration baseline)
* architecture-aware by construction

---

### Layout Semantics

**`standard` layout** (layered packages)

```text
controller/
service/
repository/
domain/
config/
```

**`hexagonal` layout** (ports & adapters)

```text
domain/
application/
adapter/
bootstrap/
```

---

### Architecture Guardrails & Feedback Loop

Guardrails are **opt-in** and create a fast, explicit feedback loop while context is still fresh.
They don’t replace design decisions or reviews—they make boundaries **visible, testable, and hard to miss**.

#### Guardrails Modes

| Mode     | Intent & Behavior                                                                                         |
| -------- | --------------------------------------------------------------------------------------------------------- |
| `basic`  | **Default (1.0.0 GA)**. Designed for adoption. Enforces core boundaries without being overly restrictive. |
| `strict` | Recommended for proof-grade, fail-fast validation. Enforces stricter dependency and boundary rules.       |
| `none`   | Explicit opt-out only. Intended for special cases; not recommended for regular use.                       |

> **Default vs recommended**
>
> * Default is `basic` to reduce adoption friction.
> * `strict` is recommended when boundaries must be enforced as a hard, build-time contract.
> * `none` should be a conscious exception.

When enabled, guardrails are generated as executable ArchUnit tests and evaluated during:

```bash
mvn verify
```

Violations fail the build **deterministically**, without application startup and without runtime checks.

### Important Notes

* Codegen Blueprint does **not** generate cross-cutting runtime behavior (security/logging/observability/etc.).
* Those concerns are intended to be delivered and governed via **shared libraries** in later Blueprint Platform phases.
* Generated projects are intentionally minimal, stable, and architecture-first by design.

> Codegen Blueprint optimizes for long-term architectural integrity—not short-term scaffolding volume.

---

## 🧪 Testing & CI (This Repository)

This section describes the CI pipeline of the **Codegen Blueprint repository itself**—not the projects generated by the CLI.

It validates both:

* the generator engine (domain, application, adapter)
* the real output produced by the generator (standard & hexagonal)

### Local verification

```bash
mvn verify
```

Runs unit + integration tests and internal architecture rules for the generator.

### CI overview

GitHub Actions validates the **GA baseline** exhaustively, and uses a **single forward-compat smoke** to detect early breakage.

| JDK         | Purpose                     | What runs                                                           |
| ----------- | --------------------------- | ------------------------------------------------------------------- |
| **Java 21** | **GA baseline (supported)** | Full generator verification + generated-project verification matrix |
| **Java 25** | Forward-compat signal       | One generated-project smoke scenario (strict guardrails + sample)   |

Notes:

* The generator runtime baseline is **Java 21**.
* Generated projects are validated by actually building them (`mvn verify`) in CI.

### Static analysis & reporting

* **CodeQL** — security/static analysis
* **JaCoCo + Codecov** — coverage for the generator codebase (Java 21)

---

### Why This Matters

This CI setup explicitly prevents the class of failures where:

> *“The generator build is green, but the generated project is broken.”*

By validating **real generated projects** across:

* architectural layouts
* guardrails modes
* sample presence
* GA and next-generation JDKs

Codegen Blueprint treats architecture as a **continuously verified contract** —
not a one-time scaffolding decision.

> **If the architecture drifts, the build tells you immediately.**

---

## 🚀 Vision & Roadmap

> Architecture should **execute**, not merely be drawn — and remain observable and verifiable 6, 12, 24 months later.

### 🌟 The Vision

**Blueprint Platform** exists to keep architecture **explicit, testable, and evolvable** across teams and time.

* **Architecture as a product** (structure + guardrails as an executable outcome)
* **Capabilities delivered via libraries + governance** (not copied boilerplate)
* **Consistency that survives scale** (onboarding, upgrades, standards)

From Day Zero to Production — architecture stays **intentional**, **testable**, and **continuously evaluated**.

---

### 🧭 Roadmap Principles (Order Matters)

> This roadmap expresses direction and intent, not contractual guarantees.

Blueprint evolves in intentional layers to protect its core promise:

1. **Prove the contract** (determinism, guardrails, reproducible proof)
2. **Add new delivery surfaces** (CLI → REST) **without changing the core engine**
3. **Deliver cross-cutting capabilities** via **versioned libraries + governance** (no boilerplate generation)
4. **Expand stacks cautiously** only after the proof and contracts mature

> 📌 Ordering matters: capabilities and stack expansion come **after** the architecture contract is proven executable.

---

### 🎯 Roadmap (High Level)

#### Phase 1 — Contract & Proof (1.0.0 GA)
* Deterministic generation + executable guardrails (`none | basic | strict`)
* Standard / Hexagonal layouts (opt-in) + framework-free domain
* CI validates real generated projects (`mvn verify`)

#### Phase 2 — Delivery Surfaces
* REST inbound adapter + interactive onboarding/config UX  
  *(same engine, new entry point — domain surface unchanged)*

#### Phase 3 — Platform Capabilities
* Versioned capabilities (libraries/BOMs + governance): security, observability, resilience, policy packs
* OpenAPI clients as a separate Blueprint module

#### Phase 4 — Stack Expansion
* Gradle, Kotlin, additional frameworks/stacks, broader governance ideas  
  *(introduced only after contracts and proof maturity)*

### 📌 Follow the Roadmap

Community-driven priorities happen in Discussions:

🔗 Participate via [GitHub Discussions](https://github.com/blueprint-platform/codegen-blueprint/discussions)

> For detailed scope and guarantees, see the **Executable Architecture Contract — 1.0.0 GA** and the **Release Discipline** policy docs.

---

### 🧩 Why this matters

| Without Blueprint | With Blueprint |
|---|---|
| Architecture drifts silently | Guardrails make drift visible (build-time) |
| Boilerplate spreads across services | Capabilities delivered via versioned libraries |
| Onboarding takes weeks | Day-zero structure + executable contracts |
| Standards depend on discipline | Standards become observable by construction |

> The platform evolves → projects stay clean → org-wide consistency holds.

Blueprint Platform isn’t “just code generation” — it’s **strategic architectural continuity**.

---

## 🤝 Contributing

Contributions are welcome — especially around:

* Architecture and guardrails improvements
* Profile expansion (adapters, stacks)
* Templates and documentation

Start here:

- 🔗 [GitHub Discussions](https://github.com/blueprint-platform/codegen-blueprint/discussions) — ideas, proposals, design review
- 🔗 [GitHub Issues](https://github.com/blueprint-platform/codegen-blueprint/issues) — bugs, actionable tasks

---

## ⭐ Support

Community feedback and real-world usage help shape the Blueprint Platform’s direction.
If you’re using `codegen-blueprint`, sharing experiences or participating in discussions is the most valuable form of support.

---

## 👤 Maintainer

**Barış Saylı** — Creator & Maintainer
* GitHub: [https://github.com/bsayli](https://github.com/bsayli)
* LinkedIn: [https://www.linkedin.com/in/bsayli](https://www.linkedin.com/in/bsayli)
* Medium: [https://medium.com/@baris.sayli](https://medium.com/@baris.sayli)

---

## 🛡 License

MIT — see [LICENSE](LICENSE).