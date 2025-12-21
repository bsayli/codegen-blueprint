# Generated Project README Previews

This document showcases **real, generated README outputs** produced by Codegen Blueprint.

It exists to make one thing explicit:

> **Codegen Blueprint does not only generate code — it generates a complete, opinionated project contract.**

The README files shown here are **not hand-written examples**.
They are the **exact README.md files generated into projects** created via the CLI.

---

## Why this matters

Most generators stop at code scaffolding.
Codegen Blueprint goes further by generating:

* a **clear project identity** (what was generated, how, and why)
* an explicit **architecture contract** (layout + enforcement)
* a **deterministic onboarding path** (how to build, run, and explore)

The generated README is therefore part of the **product output**, not auxiliary documentation.

---

## What you are seeing

Below are two generated README examples created from the **same engine**, using the **same enforcement mode**, but with **different architectural models**:

| Variant   | Layout           | Enforcement | Sample Code |
| --------- | ---------------- | ----------- | ----------- |
| Hexagonal | Ports & Adapters | strict      | basic       |
| Standard  | Layered          | strict      | basic       |

Both projects:

* were generated via the CLI
* compile and pass `mvn verify`
* include executable ArchUnit enforcement rules
* fail the build when architectural boundaries are violated

---

## Hexagonal Architecture — Generated README

This README was generated using:

```bash
--layout hexagonal --enforcement strict --sample-code basic
```

Key characteristics visible in the generated README:

* Explicit declaration of **hexagonal (ports & adapters) architecture**
* Clear explanation of **inbound / outbound boundaries**
* Strict enforcement guarantees:

    * ports isolation
    * adapter direction rules
    * domain purity
* Exact location of generated ArchUnit rules
* Runnable, minimal sample demonstrating correct dependency flow

📄 **Full generated README:**

→ [Hexagonal Project README](./generated/hexagonal/README.md)

---

## Standard (Layered) Architecture — Generated README

This README was generated using:

```bash
--layout standard --enforcement strict --sample-code basic
```

Key characteristics visible in the generated README:

* Explicit declaration of **standard layered architecture**
* Clear explanation of layer responsibilities
* Strict enforcement guarantees:

    * controller → service → repository direction
    * domain purity
    * REST boundary isolation
* Deterministic failure behavior on violations
* Simple, readable sample designed for baseline understanding

📄 **Full generated README:**

→ [Standard Project README](./generated/standard/README.md)

---

## What this proves

These READMEs demonstrate that:

* Architecture is **declared**, not implied
* Enforcement is **explicit**, not hidden
* Generated projects are **self-explanatory**
* Onboarding does not require tribal knowledge

Most importantly:

> The generated README is part of the **architecture contract**.

It documents exactly what the generator promised — and what the build will enforce.

---

## Relationship to Executable Architecture Proof

If you want to see **how these guarantees are enforced**, step by step, including:

* clean baseline builds
* intentional violations
* deterministic build failures

see:

→ **Executable Architecture Proof — Architecture Enforcement Walkthrough**

This document shows the **proof**.
The READMEs shown here represent the **resulting product**.

---

## Summary

Codegen Blueprint generates:

* architecture-aware code
* executable architectural guardrails
* **first-class project documentation**

The README is not an afterthought.
It is part of the generated system.

> **Architecture as a Product** starts with what the user sees first.
