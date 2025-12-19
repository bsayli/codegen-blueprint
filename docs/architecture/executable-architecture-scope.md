# Architecture Enforcement Scope — Codegen Blueprint 1.0.0 GA

> This unified document defines what the **Codegen Blueprint engine enforces today (1.0.0 GA)** and what the **generated project guarantees at output** — serving as a single, authoritative reference for architectural truth.

---

## 📚 Table of Contents

* [1 Purpose](#1-purpose)
* [2 Core Mental Model](#2-core-mental-model)
* [3 Engine Enforcement Guarantees (1.0.0 GA)](#3-engine-enforcement-guarantees-100-ga)
* [4 Generated Project Scope (Output Contract)](#4-generated-project-scope-output-contract)
* [5 Explicitly Not Enforced (Yet)](#5-explicitly-not-enforced-yet)
* [6 Intentional Scope Constraints](#6-intentional-scope-constraints)
* [7 Path Toward Executable Architecture](#7-path-toward-executable-architecture)
* [8 Review Guidance](#8-review-guidance)

---

## 1️⃣ Purpose

This document exists to ensure that:

* README **claims** always match **engine guarantees**
* Generated output is **predictable**, **testable**, and **architecturally honest**
* The foundation enables **stronger enforcement** without breaking trust

> 🧠 **If we promise it, we enforce it.**

Anything not explicitly listed here is **out of scope** for 1.0.0 GA.

---

## 2️⃣ Core Mental Model

| Concept       | Description                                                   |
| ------------- | ------------------------------------------------------------- |
| **Engine**    | CLI‑driven executor applying architectural profiles           |
| **Profiles**  | Architecture + runtime stack + generation rules               |
| **Artifacts** | Generated project assets (structured, ordered, and validated) |

The engine today:

> Generates clean, production‑viable Spring Boot services with **architecture‑aware** and **test‑ready** output.

---

## 3️⃣ Engine Enforcement Guarantees (1.0.0 GA)

All guarantees listed in this section are **validated by automated tests**.

This section explicitly distinguishes between:

* **mandatory enforcement inside the Codegen Blueprint engine itself**, and
* **optional enforcement generated into produced projects**.

---

### ✔ 3.1 Deterministic Layout

The engine always produces a **single‑module**, buildable project:

```
<artifactId>/
 ├─ pom.xml
 ├─ src/main/java/<basePackage>/
 ├─ src/test/java/<basePackage>/
 ├─ src/main/resources/application.yml
 ├─ .gitignore
 └─ README.md
```

No hidden modules. No conditional directories.

---

### ✔ 3.2 Identity & Naming Enforcement

The engine validates **consistency and correctness** of:

* `groupId`
* `artifactId`
* base package name
* PascalCase main class → `<ArtifactId>Application`

> ❌ Invalid identifiers cause **fail‑fast termination**.

---

### ✔ 3.3 Minimal Runtime Baseline

Every generated project:

* Compiles and boots immediately
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

### ✔ 3.7 Generator & Generated Project Architecture Enforcement

#### Engine‑Level (Mandatory)

The Codegen Blueprint codebase enforces **its own architecture** using automated tests (ArchUnit):

* Domain purity
* Strict dependency direction
* Port and adapter isolation

These guarantees apply **unconditionally** to the generator itself.

#### Generated Project (Optional)

The engine can optionally generate **architecture enforcement tests** into produced projects:

* Generated only when enabled via profile / enforcement mode
* Implemented as executable ArchUnit tests
* Focused on **structural boundaries** (e.g. layered or hexagonal layouts)

⚠️ Generated‑project enforcement is **opt‑in** in 1.0.0 GA and never implicit.

---

## 4️⃣ Generated Project Scope (Output Contract)

### Active Stack (GA)

```
springboot-maven-java
```

Every generated project includes:

```
<artifactId>/
 ├─ pom.xml
 ├─ src/main/java/<basePackage>/Application.java
 ├─ src/test/java/<basePackage>/ApplicationTests.java
 ├─ src/main/resources/application.yml
 ├─ .gitignore
 └─ README.md
```

---

### Optional Layout — Hexagonal

Enabled via:

```
--layout hexagonal
```

Enforced structural boundaries:

```
adapter/    # technology surfaces
application/ # orchestration
domain/      # business rules
bootstrap/   # wiring
```

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

## 5️⃣ Explicitly Not Enforced (Yet)

The following are **intentionally out of scope** for 1.0.0 GA:

| Item                                     | Reason                       |
| ---------------------------------------- | ---------------------------- |
| Hexagonal layout by default              | Zero‑friction adoption       |
| Policy engine / DSL                      | Requires governance language |
| Custom / policy‑level architecture rules | Next enforcement stage       |
| Org‑wide standards                       | Platform‑level concern       |

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

| Phase | Capability                       | Value                     |
| ----: | -------------------------------- | ------------------------- |
|  Next | Layout enforcement gates         | Real boundaries in output |
|  Next | CI‑level architecture validation | Drift fails fast          |
|  Next | Policy DSL                       | Governance as code        |
| Later | Org‑wide profiles                | Team‑scale compliance     |

> Best practices must **execute — not be suggestions**.

---

## 8️⃣ Review Guidance

Any change touching architecture must answer:

> ❓ **Does this change claim enforcement?**

* If **yes** → update this document
* If **no** → adjust README only

---

### Final Statement

**Codegen Blueprint 1.0.0 GA generates:**

* Clean and testable services
* Architecture‑aware structure
* Predictable foundations for evolution

> 🚀 **Executable Architecture begins here.**
