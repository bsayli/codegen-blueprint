# Executable Architecture Proof — Architecture Enforcement Walkthrough

## Fast Proof (Console-First)

If you want the **GREEN → RED → GREEN** proof in a single command
(no screenshots, just console output):

```bash
cd docs/demo
chmod +x proof-runner.sh
CODEGEN_JAR=../../target/codegen-blueprint-1.0.0.jar ./proof-runner.sh
```

The walkthrough below is the **high-resolution proof**:
screenshots, explanation, and exactly **what failed and why**.

---

This document is the **full, step-by-step proof** that Codegen Blueprint can generate
**executable architectural guardrails** that **fail the build deterministically**
when boundaries drift.

> This is not a diagram.
> This is not a convention.
> This is **architecture enforced at build time**.

---

## Table of Contents

- [Purpose](#purpose)
- [What this is (and is not)](#what-this-is-and-is-not)
- [Preconditions](#preconditions)
- [Part I — Hexagonal Architecture (Ports & Adapters)](#part-i--hexagonal-architecture-ports--adapters)
  - [Generation — Hexagonal + Strict](#1-generation--hexagonal--strict)
  - [Baseline — Clean Hexagonal Flow](#2-baseline--clean-hexagonal-flow)
  - [Intentional Violation — Breaking Hexagonal Isolation](#3-intentional-violation--breaking-hexagonal-isolation)
  - [Result — Hexagonal Build Failure](#4-result--hexagonal-build-failure)
- [Part II — Standard (Layered) Architecture](#part-ii--standard-layered-architecture)
  - [Generation — Standard + Strict](#5-generation--standard--strict)
  - [Baseline — Clean Layered Flow](#6-baseline--clean-layered-flow)
  - [Intentional Violation — Controller → Domain Service Dependency (Illegal)](#7-intentional-violation--controller--domain-service-dependency-illegal)
  - [Result — Standard Build Failure](#8-result--standard-build-failure)
- [What this proves](#what-this-proves)
- [Why this matters](#why-this-matters)

---

## Purpose

This walkthrough proves a single, concrete claim:

> **When strict enforcement is enabled, architectural boundaries become executable rules that are evaluated during the build.**

Specifically, it demonstrates that:

1. Codegen Blueprint generates projects with **explicit architectural models** (Hexagonal or Standard / Layered).
2. With `--enforcement strict`, those models are translated into **generated ArchUnit rules**.
3. Any architectural violation causes **`mvn verify` to fail immediately** — without starting the application.

---

## What this is (and is not)

### ✅ This is

* A **build-time architecture proof**
* A demonstration of **generated enforcement rules**
* A deterministic failure when architectural boundaries are violated
* A comparison across **two different architectural models**

### ❌ This is NOT

* A runtime demo
* A Spring Boot feature showcase
* An ArchUnit tutorial
* A style guide

Nothing is started. Nothing is deployed.

---

## Preconditions

To reproduce this walkthrough, you only need:

* Codegen Blueprint **1.0.0 (or later)**
* A project generated via CLI
* `--enforcement strict` enabled
* No manual modification of the generated enforcement rules

---

# Part I — Hexagonal Architecture (Ports & Adapters)

## 1) Generation — Hexagonal + Strict

The project is generated using the following command:

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli springboot \
  --group-id io.github.blueprintplatform \
  --artifact-id greeting \
  --name "Greeting" \
  --description "Greeting sample built with hexagonal architecture" \
  --package-name io.github.blueprintplatform.greeting \
  --layout hexagonal \
  --enforcement strict \
  --sample-code basic \
  --dependency web \
  --dependency data_jpa \
  --dependency actuator
```

Strict enforcement is enabled from the start.
All architectural rules are **generated**, not handwritten.

---

## 2) Baseline — Clean Hexagonal Flow

At this point:

* No code has been modified
* No shortcuts exist
* All dependencies follow the intended hexagonal direction

Inbound adapters depend **only on application ports**.

**Adapter → Port → Use Case → Domain**

<p align="center">
  <img src="./images/01-hexagonal-clean-controller.png" width="900" alt="Hexagonal clean controller using application port"/>
  <br/>
  <em>Hexagonal controller depending only on an application port</em>
</p>

Run the build:

```bash
mvn verify
```

**Result:**

* ✅ Build passes
* ✅ All ArchUnit rules are satisfied
* ✅ Architecture is valid and enforceable

This establishes the **baseline contract**.

---

## 3) Intentional Violation — Breaking Hexagonal Isolation

To prove enforcement is real, introduce a deliberate violation.

The controller is modified to depend directly on an **application implementation or domain service**, bypassing the port.

Important observations:

* The code still **compiles**
* The change looks harmless
* A code review could miss it

But architecturally, it is illegal.

<p align="center">
  <img src="./images/02-hexagonal-controller-domain-violation.png" width="900" alt="Hexagonal controller violating ports isolation"/>
  <br/>
  <em>Inbound adapter directly depending on an implementation (violation)</em>
</p>

Nothing is started. No runtime behavior exists.
Only the dependency direction changed.

---

## 4) Result — Hexagonal Build Failure

Run the exact same build again:

```bash
mvn verify
```

The build fails immediately.

<p align="center">
  <img src="./images/03-hexagonal-archunit-failure.png" width="900" alt="Hexagonal ArchUnit failure"/>
  <br/>
  <em>Generated ArchUnit rule failing the build</em>
</p>

Key facts:

* ❌ No application startup
* ❌ No runtime checks
* ❌ No human enforcement

The architecture failed **by construction**.

---

# Part II — Standard (Layered) Architecture

## 5) Generation — Standard + Strict

A second project is generated using the **standard layered model**:

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli springboot \
  --group-id io.github.blueprintplatform \
  --artifact-id greeting-standard \
  --name "Greeting (Standard Layered)" \
  --description "Greeting sample built with standard layered architecture" \
  --package-name io.github.blueprintplatform.greeting \
  --layout standard \
  --enforcement strict \
  --sample-code basic \
  --dependency web \
  --dependency data_jpa \
  --dependency actuator
```

The same enforcement mode is used, but **different architectural rules apply**.

---

## 6) Baseline — Clean Layered Flow

In the standard model:

**Controller → Service → Domain**

Controllers are allowed to depend on services,
but **must never depend on domain types directly**.

<p align="center">
  <img src="./images/01-standard-clean-controller.png" width="900" alt="Standard layered clean controller"/>
  <br/>
  <em>Standard controller respecting layered boundaries</em>
</p>

Run the build:

```bash
mvn verify
```

**Result:**

* ✅ Build passes
* ✅ All layered rules are satisfied
* ✅ Architecture is valid

---

## 7) Intentional Violation — Controller → Domain Service Dependency (Illegal)

Now introduce a deliberate boundary violation by injecting and calling a **domain service**
directly from the controller.

Important observations:

* The code still **compiles**
* The application would **still run**
* The change looks harmless — and reviewers may miss it

But architecturally it is illegal in **strict standard layered enforcement**:

> **Controllers must not depend on domain services.**
>
> Domain models may be used internally (e.g., in mappers),
> but orchestration and behavior must be reached via the service layer.

<p align="center">
  <img src="./images/02-standard-domain-violation.png" width="900" alt="Standard controller directly depending on a domain service (violation)"/>
  <br/>
  <em>Controller directly depending on a domain service (intentional violation)</em>
</p>

---

## 8) Result — Standard Build Failure

Run the build again:

```bash
mvn verify
```

The build fails deterministically.

<p align="center">
  <img src="./images/03-standard-archunit-failure.png" width="900" alt="Standard ArchUnit failure"/>
  <br/>
  <em>Layered architecture violation detected at build time</em>
</p>

---

## What this proves

**Different architectures. Different rules. Same outcome.**

* Hexagonal enforces **ports & adapter isolation**
* Standard enforces **layered dependency direction**
* Both are enforced **automatically**
* Both fail the build when violated

No documentation is consulted.
No conventions are trusted.

---

## Why this matters

This is the practical difference between:

* **Documented architecture** — can be ignored
* **Executable architecture** — cannot be ignored

With Codegen Blueprint:

* Architecture is generated once
* Guardrails are executable
* Drift is detected immediately

> **If a rule is violated, the build breaks.**

No discussions.

No conventions.

No exceptions.

That is what **Architecture as a Product** means in practice.

---

## Generated Project Output (Optional)

This walkthrough focused on **proving enforcement** — not on showcasing generated artifacts.

If you want to see what **actual projects generated by Codegen Blueprint look like**, including:

* the exact `README.md` files written into generated projects
* how architecture, enforcement, and sample code are explained to developers
* what a team receives *after* generation

see:

→ **Generated Project README Previews**  
[Generated Readmes](./generated-readmes.md)

These READMEs are **real generator output**, not examples.
They represent the **human-facing side of the same architecture contract** enforced in this walkthrough.
