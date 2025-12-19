# Release Discipline

This document defines the **release and compatibility discipline** of Codegen Blueprint.
It describes what each version line means, what is guaranteed, and how the project evolves
*without breaking architectural promises*.

Codegen Blueprint treats releases as **product contracts**, not marketing milestones.

---

## Core Principles

* Releases communicate **guarantees**, not feature volume
* Compatibility is intentional and explicit
* Architectural integrity takes precedence over speed
* Breaking changes are allowed only when clearly signaled

> If a guarantee is not written here, it is **not guaranteed**.

---

## Versioning Model

Codegen Blueprint follows a **SemVer-inspired** model, with additional architectural meaning:

```
MAJOR.MINOR.PATCH
```

Where:

* **MAJOR** — Architectural or contract-breaking change
* **MINOR** — Backward-compatible capability expansion
* **PATCH** — Bug fixes and non-behavioral corrections

---

## 1.0.0 — General Availability (GA)

Version **1.0.0** marks the point where Codegen Blueprint becomes a **stable product**, not an experiment.

From this version onward:

* Public behavior is intentional
* Guarantees are explicit
* Compatibility expectations begin

> Anything prior to 1.0.0 exists for historical context only.

### What 1.0.0 Guarantees

* Deterministic project generation

    * Same input → same output
* Framework-free domain core by construction
* Supported architecture layouts:

    * `standard` (layered)
    * `hexagonal` (ports & adapters)
* Optional executable architecture enforcement:

    * Generated ArchUnit tests (`basic`, `strict`)
* Generated projects pass `mvn verify` on first run
* Profile-driven stack selection

    * Spring Boot (3.4, 3.5)
    * Java 21 (GA target)
    * Maven

These guarantees define the **minimum bar** that will not regress within the 1.x line.

---

## 1.0.x — Patch Releases

Patch releases:

* Fix bugs
* Correct incorrect or unintended behavior
* Improve documentation
* Stabilize internals

Patch releases **do not**:

* Change CLI contracts
* Modify generated project structure
* Alter architectural enforcement rules in breaking ways

> If a generated project built successfully on **1.0.0**,  
> it must continue to build on any **1.0.x** release —  
> except where a fix corrects behavior that was **objectively incorrect or unsafe**.
---

## 1.1.x — Minor Releases

Minor releases introduce **new capabilities without breaking existing guarantees**.

Examples of allowed changes:

* New CLI options (opt-in)
* New profiles or artifacts
* Additional enforcement modes
* Extended template coverage
* Support for newer platform versions (opt-in)

Minor releases **must not**:

* Break existing layouts
* Change default behavior silently
* Invalidate existing generated projects

> Upgrading from 1.0.x → 1.1.x should be safe for existing users
> unless they explicitly opt into new features.

---

## 2.0.0 — Major Releases

A major release indicates a **deliberate contract reset**.

This may include:

* Architectural model changes
* CLI contract restructuring
* Breaking changes to generated output
* Enforcement rule semantics changes

Major releases:

* Are announced clearly
* Are documented explicitly
* Provide migration guidance when feasible

> Breaking architecture silently is never acceptable.
> Breaking it explicitly — with intent — sometimes is.

---

## Compatibility Philosophy

Compatibility is evaluated at three levels:

1. **Generator behavior**
2. **Generated project structure**
3. **Executable architecture guarantees**

A release is considered compatible only if **all three remain valid**.

---

## Supported Platform Versions

Unless stated otherwise:

* Java 21 is the **GA compatibility baseline**
* Newer LTS versions (e.g. Java 25) may work

    * but are not guaranteed until explicitly declared

The same applies to Spring Boot versions.

---

## Final Note

Release discipline exists to protect users from ambiguity.

Codegen Blueprint prefers:

* Fewer releases
* Clear contracts
* Predictable evolution

Over fast iteration with undefined consequences.

> Architecture is not only generated — it is **versioned, protected, and enforced**.
