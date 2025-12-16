# Architecture Governance & AI Collaboration Protocol

> **Executable Architecture requires explicit decisions, not accidental code.**

This document defines how architectural decisions are made, enforced, and protected in the Codegen Blueprint project — including **how AI is allowed to participate**.

---

## 1. Core Principle

**Architecture is not documentation. It is behavior.**

Therefore:

* Every architectural claim must map to an enforceable rule or test
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

* Test, rule, or enforcement mechanism
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

---

## 5. Enforcement Philosophy

* Enforcement is **opt-in**, never hidden
* Rules are generated, not hard-wired
* Tests > conventions > documentation

Supported levels:

* `none` — generation only
* `basic` — structural & dependency guards
* `strict` — architecture drift prevention

> Architecture must fail fast or not claim enforcement.

---

## 6. AI Contribution Rules

AI-generated code **must**:

* Respect existing package and layer boundaries
* Use intent-driven naming
* Avoid speculative abstractions
* Be small, reviewable, and reversible

AI-generated code **must not**:

* Introduce new architectural concepts
* Rename public APIs without discussion
* Add frameworks to core layers
* Bypass existing enforcement mechanisms

---

## 7. Merge Gate Checklist

A change may be merged only if:

* [ ] Architectural intent is explicit
* [ ] Boundary placement is justified
* [ ] Contracts are clear and minimal
* [ ] Tests or rules enforce the claim
* [ ] README and docs remain truthful
* [ ] No new drift vectors introduced

> Passing tests alone is not sufficient.

---

## 8. Guiding Belief

This project intentionally resists:

* Blind code generation
* Magic scaffolding
* Architecture-by-template

Instead, it embraces:

* Explicit decisions
* Enforced boundaries
* Collaborative reasoning

> **Architecture is a long-term asset.**
> This governance exists to protect it. 