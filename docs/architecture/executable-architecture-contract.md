# Executable Architecture Contract — 1.0.0 GA

**What Codegen Blueprint 1.0.0 GA guarantees, evaluates, and produces.**

This document defines the **exact, non-negotiable architecture guarantees**
provided by Codegen Blueprint **1.0.0 GA** at generation and build time.

> ⚠️ **GA Contract**
>
> If something is **not explicitly listed in this document**,
> it is **NOT guaranteed** as part of the 1.0.0 GA release.
---

## 📚 Table of Contents

* [1 Purpose](#1-purpose)
* [2 Core Mental Model](#2-core-mental-model)
* [3 Engine Guardrail Guarantees (1.0.0 GA)](#3-engine-guardrails-guarantees-100-ga)
* [4 Generated Project Scope (Output Contract)](#4-generated-project-scope-output-contract)
* [5 Explicitly Not Guardrailed (Yet)](#5-explicitly-not-guardrailed-yet)
* [6 Intentional Scope Constraints](#6-intentional-scope-constraints)
* [7 Path Toward Executable Architecture](#7-path-toward-executable-architecture)
* [8 Review Guidance](#8-review-guidance)

---

## 1️⃣ Purpose

This document exists to ensure that:

* README **claims** always match **engine guarantees**
* Generated output is **predictable**, **testable**, and **architecturally honest**
* The foundation enables **stronger guardrails** without breaking trust

> 🧠 **If we promise it, the build makes it observable.**

Anything not explicitly listed here is **out of scope** for 1.0.0 GA.

---

## 2️⃣ Core Mental Model

| Concept       | Description                                                   |
| ------------- | ------------------------------------------------------------- |
| **Engine**    | CLI‑driven generator applying architectural profiles           |
| **Profiles**  | Architecture + runtime stack + generation rules               |
| **Artifacts** | Generated project assets (structured, ordered, and validated) |

The engine today:

> Generates clean, production‑viable Spring Boot services with **architecture‑aware** and **test‑ready** output.

---

## 3️⃣ Engine Guardrails Guarantees (1.0.0 GA)

All guarantees listed in this section are **validated by automated tests**.

This section explicitly distinguishes between:

* **mandatory guardrails inside the Codegen Blueprint engine itself**, and
* **optional guardrails generated into produced projects**.

---

### ✔ 3.1 Deterministic Output (Structure)

For the active GA profile, the engine deterministically produces a **single-module**, buildable project with a **contracted output file set**.

The authoritative list of generated files and paths is defined in **Section 4: Generated Project Scope (Output Contract)**.

> Same inputs → same output (structure + rendered content): no ordering randomness, no environment-dependent behavior.
---

### ✔ 3.2 Identity & Naming Validation

The engine validates **consistency and correctness** of:

* `groupId`
* `artifactId`
* base package name
* PascalCase main class → `<ArtifactId>Application`

> ❌ Invalid identifiers cause **fail‑fast termination**.

---

### ✔ 3.3 Minimal Runtime Baseline

Every generated project:

* Compiles and starts without additional configuration
* Includes **only explicitly requested dependencies**
* Starts via `SpringApplication.run()`

📌 No demo leftovers. No accidental scaffolding.

---

### ✔ 3.4 Test‑Ready Output

Generated projects always:

* Contain a Spring test bootstrap (`@SpringBootTest`)
* Pass `mvn verify` immediately after generation

Testing is **not optional**.

---

### ✔ 3.5 Engine–Template Separation

The Codegen Blueprint engine **does not depend on**:

* Spring Framework
* File system APIs
* Build tools (Maven / Gradle)

Technology details live exclusively in **adapters and profiles**.

> This guarantees future support for Gradle, Kotlin, Quarkus — without engine refactoring.

---

### ✔ 3.6 Profile‑Defined Execution

Profiles fully determine:

* Artifact ordering
* Template namespaces
* Architecture layout semantics

Example:

```bash
java -jar codegen-blueprint.jar --cli springboot ...
```

The engine executes — profiles decide *what* and *how*.

---

### ✔ 3.7 Generator & Generated Project Architecture Guardrails

#### Engine‑Level (Mandatory)

The Codegen Blueprint codebase validates **its own architecture** using automated tests (ArchUnit):

* Domain purity
* Strict dependency direction
* Port and adapter isolation

These guarantees apply **unconditionally** to the generator itself.

#### Generated Project (Optional)

The engine can optionally generate **architecture guardrails tests** into produced projects:

* Generated only when enabled via profile / guardrails mode
* Implemented as executable ArchUnit tests
* Focused on **structural boundaries** (e.g. layered or hexagonal layouts)

⚠️ Generated-project guardrails are **opt-in** in 1.0.0 GA and never implicit.

---

## 4️⃣ Generated Project Scope (Output Contract)

### Active Stack (GA)

```text
springboot-maven-java
```

### Baseline output (always generated)

**Naming:** `<MainApplicationClass> = PascalCase(<artifactId>) + "Application"`

Every generated project is **single-module** and includes:

```text
<artifactId>/
 ├─ pom.xml (with Maven Wrapper)
 ├─ .mvn/wrapper/maven-wrapper.properties
 ├─ mvnw
 ├─ mvnw.cmd
 ├─ src/main/java/<basePackage>/<MainApplicationClass>.java
 ├─ src/test/java/<basePackage>/<MainApplicationClass>Tests.java
 ├─ src/main/resources/application.yml
 ├─ .gitignore
 └─ README.md
```

> `README.md` and generated ArchUnit tests (when enabled via --guardrails) are part of the **delivered contract surface**.


---

### Optional Layout — Hexagonal

Enabled via:

```text
--layout hexagonal
```

Generated package families (Ports & Adapters) under:

src/main/java/<basePackage>/

```text
adapter/      # technology surfaces
application/  # orchestration (use cases, ports)
domain/       # business rules (framework-free)
bootstrap/    # wiring
```

> When `--guardrails` is enabled, the generated ArchUnit guardrails validate these package boundaries at build time (`mvn verify`).

---

### Optional Teaching Example — Sample Code

Enabled via:

```
--sample-code basic
```

Produces:

* A minimal REST greeting endpoint
* A domain‑driven reference slice

Runnable immediately:

```bash
./mvnw spring-boot:run
```

> Clean. Runnable. Understandable.

---

## 5️⃣ Explicitly Not Guardrailed (Yet)

The following are **intentionally out of scope** for 1.0.0 GA:

| Item                                     | Reason                        |
| ---------------------------------------- |-------------------------------|
| Hexagonal layout by default              | Zero‑friction adoption        |
| Policy engine / DSL                      | Requires governance language  |
| Custom / policy‑level architecture rules | Next guardrails stage         |
| Org‑wide standards                       | Platform‑level concern        |

> Today: architecture‑aware → Tomorrow: architecture‑policed

---

## 6️⃣ Intentional Scope Constraints

* 🚫 No bloated opinions
* 🚫 No magical side‑effects
* 🚫 No drift from declared contracts
* 🎯 Precision over volume

> Narrow now → **explosive ecosystem later**

---

## 7️⃣ Path Toward Executable Architecture

This section outlines the **direction of evolution**, not additional GA guarantees.

It intentionally avoids feature timelines and implementation details.
The purpose is to clarify **how executable architecture matures over time**,
while keeping the **1.0.0 GA contract strict and non-negotiable**.

> 📌 Ordering matters — each step builds on the previous one.

| Stage | Capability                        | Value                          |
| ----: | --------------------------------- | ------------------------------ |
|  Next | Layout guardrails gates          | Structural boundaries evaluated |
|  Next | CI-level architecture validation  | Drift fails fast               |
|  Next | Policy DSL                        | Governance as code             |
| Later | Org-wide profiles                 | Team-scale compliance          |

Detailed roadmap, delivery surfaces, and capability expansion
are documented separately in the main README and are **not part of this GA contract**.

> Best practices must **execute — not be suggestions**.

---

## 8️⃣ Review Guidance

Any change touching architecture must answer:

> ❓ **Does this change claim guardrails?**

* If **yes** → update this document
* If **no** → adjust README only

---

### Final Statement

**Codegen Blueprint 1.0.0 GA generates:**

* Clean and testable services
* Architecture‑aware structure
* Predictable foundations for evolution

> 🚀 **Executable Architecture becomes observable — and testable — here.**
