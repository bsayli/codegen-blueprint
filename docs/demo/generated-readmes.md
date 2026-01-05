# Generated Project README Previews

This document showcases **real, generated README outputs** produced by Codegen Blueprint.

It exists to make one thing explicit:

> **Codegen Blueprint does not only generate code — it generates a clear, opinionated project-level output contract.**

The README files shown here are **not handwritten examples**.
They are the **exact `README.md` files generated into projects** created via the CLI.

> **Terminology note**
>
> In this document, *contract* means the **generated project’s output contract** (what the generated project says about itself: layout, guardrails mode, build-time behavior, onboarding).
>
> This is **not** the repository’s **1.0.0 GA guarantee surface**. GA guarantees live in the dedicated contract/policy docs.

---

## 📑 Table of Contents

* [Why this matters](#why-this-matters)
* [What you are seeing](#what-you-are-seeing)
* [Hexagonal Architecture — Generated README](#hexagonal-architecture--generated-readme)
* [Standard (Layered) Architecture — Generated README](#standard-layered-architecture--generated-readme)
* [What this proves](#what-this-proves)
* [Relationship to Executable Architecture Proof](#relationship-to-executable-architecture-proof)
* [Summary](#summary)

---

## Why this matters

Most generators stop at scaffolding.
Codegen Blueprint also generates a **first-class project README** that makes the output:

* **self-identifying** (what was generated and how)
* **explicit about architecture** (layout and boundaries)
* **explicit about build-time guardrails** (when enabled)
* **onboarding-friendly** (how to build, run, and explore)

The generated README is therefore part of the **product output**, not auxiliary documentation.

---

## What you are seeing

Below are two generated README examples created from the **same engine** with **strict guardrails enabled**, but with **different architectural models**:

| Variant   | Layout           | Guardrails Mode | Sample Code |
| --------- | ---------------- | --------------- | ----------- |
| Hexagonal | Ports & Adapters | strict          | basic       |
| Standard  | Layered          | strict          | basic       |

Both projects:

* were generated via the CLI
* pass `mvn verify` on the target baseline
* include generated ArchUnit guardrails rules
* fail the build deterministically when enabled boundaries are violated

---

## Hexagonal Architecture — Generated README

Generated using:

```bash
--layout hexagonal --guardrails strict --sample-code basic
```

What the generated README makes explicit:

* **Hexagonal model** (ports & adapters)
* Inbound/outbound boundaries and intended dependency direction
* Strict guardrails via generated ArchUnit tests (e.g., **ports isolation**, **domain purity**)
* Where the generated rules live in the project

📄 **Full generated README:**

→ [Hexagonal Project README](./generated/hexagonal/README.md)

---

## Standard (Layered) Architecture — Generated README

Generated using:

```bash
--layout standard --guardrails strict --sample-code basic
```

What the generated README makes explicit:

* **Layered model** (controller/service/repository/domain)
* Layer responsibilities and expected direction
* Strict guardrails via generated ArchUnit tests (e.g., **controller → service → repository**, **domain purity**)
* Deterministic build failure behavior on violations

📄 **Full generated README:**

→ [Standard Project README](./generated/standard/README.md)

---

## What this proves

These generated READMEs show that:

* Architecture is **declared**, not implied
* Guardrails are **explicit**, not hidden
* The generated project is **self-explanatory** for new contributors

Most importantly:

> The generated README is part of the generated project’s **output contract**: it tells you what the build will validate when guardrails are enabled.

---

## Relationship to Executable Architecture Proof

If you want to see **how these guarantees are enforced**, end-to-end (GREEN → RED → GREEN), including:

* clean baseline builds
* intentional violations
* deterministic build failures

see:

→ **Executable Architecture Proof — Guardrails Walkthrough**

That document shows the **proof**.
This document shows the **human-facing output** shipped inside generated projects.

---

## Summary

Codegen Blueprint generates:

* architecture-aware code
* executable build-time guardrails (when enabled)
* **first-class project documentation**

The README is not an afterthought.
It is part of the generated system.

> **Architecture as a Product** starts with what the user sees first.
