# Engine Scope — Codegen Blueprint 1.0.0

> **What this document is:**
> The **formal contract** describing what Codegen Blueprint **guarantees** in 1.0.0.
>
> **What this document is not:**
> A list of future intentions or marketing claims.

---

## 📑 Table of Contents

* [1. Purpose](#1-purpose)
* [2. Core Mental Model](#2-core-mental-model)
* [3. Enforcement Guarantees (1.0.0)](#3-enforcement-guarantees-100)

   * [3.1 Deterministic Project Layout](#31-deterministic-project-layout)
   * [3.2 Naming & Identity Enforcement](#32-naming--identity-enforcement)
   * [3.3 Spring Boot Minimal Runtime Baseline](#33-spring-boot-minimal-runtime-baseline)
   * [3.4 Test-Ready Project](#34-test-ready-project)
   * [3.5 Separation of Engine & Templates](#35-separation-of-engine--templates)
   * [3.6 Profile-Driven Execution](#36-profile-driven-execution)
* [4. Explicitly Not Enforced (Yet)](#4-explicitly-not-enforced-yet)
* [5. Intentional Scope Constraints](#5-intentional-scope-constraints)
* [6. Path Toward Executable Architecture](#6-path-toward-executable-architecture)
* [7. Review Guidance](#7-review-guidance)

---

## 1. Purpose

This document ensures:

* ✔ Alignment between README promises and actual engine behavior
* ✔ Predictability for early adopters
* ✔ A stable baseline for future architectural enforcement

> If the README **claims** it, the engine **must** guarantee it — and it must be reflected here.

---

## 2. Core Mental Model

| Concept        | Definition                                                                   |
| -------------- | ---------------------------------------------------------------------------- |
| **Platform**   | Long-term home for engines, enforcement & governance (`blueprint-platform`)  |
| **Engine**     | CLI-based generator that applies profiles (`codegen-blueprint`)              |
| **Profile**    | Defines build tool + language + technology stack (`springboot-maven-java`)   |
| **Blueprints** | Template artifacts selected by profile (e.g., `POM_XML`, `APPLICATION_YAML`) |

The engine’s job in 1.0.0:

> **Generate a clean, production-ready Spring Boot skeleton**
> that is prepared — but not yet forced — to follow architectural constraints.

---

## 3. Enforcement Guarantees (1.0.0)

These behaviors are **strictly required** and validated through tests.

### 3.1 Deterministic Project Layout

Generated structure must follow:

```
<artifactId>/
 ├─ pom.xml
 ├─ src/main/java/<basePackage>/
 ├─ src/test/java/<basePackage>/
 ├─ src/main/resources/application.yml
 ├─ .gitignore
 └─ README.md (minimal)
```

*always single-module*

---

### 3.2 Naming & Identity Enforcement

Engine normalizes and applies:

* ✔ `groupId`
* ✔ `artifactId`
* ✔ project name
* ✔ Java package namespace

Main class naming rule:

```
<PascalCasedArtifact>Application
```

Misformatted identifiers → **fail early**
(no silently invalid layout)

---

### 3.3 Spring Boot Minimal Runtime Baseline

Generated project must:

* ✔ Build successfully using Maven
* ✔ Run `SpringApplication.run(...)` out-of-the-box
* ✔ Include **only** core starters specified by dependencies

No demo controllers
No accidental architectural noise

---

### 3.4 Test-Ready Project

Generated project must:

* ✔ Include `@SpringBootTest` entrypoint
* ✔ Mirror source layout in test tree
* ✔ Pass `mvn verify` immediately after generation

Testing is not optional.

---

### 3.5 Separation of Engine & Templates

Engine **never** imports:

* 🚫 Spring
* 🚫 File system logic
* 🚫 Maven implementation details

All stack-specific concerns are owned by:

* Profiles
* Template layer

This enables future:

> Gradle, Kotlin, Quarkus… without touching core engine.

---

### 3.6 Profile-Driven Execution

Generation always activated via profile:

```bash
java -jar codegen-blueprint.jar \
  --cli \
  springboot \
  --group-id com.acme \
  --artifact-id order-service \
  --name "Order Service" \
  --package-name com.acme.order \
  --layout hexagonal \    # optional
  --dependency web \
  --dependency validation
```

Profiles decide:

* ✔ structure
* ✔ templates
* ✔ technology capabilities

---

## 4. Explicitly Not Enforced (Yet)

| Not Included                      | Reason                                      |
| --------------------------------- | ------------------------------------------- |
| Hexagonal structure by default    | Optional for now to avoid adoption friction |
| Compile-time boundary enforcement | Requires future policy engine               |
| ArchUnit generation               | Milestone after hexagonal scaffolding       |
| Governance across repos           | Will arrive with org-level profiles         |

> We are **architecture-aware**, not yet **architecture-policing**.

---

## 5. Intentional Scope Constraints

* 1️⃣ Trust first — enforce later
* 2️⃣ Focused quality > bloated footprint
* 3️⃣ Evolution without rewriting core engine

> Narrow now → scalable tomorrow

---

## 6. Path Toward Executable Architecture

| Stage     | Capability                         | Effect                        |
| --------- | ---------------------------------- | ----------------------------- |
| **v1.1+** | Layout-aware hexagonal scaffolding | Real boundaries in generation |
| **v1.2+** | Auto-arch tests                    | Prevent silent drift          |
| **v1.3+** | Policy DSL + CI enforcement        | Architecture as quality gates |
| **v2.0**  | Org profiles                       | Governance at scale           |

This roadmap exists **outside** enforcement scope for 1.0.0.

---

## 7. Review Guidance

Every PR must validate:

> Does this change claim enforcement behavior?

If **yes** → update this document
If **not** → ensure claims remain in README roadmap

> 🔑 This document is the **source of truth** for what users can rely on **today**.

---

### Final statement

**Codegen Blueprint 1.0.0**:
A clean, testable, profile-driven Spring Boot starting point
built for **real enforcement** in the versions that follow.
