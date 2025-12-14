# Architecture Enforcement Scope — Codegen Blueprint 1.0.0 GA

> This unified document defines what the **Codegen Blueprint engine enforces today (1.0.0 GA)** and what the **generated project guarantees at output** — a single reference point for architectural truth.

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

Ensure that:

* README **claims** match **engine guarantees**
* Output is **predictable**, **testable**, and **clean**
* The foundation enables **strict enforcement** as we evolve

> 🧠 If we promise it, we enforce it.

---

## 2️⃣ Core Mental Model

| Concept       | Description                                         |
| ------------- | --------------------------------------------------- |
| **Engine**    | CLI‑driven executor applying architectural profiles |
| **Profiles**  | Architecture + runtime stack + generation rules     |
| **Artifacts** | Generated project assets (structured + validated)   |

📌 The engine today:

> Generates clean, production‑viable Spring Boot services — with **architecture‑aware** and **test‑ready** output.

---

## 3️⃣ Engine Enforcement Guarantees (1.0.0 GA)

All items validated via automated tests.

### ✔ 3.1 Deterministic Layout

Always **single‑module** + buildable output:

```
<artifactId>/
 ├─ pom.xml
 ├─ src/main/java/<basePackage>/
 ├─ src/test/java/<basePackage>/
 ├─ src/main/resources/application.yml
 ├─ .gitignore
 └─ README.md
```

### ✔ 3.2 Identity & Naming Enforcement

Engine validates **consistency & correctness**:

* groupId
* artifactId
* base package
* PascalCase main class → `<Artifact>Application`

> ❌ Invalid identifiers → **fail fast**

### ✔ 3.3 Minimal Runtime Baseline

Project must:

* Compile + run instantly
* Include only explicit dependencies
* Boot through SpringApplication.run()

📌 No accidental demo code.

### ✔ 3.4 Test‑Ready Output

Generated project must:

* Contain test bootstrap (`@SpringBootTest`)
* Pass `mvn verify` immediately after creation

Testing == required.

### ✔ 3.5 Engine–Template Separation

Engine **does not depend on**:

* Spring
* File system
* Build systems (Maven, Gradle…)

Technology lives in **adapters + profiles**.

> Enables Gradle/Kotlin/Quarkus — **zero** engine refactor.

### ✔ 3.6 Profile‑Defined Execution

Profile determines:

* Artifact ordering
* Template namespace
* Architecture boundaries

Example:

```bash
java -jar codegen-blueprint.jar --cli springboot ...
```

### ✔ 3.7 Generator Architecture Enforcement (Internal)

The Codegen Blueprint engine itself enforces its own architectural boundaries
using automated architecture tests (ArchUnit).

This guarantees:

* domain purity inside the generator
* strict dependency direction
* adapter and port isolation

⚠️ This enforcement applies to the **generator codebase itself** —
not to generated projects (yet).

---

## 4️⃣ Generated Project Scope (Output Contract)

### Active Stack (GA)

```
springboot-maven-java
```

Output must include:

```
<artifactId>/
 ├─ pom.xml
 ├─ src/main/java/<basePackage>/Application.java
 ├─ src/test/java/<basePackage>/ApplicationTests.java
 ├─ src/main/resources/application.yml
 ├─ .gitignore
 └─ README.md
```

### Optional Layout — Hexagonal

```
--layout hexagonal
```

Adds enforceable boundaries:

```
adapter/    # tech surfaces
application # orchestration
domain      # business rules
bootstrap   # wiring
```

### Optional Teaching Example — Sample Code

```
--sample-code basic
```

Produces:

* REST greeting endpoint
* Domain‑driven reference

Run instantly:

```bash
./mvnw spring-boot:run
```

> Clean. Runnable. Understandable.

---

## 5️⃣ Explicitly Not Enforced (Yet)

We **intentionally** do not enforce:

| Item                    | Why                             |
| ----------------------- | ------------------------------- |
| Hexagonal by default    | Zero‑friction adoption          |
| Policy engine           | Requires DSL + governance model |
| Architecture test rules | Next stage of enforceability    |
| Org‑wide standards      | Platform‑level roadmap          |

> Today: architecture‑aware → Tomorrow: architecture‑policed

---

## 6️⃣ Intentional Scope Constraints

* 🚫 No bloated opinions
* 🚫 No magical side‑effects
* 🚫 No drift from contract
* 🎯 Precision > volume

> Narrow now → **explosive ecosystem later**

---

## 7️⃣ Path Toward Executable Architecture

| Stage | Capability                   | Value                            |
| ----: | ---------------------------- | -------------------------------- |
|  v1.1 | Layout enforcement gates     | Real boundaries in code output   |
|  v1.2 | Auto‑architecture validation | CI fails on drift                |
|  v1.3 | Policy DSL                   | Governance as code               |
|  v2.0 | Org‑wide profiles            | Team‑scale compliance automation |

> Best practices must **execute — not be suggestions**

---

## 8️⃣ Review Guidance

Any change touching architecture must ask:

> ❓ Does this change **claim** enforcement?

If **yes** → update this document
If **no** → adjust README only

---

### Final Statement

**Codegen Blueprint 1.0.0 GA generates:**

* Clean & testable services
* Architecture‑aware structure
* Predictable foundations for evolution

> 🚀 **Executable Architecture begins here.**
