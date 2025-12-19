# 🚀 Codegen Blueprint — Hexagonal Architecture Deep Dive

**Executable Architecture in Action — A Production‑Grade Reference**

This deep‑dive explains **how Hexagonal Architecture (Ports & Adapters)** is **executed and enforced** in Codegen Blueprint — not as guidelines, but **as generated, testable behavior**.

Architecture decisions are **compiled into the generator and materialized in the output**:

* Domain stays 🔒 framework‑free
* Technology swaps 🔁 without core changes
* Best practices 🚧 enforced via generated artifacts
* Generated services 🧱 inherit structure by construction

> **Architecture is not a guideline — it executes.**

---

## 📑 Table of Contents

* [Why Hexagonal Here?](#-why-hexagonal-here)
* [Layered Execution Flow](#-layered-execution-flow)
* [Ports & Adapters — Where the Power Lives](#-ports--adapters--where-the-power-lives)
* [Domain → Outbound Ports](#-domain--outbound-ports-pure-infrastructure-abstractions)
* [Application → Outbound Ports](#-application--outbound-ports-delivery--orchestration)
* [Application → Artifact Generation Ports](#-application--artifact-generation-ports)
* [Artifact Execution Engine](#-artifact-execution-engine)
* [Profiles — The Architecture Contract](#-profiles--the-architecture-contract)
* [Source Layout Enforcement](#-source-layout-enforcement)
* [Resource Model](#-resource-model--better-than-just-files)
* [Verified Architecture — Testing Strategy](#-verified-architecture--testing-strategy)
* [What You Learn from This Repo](#-what-you-learn-from-this-repo)
* [Try It — CLI Delivery Adapter](#-try-it--cli-delivery-adapter)
* [Architecture Execution Path](#-architecture-execution-path-mental-model)
* [Final Thoughts](#-final-thoughts)

---

## 🧭 Why Hexagonal Here?

Most project templates generate **folders**.
Codegen Blueprint generates **architectural intent**.

Hexagonal Architecture was chosen because it delivers:

| Principle                   | Value Delivered                   |
| --------------------------- | --------------------------------- |
| Strict dependency direction | Pure, independent domain model    |
| Ports define contracts      | Technology swaps without refactor |
| Adapter isolation           | Framework choice does not leak    |
| Test‑first boundaries       | Faster evolution with confidence  |

> The generated output already **protects the future architecture** of your service.

---

## 🧱 Layered Execution Flow

Strict inward dependency:

```
adapter (delivery + tech)
        ↓
application (use cases, orchestration)
        ↓
domain (business rules only)
```

Runtime wiring is delivered via `bootstrap` (Spring only at the edges).

* 📌 No Spring inside `domain`
* 📌 No template engine inside `domain` or `application`
* 📌 No file system assumptions inside business logic

---

## 🔌 Ports & Adapters — Where the Power Lives

Ports define **what is allowed**.
Adapters define **how it is done**.

No shortcuts. No hidden dependencies.

---

## 🧠 Domain → Outbound Ports (Pure Infrastructure Abstractions)

These ports represent **fundamental IO capabilities** required by the domain.
The domain **declares the need**, but never performs IO itself.

| Port                     | Responsibility                              |
| ------------------------ | ------------------------------------------- |
| `ProjectRootPort`        | Prepare and validate project root directory |
| `ProjectWriterPort`      | Persist generated files and directories     |

**Key characteristics:**

* ✔ No ZIP / archive knowledge
* ✔ No delivery concerns
* ✔ No CLI / REST assumptions
* ✔ File‑system is an **implementation detail**

```
domain.port.out.filesystem
├─ ProjectRootPort
├─ ProjectWriterPort
```

➡ Domain never touches IO implementations
➡ Domain never packages output

---

## 🎯 Application → Outbound Ports (Delivery & Orchestration)

The application layer owns **use-case execution and delivery concerns**.

| Port                  | Responsibility                                                  |
| --------------------- | --------------------------------------------------------------- |
| `ProjectArchiverPort` | Package generated project (ZIP today, OCI tomorrow)             |
| `ProjectOutputPort`   | Discover generated project output for reporting & delivery UX   |

```
application.port.out.archive
└─ ProjectArchiverPort

application.port.out.output
└─ ProjectOutputPort
```

➡ Packaging and output discovery are **not domain concerns**  
➡ They are **delivery / orchestration mechanisms**  
➡ Therefore they belong to the **application layer**

---

## 🧩 Application → Artifact Generation Ports

Each generated artifact is **explicit**, **intentional**, and **independently replaceable**.

Artifact generation is modeled as a **first‑class application concern** — not a side effect of templates.

| Port                           | Generated Output / Responsibility  |
| ------------------------------ | ---------------------------------- |
| `BuildConfigurationPort`       | `pom.xml`                          |
| `BuildToolFilesPort`           | Maven wrapper + tooling            |
| `SourceLayoutPort`             | Directory & package conventions    |
| `MainSourceEntrypointPort`     | Application bootstrap class        |
| `TestSourceEntrypointPort`     | Test bootstrap                     |
| `ApplicationConfigurationPort` | `application.yml`                  |
| `IgnoreRulesPort`              | `.gitignore`                       |
| `ProjectDocumentationPort`     | `README.md`                        |
| `SampleCodePort`               | Optional sample REST / domain code |
| `ArchitectureGovernancePort`   | Architecture enforcement artifacts |

All artifact ports implement:

```
application.port.out.artifact.ArtifactPort
```

### Architecture Governance as an Artifact

`ArchitectureGovernancePort` models **architecture enforcement itself** as a generated artifact.

Depending on profile and enforcement level, this may generate:

* ArchUnit‑based architecture tests
* Layered or Hexagonal boundary rules
* Dependency direction constraints

Enforcement artifacts are:

* ✔ Generated (not hard‑wired)
* ✔ Opt‑in (`--enforcement basic | strict`)
* ✔ Profile‑scoped
* ✔ Evolvable without engine refactors

> Architecture enforcement is **delivered as code**, like any other artifact.

---

## ⚙️ Artifact Execution Engine

Artifact generation is **ordered**, **deterministic**, and **profile‑driven**.

| Component                  | Responsibility                             |
| -------------------------- | ------------------------------------------ |
| `ProjectArtifactsSelector` | Selects profile‑specific artifact pipeline |
| `ProjectArtifactsPort`     | Executes artifacts in defined order        |

> Nothing is generated accidentally — every file is **architecturally intentional**.

---

## 🧬 Profiles — The Architecture Contract

Profiles externalize **what is generated** and **in which order**.

Example pipeline:

```
build-config
→ build-tool-files
→ ignore-rules
→ source-layout
→ app-config
→ main-source-entrypoint
→ test-source-entrypoint
→ architecture-governance (optional)
→ sample-code (optional)
→ project-documentation
```

Profiles are:

* ✔ Enforced architecture standards
* ✔ Reusable across teams
* ✔ Extensible without core changes

---

## 📐 Source Layout Enforcement

### Standard

```
src/main/java/<basepkg>/
src/main/resources/
src/test/java/<basepkg>/
```

### Hexagonal (opt‑in)

```
adapter/
  ├─ in/
  └─ out/
application/
domain/
bootstrap/
```

---

## 📂 Resource Model — Better than “Just Files”

| Type      | Domain Model              | Why                 |
| --------- | ------------------------- | ------------------- |
| Directory | `GeneratedDirectory`      | Validated structure |
| Text      | `GeneratedTextResource`   | Safe content        |
| Binary    | `GeneratedBinaryResource` | Tooling & wrappers  |

> Generated resources are modeled, validated, and ordered — not written ad-hoc.

---

## 🧪 Verified Architecture — Testing Strategy

| Test Type   | Ensures                           |
| ----------- | --------------------------------- |
| Unit        | Domain & rule correctness         |
| Integration | Correct wiring                    |
| E2E CLI     | Generated project validity        |
| ArchUnit    | Enforced architectural boundaries |

Together, these tests ensure that architectural intent is continuously validated
from generation time through build execution.

> Tests protect **architecture**, not just syntax.
> Architecture tests protect *dependency direction*, not implementation details.

---

## 🎯 What You Learn from This Repo

| Skill                    | How This Repo Teaches It                                      |
| ------------------------ | ------------------------------------------------------------- |
| Hexagonal mastery        | True isolation enforced by ports and dependency direction     |
| Maintainable scaffolding | Evolution paths encoded from day zero                         |
| Architecture automation  | Governance-as-Code via executable rules and pipelines          |
| Multi-stack enablement   | New stacks added without touching the core engine             |
| Testing for architecture | Architecture validated through tests — not conventions        |

This repository is a **production reference architecture**.

> It prioritizes architectural correctness and evolution over feature completeness.

It is designed to be:
- studied by experienced developers and architects
- extended as a platform foundation
- challenged through architectural change scenarios

It is **not positioned as** a classroom demo, step-by-step tutorial, or framework showcase.

Instead, it serves as a **production-oriented reference** for developers and architects
who want to study, evaluate, and evolve executable architecture in real systems.

---

## 🎮 Try It — CLI Delivery Adapter

This section demonstrates the **actual, supported CLI contract** for Codegen Blueprint **1.0.0 GA** when generating a **Hexagonal Architecture** project.

The command below reflects the real engine behavior, generated structure, and enforcement capabilities — no placeholders, no aspirational flags.

---

### Generate a Hexagonal Spring Boot Project

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli springboot \
  --group-id io.github.blueprintplatform \
  --artifact-id greeting \
  --name "Greeting" \
  --description "Greeting sample built with hexagonal architecture" \
  --package-name io.github.blueprintplatform.greeting \
  --layout hexagonal \
  --enforcement basic \
  --sample-code basic
```

---

### What This Command Does

This single command:

* Selects the **Spring Boot · Maven · Java 21** profile
* Generates a **hexagonal (ports & adapters)** source layout
* Produces a **framework-free domain core** by construction
* Enables **basic architecture enforcement** via generated ArchUnit tests
* Adds a **minimal teaching sample** (domain + application + adapter)
* Outputs a project that **builds and verifies immediately**

No runtime configuration is required.
No manual wiring is expected.

---

### Resulting Guarantees

The generated project:

* Passes `mvn verify` on first run
* Enforces architectural boundaries at test time
* Prevents accidental dependency direction violations
* Keeps Spring strictly at the edges (`bootstrap` and `adapter` layers)

> Architecture is not suggested — it is **enforced by construction**.

---

This CLI adapter is the **primary delivery mechanism** of Codegen Blueprint 1.0.0 GA.
Everything else in the system exists to make this command **predictable, safe, and evolution-ready**.

---

## 🔍 Architecture Execution Path (Mental Model)

```
CLI input
 ↓
ProjectBlueprint
 ↓
ProjectArtifactsSelector
 ↓
ProjectArtifactsPort
 ↓
ProjectWriterPort
```

> No artifact knows about another artifact — only the pipeline knows the order.

---

## ⭐ Final Thoughts

**Executable Architecture** means:

* Architecture cannot drift accidentally
* Domain is always protected
* Technology evolves independently
* Standards are repeatable at scale

> Architecture is no longer documentation — it is **behavior**.
