# Architecture Enforcement Scope (GA 1.0.0)

This document defines the **exact, build-time architecture enforcement contract** provided by Codegen Blueprint **1.0.0 GA**.

The goal is not to explain ArchUnit or teach architecture styles, but to **declare what is enforced, when, and why**.

> Architecture here is not documented or recommended.
> It is **generated and enforced deterministically at build time**.

---

## Enforcement overview

**Mechanism**

* Enforcement is implemented via **generated ArchUnit tests**.
* Tests are generated based on:

* Selected **layout** (`hexagonal` or `standard`)
* Selected **enforcement mode** (`none`, `basic`, or `strict`)
* Rules execute automatically during:

```bash
mvn verify
```

**Default behavior (1.0.0 GA)**

* The CLI default enforcement mode is **`basic`**
* Architecture enforcement is **enabled by default**
* `none` must be **explicitly selected** to disable enforcement

**Failure behavior**

* Any violation causes the build to **fail immediately**
* No application startup is required
* No runtime checks are involved

---

## Enforcement modes

### none

* No architecture tests are generated.
* No enforcement guarantees exist.

### basic

* Lightweight but meaningful guardrails.
* Prevents the most common architectural drift.
* Suitable for teams introducing architecture incrementally.

### strict

* Full enforcement of architectural boundaries.
* Prevents structural drift and accidental coupling.
* Intended for teams that treat architecture as a **non-negotiable contract**.

---

## Hexagonal architecture enforcement

### Hexagonal — Basic

**Guaranteed rules**

* Application layer **must not depend on adapters**
* Bootstrap layer is a **leaf module** (no other module may depend on it)

**Intent**

* Protects the core use cases from infrastructure concerns
* Ensures bootstrapping remains replaceable

---

### Hexagonal — Strict

Includes **all Basic rules**, plus the following:

#### Adapter direction isolation

* Inbound adapters **must not depend on outbound adapters**
* Outbound adapters **must not depend on inbound adapters**

#### Domain purity

* Domain may depend **only on:**

    * JDK types (`java.*`)
    * Other domain types
* No framework, adapter, or application dependencies allowed

#### Ports isolation

* Adapters may depend **only on application ports**
* Adapters must not depend on application implementation classes

#### Package cycle prevention

* No cyclic dependencies between top-level packages
* No cycles inside adapter subpackages

#### REST boundary isolation (Spring Web only)

Enforced only when:

* `spring-boot-starter-web` is present

Rules:

* REST controllers **must not expose domain types** in:

    * Return types
    * Parameters
    * Generic signatures
* DTOs under inbound adapters must not depend on domain

**Rationale**

* HTTP is a public boundary
* Domain types must never leak through that boundary

---

## Standard (Layered) architecture enforcement

### Standard — Basic

**Guaranteed rules**

* Controllers must not depend on repositories
* Domain must not depend on:

    * Controllers
    * Services
    * Repositories

**Intent**

* Prevents shortcutting the service layer
* Keeps domain isolated from infrastructure and delivery layers

---

### Standard — Strict

Includes **all Basic rules**, plus the following:

#### Layer dependency direction

* Controllers must not depend on repositories
* Services must not depend on controllers
* Repositories must not depend on services or controllers

#### Domain purity

* Domain may depend **only on:**

    * JDK types
    * Other domain types

#### Package cycle prevention

* No cyclic dependencies between top-level packages

#### REST boundary isolation (Spring Web only)

Rules:

* REST controllers must not expose domain types in:

    * Return types
    * Parameters
    * Generic signatures
* Controller DTOs must not depend on domain

**Rationale**

* Controller layer is the public HTTP boundary
* Domain must remain internal

---

## What enforcement does NOT cover

The following are **explicitly out of scope**:

* Code style or formatting
* Naming conventions
* Business rule correctness
* Runtime behavior (transactions, security, performance)
* Dependency vulnerability scanning

Enforcement is **structural**, not behavioral.

---

## Generated test locations

Architecture tests are generated under:

```
src/test/java/<projectPackage>/architecture/archunit/**
```

They are:

* Deterministic
* Generated from templates
* Versioned as part of the GA contract

---

## Versioning guarantee

This enforcement scope is part of the **1.0.0 GA contract**.

Changes to these rules are treated as:

* Contract changes
* Explicit version upgrades

No silent enforcement changes are introduced within a GA line.

---

## Summary

Codegen Blueprint enforces architecture by **construction**:

* Architecture is generated
* Guardrails are executable
* Violations fail the build

> If a rule is violated, the build breaks.
> There are no conventions, exceptions, or runtime excuses.
