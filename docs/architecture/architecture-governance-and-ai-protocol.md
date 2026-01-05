# Architecture Governance & AI Collaboration Protocol — 1.0.0 GA

> **Executable Architecture requires explicit decisions and observable guardrails — not accidental code.**

This document defines how architectural decisions are made, **evaluated**, and protected in the Codegen Blueprint project — including **how AI is allowed to participate**.

---

## 📑 Table of Contents

- [Core Principle](#1-core-principle)
- [Roles & Responsibilities](#2-roles--responsibilities)
- [Decision-Making Protocol](#3-decision-making-protocol)
- [Architectural Boundaries](#4-architectural-boundaries-non-negotiable)
- [Architecture Guardrails Philosophy](#5-architecture-guardrails-philosophy)
- [AI Contribution Rules](#6-ai-contribution-rules)
- [Merge Gate Checklist](#7-merge-gate-checklist)
- [Guiding Belief](#8-guiding-belief)

---

## 1. Core Principle

**Architecture is not documentation. It is behavior.**

Therefore:

* Every architectural claim must map to an observable guardrails mechanism
* Every generated artifact must be intentional
* Nothing enters the repository by accident

> If we cannot explain *why* a piece of code exists, it does not belong here.

---

## 2. Roles & Responsibilities

### Human (Architect / Maintainer)

* Owns **architectural intent**
* Makes **final decisions**
* Defines boundaries, policies, and invariants
* Acts as the **merge gatekeeper**

### AI (Collaborative Implementer)

* Produces **draft implementations** only
* Never makes architectural decisions autonomously
* Must follow existing boundaries and contracts
* Is expected to be questioned, challenged, and corrected

> AI writes code. Humans own architecture.

---

## 3. Decision-Making Protocol

Every non-trivial change follows this explicit sequence:

> **Skipping any step invalidates the change — regardless of test results.**

This protocol exists to ensure architectural intent survives beyond individual implementations.

### 3.1 Intent

*What problem are we solving?*

* Short, explicit statement
* No implementation details

### 3.2 Boundary

*Where does this change live?*

* Domain / Application / Adapter / Profile
* Justification required

### 3.3 Contract

*What is the input/output shape?*

* Ports over implementations
* Application models ≠ Adapter DTOs
* No framework leakage

### 3.4 Proof

*How is this guaranteed?*

* Test, rule, or **guardrails mechanism**
* Manual guarantees are not accepted

### 3.5 Drift Check

*What future mistake does this prevent?*

* Explicit anti-drift rationale

---

## 4. Architectural Boundaries (Non-Negotiable)

### Domain

* No framework dependencies
* No IO, filesystem, templating, or delivery logic
* Contains:

  * Aggregates
  * Value objects
  * Policies
  * Fail-fast domain violations

### Application

* Orchestrates use cases
* Depends on domain only
* Owns delivery coordination (ZIP, filesystem, pipelines)
* Defines execution order — not content

### Adapters

* Contain all technology-specific code
* Implement ports
* Are replaceable without touching core logic

### Profiles

* Define **what** gets generated and **in which order**
* Encode architectural policy as configuration
* Are the single source of truth for generation pipelines
* Prevent implicit coupling by making generation order explicit

---

## 5. Architecture Guardrails Philosophy

* Architecture guardrails are **opt-in**, never hidden
* Guardrails rules are **generated**, not hard-wired

> Generated guardrails rules are platform-defined and centrally evolved,
> while remaining visible and reviewable at the service level.

Supported guardrails modes (explicitly selected at generation time):

* `none` — generation only
* `basic` — structural & dependency guards
* `strict` — architecture drift prevention

> Architecture guardrails must surface violations deterministically —
> or they must not be claimed at all.

---

## 6. AI Contribution Rules

AI-generated code **must**:

* Respect existing package and layer boundaries
* Use intent-driven naming
* Avoid speculative abstractions
* Be small, reviewable, and reversible
* Be explainable in architectural terms, not just technical ones

AI-generated code **must not**:

* Introduce new architectural concepts
* Rename public APIs without discussion
* Add frameworks to core layers
* Bypass existing guardrails mechanisms

---

## 7. Merge Gate Checklist

A change may be merged only if:

* [ ] Architectural intent is explicit
* [ ] Boundary placement is justified
* [ ] Contracts are clear and minimal
* [ ] Tests or guardrails mechanisms make the claim observable
* [ ] README and docs remain truthful
* [ ] No new drift vectors introduced
* [ ] No architectural decision was delegated implicitly to tooling or frameworks

> Passing tests alone is not sufficient.

---

## 8. Guiding Belief

This project intentionally resists:

* Blind code generation
* Magic scaffolding
* Architecture-by-template

Instead, it embraces:

* Explicit decisions
* Observable guardrails
* Collaborative reasoning

> **Architecture is a long-term asset.**
> This governance exists to protect it.