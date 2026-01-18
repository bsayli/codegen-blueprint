# Executable Architecture Proof — Guardrails Enforcement Walkthrough

This document provides an end-to-end **GREEN → RED → GREEN** proof that Codegen Blueprint guardrails are **generated and enforced at build time**.

* If you want the fastest, deterministic proof: start with **Fast Proof (Console-First)**.
* If you want human-readable evidence (screenshots + rule + failure output): continue with the **High-Resolution Walkthrough**.

---

## Table of Contents

- [Fast Proof (Console-First)](#fast-proof-console-first)
- [What this proves](#what-this-proves)
- [Proof Flow (High-Level)](#proof-flow-highlevel)
- [Proof Output (Persistent, Inspectable Artifacts)](#proof-output-persistent-inspectable-artifacts)
  - [Per-run directory structure](#perrun-directory-structure)
- [What each artifact means](#what-each-artifact-means)
  - [`logs/`](#logs)
  - [`excerpts/`](#excerpts)
  - [`env.txt`](#envtxt)
  - [`proof-summary.txt`](#proof-summarytxt)
- [Why this matters](#why-this-matters)
- [Notes](#notes)
- [High-Resolution Walkthrough (Manual Proof)](#high-resolution-walkthrough-manual-proof)
  - [Purpose](#purpose)
  - [What this is (and is not)](#what-this-is-and-is-not)
  - [Preconditions](#preconditions)
  - [Walkthrough Structure (Applies to Every Case)](#walkthrough-structure-applies-to-every-case)
- [Part I — Hexagonal Architecture (Ports & Adapters)](#part-i--hexagonal-architecture-ports--adapters)
  - [Case 1 — Hexagonal Strict: Adapter → Application Implementation (Port Bypass)](#case-1--hexagonal-strict-adapter--application-implementation-port-bypass)
    - [1) Generation — Hexagonal + Strict](#1-generation--hexagonal--strict)
    - [2) Baseline — Clean Hexagonal Flow (GREEN)](#2-baseline--clean-hexagonal-flow-green)
    - [3) Intentional Violation — Break Ports Isolation (RED trigger)](#3-intentional-violation--break-ports-isolation-red-trigger)
    - [4) Result — Hexagonal Build Failure (RED evidence)](#4-result--hexagonal-build-failure-red-evidence)
- [Part II — Standard (Layered) Architecture](#part-ii--standard-layered-architecture)
  - [Case 2 — Standard Strict: Controller → Repository (Repository Bypass)](#case-2--standard-strict-controller--repository-repository-bypass)
    - [5) Generation — Standard + Strict](#5-generation--standard--strict)
    - [6) Baseline — Controller Does Not Depend on Repository (GREEN)](#6-baseline--controller-does-not-depend-on-repository-green)
    - [7) Intentional Violation — Controller Depends on Repository (RED trigger)](#7-intentional-violation--controller-depends-on-repository-red-trigger)
    - [8) Result — Standard Build Failure (RED evidence)](#8-result--standard-build-failure-red-evidence)
  - [Case 3 — Standard Schema Sanity: Missing Canonical Family (controller renamed)](#case-3--standard-schema-sanity-missing-canonical-family-controller-renamed)
    - [9) Baseline — Standard Schema Intact (GREEN)](#9-baseline--standard-schema-intact-green)
    - [10) Intentional Violation — Break Canonical Schema (RED trigger)](#10-intentional-violation--break-canonical-schema-red-trigger)
    - [11) Result — Schema Sanity Failure (RED evidence)](#11-result--schema-sanity-failure-red-evidence)
- [What this proves](#what-this-proves-1)
- [Why this matters](#why-this-matters-1)


## Fast Proof (Console-First)

> 🔎 Prefer visuals instead of running the build?  
> → Jump to the **[High-Resolution Walkthrough (Manual Proof)](#high-resolution-walkthrough-manual-proof)**  
> → Back to **[Table of Contents](#table-of-contents)**

> **Execution precondition**
>
> This proof assumes you are running from a local clone of the
> `codegen-blueprint` repository and have already built the generator JAR.
> This section documents the **verification protocol**, not initial setup.

If you want to see the **GREEN → RED → GREEN** proof (**strict architecture guardrails**)  
**purely via the console** — no screenshots, no explanations, just deterministic build output — run:

```bash
# From the repository root
cd docs/demo
chmod +x ./proof/proof-runner.sh
CODEGEN_JAR="$(ls -1 ../../target/codegen-blueprint-*.jar | head -n 1)" ./proof/proof-runner.sh
```

The command exits with a **non-zero code on any contract violation or unexpected behavior**
and prints a concise, step-by-step execution status to the console.

---

## What this proves

This single command demonstrates — end to end — that Codegen Blueprint can:

* generate a real project with **strict architecture guardrails**
* evaluate those guardrails **at build time** (`mvn verify`)
* fail the build **deterministically** when a boundary is violated
* return to green immediately once the violation is removed

### Proof characteristics

* No application run (no `spring-boot:run`, no `java -jar`)
* No runtime enforcement — guardrails are evaluated at build time
* No custom test framework beyond standard JUnit + ArchUnit

> **Architecture is evaluated by the build itself.**

---

## Proof Flow (High‑Level)

The script executes the following sequence for **both Hexagonal and Standard (Layered)** architectures:

```
Generate project
→ mvn verify (GREEN)
→ inject boundary violation
→ mvn verify (RED)
→ revert violation
→ mvn verify (GREEN)
```

This is performed for **real generated code**, not mocks, examples, or pre‑canned test fixtures.

---

## Proof Output (Persistent, Inspectable Artifacts)

In addition to console output, **each run produces a durable proof bundle** under the repository:

```
docs/demo/proof-output/
```

### Per‑run directory structure

Each execution creates a **timestamped run directory**:

```
docs/demo/proof-output/
└── 20260104-180325/
    ├── logs/
    │   ├── HEX_baseline.log
    │   ├── HEX_violation.log
    │   ├── HEX_fixed.log
    │   ├── STD_baseline.log
    │   ├── STD_schema_violation.log
    │   ├── STD_schema_fixed.log
    │   ├── STD_violation.log
    │   └── STD_fixed.log
    ├── excerpts/
    │   ├── HEX_violation.excerpt.txt
    │   ├── STD_schema_violation.excerpt.txt
    │   └── STD_violation.excerpt.txt
    ├── env.txt
    └── proof-summary.txt
```

---

## What each artifact means

### `logs/`

* Full, raw build output (`mvn verify`) for each proof step
* Contains complete ArchUnit failure messages and stack traces
* Suitable for deep inspection, CI attachments, or audits

### `excerpts/`

* Focused slices extracted from logs
* Centered around **ArchUnit / guardrails violations**
* Designed for fast human review without scanning full logs

### `env.txt`

Captured execution context, including:

* OS and shell
* Java version
* Maven version
* Codegen Blueprint JAR path
* Timestamp of the run

This ensures the proof is **reproducible and attributable**.

### `proof-summary.txt`

A machine‑readable and human‑readable execution ledger:

* ordered proof steps
* PASS / EXPECTED_FAIL / UNEXPECTED_PASS statuses
* exact log and excerpt file references
* final proof result

This file is the **primary evidence ledger** that the proof executed as intended.

---

## Why this matters

Console output proves **something happened**.

The `proof-output` directory proves:

* **what** happened
* **where** it failed
* **why** it failed
* **that it recovered deterministically**

This transforms guardrails from:

*“trust me, it works”*

into:

> **inspectable, replayable, build‑time evidence**.

---

## Notes

* Each run is append‑only; older runs are preserved unless manually removed
* For deeper inspection of generated projects, run with:

```bash
KEEP_WORK_DIR=1 ./proof-runner.sh
```

---

> This is not documentation.
> This is not a convention.
>
> **This is architecture enforced, evaluated, and proven at build time.**

---

## Why there is a detailed walkthrough below

The remainder of this document is the **high‑resolution proof** —
the **human‑readable version of the console‑first proof**:

* screenshots of the generated structure
* the exact minimal code change that introduces the violation *(same as the script injects)*
* the precise generated ArchUnit rule that fails
* the proof artifacts showing **why** it failed:

  * console output
  * `proof-output/` logs and excerpts

This is intentional.

> Executable architecture is only convincing when failure is **observable and explainable** —
> not just asserted.

---

> This is not a diagram.
> This is not a convention.
> This is **architecture evaluated at build time**.

---

## High-Resolution Walkthrough (Manual Proof)

> ⚡ Want the fastest possible proof?  
> → Jump to **[Fast Proof (Console-First)](#fast-proof-console-first)**  
> → Back to **[Table of Contents](#table-of-contents)**

### Purpose

This walkthrough proves a single, concrete claim:

> **When strict guardrails mode is enabled, architectural boundaries become executable rules that are evaluated during the build.**

Specifically, it demonstrates that:

1. Codegen Blueprint generates projects with **explicit architectural models** (Hexagonal or Standard / Layered).
2. With `--guardrails strict`, those models are translated into **generated ArchUnit rules**.
3. Any architectural violation causes **`mvn verify` to fail immediately** — with deterministic evidence.

---

### What this is (and is not)

#### ✅ This is

* A **build-time architecture proof**
* A demonstration of **guardrails rules produced by the generator**
* Deterministic failures when architectural boundaries are violated
* A comparison across **two different architectural models**

#### ❌ This is NOT

* A runtime demo
* A Spring Boot feature showcase
* An ArchUnit tutorial
* A style guide

No manual app server is started.
The proof is driven by **`mvn verify`**

---

### Preconditions

To reproduce this walkthrough, you need:

* **Codegen Blueprint 1.0.0 (or later)**
* A project generated via the **CLI**
* Architecture guardrails enabled in **`strict`** mode
* The generated guardrails rules left **unchanged** (no manual edits)

No runtime configuration or external infrastructure is required.
All validations happen **at build time** via `mvn verify`.

---

### Walkthrough Structure (Applies to Every Case)

Every case follows the **same proof protocol**:

```
Generate project
→ mvn verify (GREEN)
→ inject boundary violation
→ mvn verify (RED)
→ revert violation
→ mvn verify (GREEN)
```

Each case uses **three screenshots** to make the proof observable:

1. **Baseline (GREEN)** — clean dependency direction
2. **Violation (RED trigger)** — the exact forbidden dependency introduced
3. **Failure (RED evidence)** — the ArchUnit rule and failure output

> **You will end up with 3 cases × 3 images = 9 images.**

---

## Part I — Hexagonal Architecture (Ports & Adapters)

### Case 1 — Hexagonal Strict: Adapter → Application Implementation (Port Bypass)

This case matches the proof-runner failure:

* **Test:** `HexagonalStrictPortsIsolationTest.adapters_must_not_depend_on_application_implementation`
* **Violation:** `adapter..` depends on an application implementation (`application.usecase..`) instead of `application.port..`

#### 1) Generation — Hexagonal + Strict

Generate the project:

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli springboot \
  --group-id io.github.blueprintplatform \
  --artifact-id greeting-hex \
  --name "Greeting" \
  --description "Proof: hexagonal strict guardrails" \
  --package-name io.github.blueprintplatform.greeting \
  --layout hexagonal \
  --guardrails strict \
  --sample-code basic \
  --dependency web
```

Strict guardrails mode is enabled from the start.
All architectural rules are **generated from profiles**, not handwritten.

---

#### 2) Baseline — Clean Hexagonal Flow (GREEN)

Inbound adapters depend **only on application ports**.

**Adapter → Port → Use Case → Domain**

<p align="center">
  <img src="./images/case-01-hex/01-hex-baseline-green.png" width="900" alt="Case 1 (Hex) baseline: controller depends only on application port"/>
  <br/>
  <em>Case 1 — Baseline (GREEN): inbound adapter depends only on an application port</em>
</p>

This snapshot shows the **initial generated state** of a hexagonal project with strict guardrails enabled.
No architectural violation has been introduced at this point.

---

#### 3) Intentional Violation — Break Ports Isolation (RED trigger)

Introduce a deliberate violation by making the controller depend on an **application implementation**
(e.g., `...application.usecase.GetGreetingHandler`) instead of the port.

This is illegal in strict hex guardrails:

> **Adapters may depend only on `application.port..`** — not application implementations.

<p align="center">
  <img src="./images/case-01-hex/02-hex-violation.png" width="900" alt="Case 1 (Hex) violation: controller depends on application implementation"/>
  <br/>
  <em>Case 1 — Violation: inbound adapter depends on an application implementation (port bypass)</em>
</p>

---

#### 4) Result — Hexagonal Build Failure (RED evidence)

Run the exact same build again:

```bash
mvn verify
```

The build fails deterministically.

<p align="center">
  <img src="./images/case-01-hex/03-hex-failure.png" width="900" alt="Case 1 (Hex) failure: generated ArchUnit rule fails the build"/>
  <br/>
  <em>Case 1 — Failure: generated ArchUnit rule detects adapter → application implementation dependency</em>
</p>

Console evidence (proof-runner aligned):

```
Rule: no classes in ..adapter.. should depend on classes in application but not ports (outside '.port.')
Violation:
  Field <...GreetingController.__archViolation> has type <...GetGreetingHandler>
Test:
  HexagonalStrictPortsIsolationTest.adapters_must_not_depend_on_application_implementation
```

---

## Part II — Standard (Layered) Architecture

### Case 2 — Standard Strict: Controller → Repository (Repository Bypass)

This case matches the proof-runner failure:

* **Test:** `StandardStrictLayerDependencyRulesTest.controllers_must_not_depend_on_repositories`
* **Violation:** `controller..` depends on `repository..`

#### 5) Generation — Standard + Strict

Generate the project:

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli springboot \
  --group-id io.github.blueprintplatform \
  --artifact-id greeting-standard \
  --name "Greeting" \
  --description "Proof: standard strict guardrails" \
  --package-name io.github.blueprintplatform.greeting \
  --layout standard \
  --guardrails strict \
  --sample-code basic \
  --dependency web
```

---

#### 6) Baseline — Controller Does Not Depend on Repository (GREEN)

In the standard model, controllers must remain delivery-only and must **not** depend on repositories.

<p align="center">
  <img src="./images/case-02-std-repo/01-std-baseline-green.png" width="900" alt="Case 2 (Standard) baseline: controller not depending on repository"/>
  <br/>
  <em>Case 2 — Baseline (GREEN): controller does not depend on repository</em>
</p>


This snapshot represents the **initial generated structure** of a standard layered project.
At this stage, no forbidden dependency exists.

---

#### 7) Intentional Violation — Controller Depends on Repository (RED trigger)

Introduce a deliberate violation by injecting/calling a repository from a controller.

But architecturally it is illegal:

> **Controllers must not depend on repositories.**

<p align="center">
  <img src="./images/case-02-std-repo/02-std-violation.png" width="900" alt="Case 2 (Standard) violation: controller depends on repository"/>
  <br/>
  <em>Case 2 — Violation: controller directly depends on repository (repository bypass)</em>
</p>

---

#### 8) Result — Standard Build Failure (RED evidence)

Run:

```bash
mvn verify
```

The build fails deterministically.

<p align="center">
  <img src="./images/case-02-std-repo/03-std-failure.png" width="900" alt="Case 2 (Standard) failure: ArchUnit rule fails for controller->repository"/>
  <br/>
  <em>Case 2 — Failure: generated ArchUnit rule detects controller → repository dependency</em>
</p>

Console evidence (proof-runner aligned):

```
Rule: no classes in ..controller.. should depend on classes in ..repository..
Violation:
  Field <...GreetingController.__archViolation> has type <...LoggingGreetingAuditRepository>
Test:
  StandardStrictLayerDependencyRulesTest.controllers_must_not_depend_on_repositories
```

---

### Case 3 — Standard Schema Sanity: Missing Canonical Family (controller renamed)

This case matches your schema experiment:

* **Test:** `StandardPackageSchemaSanityTest.each_standard_bounded_context_must_contain_required_canonical_families`
* **Violation:** canonical family **`controller`** is missing because you renamed it (e.g., `controller` → `controllers`).

#### 9) Baseline — Standard Schema Intact (GREEN)

A detected bounded context root must contain all required canonical families:

* `controller`
* `service`
* `domain`

<p align="center">
  <img src="./images/case-03-std-schema/01-std-schema-baseline-green.png" width="900" alt="Case 3 (Standard schema) baseline: controller/service/domain present"/>
  <br/>
  <em>Case 3 — Baseline (GREEN): standard schema contains controller/service/domain</em>
</p>

This snapshot shows the **unaltered, generated package schema** before any canonical family is renamed or removed.

---

#### 10) Intentional Violation — Break Canonical Schema (RED trigger)

Introduce the deliberate schema violation by renaming the canonical package family:

* `...controller...` → `...controllers...`

This is illegal because the guardrails schema sanity test requires the canonical families to exist.

<p align="center">
  <img src="./images/case-03-std-schema/02-std-schema-violation.png" width="900" alt="Case 3 (Standard schema) violation: controller family renamed/missing"/>
  <br/>
  <em>Case 3 — Violation: canonical family 'controller' is missing due to rename</em>
</p>

---

#### 11) Result — Schema Sanity Failure (RED evidence)

Run:

```bash
mvn verify
```

The build fails deterministically.

<p align="center">
  <img src="./images/case-03-std-schema/03-std-schema-failure.png" width="900" alt="Case 3 (Standard schema) failure: schema sanity test fails"/>
  <br/>
  <em>Case 3 — Failure: schema sanity test fails (required canonical families missing)</em>
</p>

Console evidence (aligned to your output):

```
  STANDARD package schema integrity failure under base scope 'io.github.blueprintplatform.greeting.bp.sample
  
  Required canonical families: [controller, service, domain]
  Violations:
   - context: io.github.blueprintplatform.greeting.bp.sample
       present: controller ❌, service ✅, domain ✅
       missing: controller
  
  Test:
    StandardPackageSchemaSanityTest.each_standard_bounded_context_must_contain_required_canonical_families
```

---

## What this proves

**Different architectures. Different rules. Same outcome.**

* Hexagonal validates **ports & adapter isolation**
* Standard validates **layered dependency direction**
* Standard also validates **schema integrity** (canonical families)
* All are evaluated **automatically at build time**
* All surface drift by breaking the build **deterministically** with explicit evidence

No documentation is consulted at evaluation time.
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

> **If a rule is violated, the build fails fast with explicit feedback.**

No assumptions.
No hidden conventions.
No silent drift.

That is **Architecture as a Product** — observable, repeatable, and evaluated at build time.

---

## Generated Project Output (Reference)

This walkthrough focused on **proving guardrails** — not on showcasing generated artifacts.

If you want to see what **actual projects generated by Codegen Blueprint look like**, including:

* the exact `README.md` files written into generated projects
* how architecture, guardrails, and sample code are explained to developers
* what a team receives *after* generation

see:

* 📘 **Generated Project README Previews** — real, generated README outputs shipped with projects

  → [Generated Readmes](./generated-readmes.md)

These READMEs are **real generator output**, not examples.

They represent the **human-facing expression of the same architecture contract**
that is **evaluated and verified** throughout this walkthrough.