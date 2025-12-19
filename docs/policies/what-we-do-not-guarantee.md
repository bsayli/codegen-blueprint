# What We Explicitly Do **NOT** Guarantee

This document defines the **explicit non‑guarantees** of Codegen Blueprint.

It exists to prevent **incorrect expectations**, protect the architectural intent of the project,
and make the scope of responsibility unambiguous.

> Codegen Blueprint is opinionated by design.
> What it does *not* do is as important as what it does.

If a behavior, capability, or outcome is **not explicitly guaranteed here or in the Release Discipline**,
it must be assumed to be **out of scope**.

---

## Purpose of This Document

This document exists to:

* Prevent misuse of Codegen Blueprint
* Eliminate ambiguity about responsibility boundaries
* Avoid implicit promises created by assumptions
* Protect the project from being evaluated against goals it never claimed

This is **not** a legal disclaimer and **not** an abdication of responsibility.
It is a statement of **intentional design boundaries**.

---

## ❌ What Codegen Blueprint Does NOT Guarantee

### 1. Runtime Behavior or Production Correctness

Codegen Blueprint **does not guarantee**:

* Runtime correctness of generated applications
* Performance characteristics
* Scalability, resilience, or availability behavior
* Production‑grade configuration for any specific environment

The generator focuses on **architectural structure and boundaries**, not runtime behavior.

> A project that compiles and passes `mvn verify` is structurally valid —
> it is not automatically production‑ready.

---

### 2. Framework Best Practices Beyond Explicit Guarantees

Codegen Blueprint **does not claim** that:

* Generated code represents the “best possible” Spring Boot implementation
* Framework usage reflects every evolving best practice
* Generated APIs or services follow all community conventions

Only guarantees **explicitly stated** in:

* `release-discipline.md`
* README GA scope sections

are considered contractual.

---

### 3. Universal Architecture Compatibility

Codegen Blueprint **does not guarantee**:

* Compatibility with every organizational architecture style
* Alignment with existing legacy codebases
* Compliance with undocumented internal company rules

The generator enforces **its own explicit architectural models**:

* `standard` (layered)
* `hexagonal` (ports & adapters)

If your organization’s architecture diverges from these models,
Codegen Blueprint is **not obligated to adapt**.

---

### 4. Unlimited Dependency Freedom

Codegen Blueprint **intentionally does not guarantee**:

* Free‑form dependency injection via CLI
* Arbitrary third‑party library inclusion
* Automatic alignment with custom BOMs

Dependency aliases are **restricted by design** to:

* prevent dependency sprawl
* preserve architectural clarity
* maintain deterministic output

> Dependency freedom is a runtime decision.
> Architectural intent is a generation‑time decision.

---

### 5. Backward Compatibility Outside Declared Contracts

Codegen Blueprint **does not guarantee**:

* Backward compatibility across major versions
* Stability of experimental or pre‑GA features
* Compatibility for versions not explicitly listed as supported

Compatibility guarantees **begin at 1.0.0 GA**
and apply **only within the declared version line**.

---

### 6. Automatic Migration or Upgrade Assistance

Codegen Blueprint **does not guarantee**:

* Automated migration between generated project versions
* Tooling for refactoring existing projects
* Upgrade paths for modified or manually altered output

Generated projects are **starting points**, not managed artifacts.

---

### 7. Absence of Required Architectural Decisions

Codegen Blueprint **does not remove the need** for:

* Architectural thinking
* Design trade‑off decisions
* Context‑specific judgment

The generator **encodes intent**, but it does not replace architects.

> Architecture as a Product does not mean architecture without responsibility.

---

### 8. Template Stability as a Public API

Templates are **not** considered a public API.

Codegen Blueprint **does not guarantee**:

* Stability of internal template structure
* Backward compatibility for custom template overrides
* Support for undocumented template hooks

Only generated output contracts are protected — not template internals.

---

## Relationship to Other Policies

This document must be read together with:

* **Release Discipline** — what *is* guaranteed and versioned
* **SECURITY.md** — what security responsibilities are accepted
* **CONTRIBUTING.md** — what contributions are acceptable

Together, these documents define the **complete responsibility boundary** of the project.

---

## Final Statement

Codegen Blueprint is designed to be **deliberate, constrained, and enforceable**.

Its strength comes from:

* explicit guarantees
* intentional limitations
* architectural integrity over convenience

> If a feature is missing, it is often because adding it would weaken the product.

Understanding these non‑guarantees is essential to using Codegen Blueprint correctly.
