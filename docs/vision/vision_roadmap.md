## 🚀 Vision & Roadmap — Engine Scope

> Architecture should **execute**, not merely be drawn.
> And it must remain **observable and verifiable** — even 6, 12, or 24 months later.

---

## 🌟 The Vision

**codegen‑blueprint** is the **executable engine** of the **Blueprint Platform**.

Its responsibility is to **prove architectural intent as a concrete, build‑time outcome** — not as documentation, convention, or best‑effort review.

The engine focuses on three core guarantees:

* **Architecture as an Executable Product**
  Structural intent and guardrails delivered as **testable, build‑breaking artifacts**
* **Deterministic Foundations**
  Generated projects behave the same across machines, teams, and time
* **Zero‑Drift Day‑Zero Baseline**
  Architecture is explicit from the first commit and stays observable

From **Day Zero to CI**, architecture remains **intentional**, **testable**, and **continuously evaluated**.

---

## 🧭 Roadmap Principles (Order Matters)

codegen‑blueprint evolves in **intentional layers** to protect its core promise and avoid premature surface expansion.

Each phase builds on **proven contracts and executable proof**, not assumptions.

1. **Strengthen the contract & proof**
   Determinism, executable guardrails, reproducible evidence
2. **Add new delivery surfaces**
   CLI today → REST tomorrow — **without changing the core engine**
3. **Integrate platform capabilities**
   Select, wire, and govern behavior — **not generate it**
4. **Expand profiles cautiously**
   More stacks = more surface area → only after proof maturity

> 📌 Ordering is non‑negotiable.
> Capabilities and profiles come **after** architectural intent is proven executable.

---

## 🎯 Roadmap

### 🔹 Phase 1 — Architecture‑First Generation (1.0.0 GA)

This phase establishes the **executable architectural foundation**.

* Hexagonal / Standard (Layered) generation (opt‑in)
* Architecture guardrails via **generated ArchUnit checks**
  (`none | basic | strict`)
* CLI‑driven, profile‑based generation
  (Spring Boot · Maven · Java 21)
* Framework‑free domain core **by construction**
* End‑to‑end **buildable output** evaluated in CI
  (generated projects verified with `mvn verify`)

📌 **GA Objective** → zero‑drift foundations + executable proof

---

### 🔹 Phase 2 — New Delivery Surfaces (Planned)

This phase expands **access**, not responsibility.

The **core engine and domain surface remain unchanged**.

* REST inbound adapter
  (same engine, new entry point)
* Interactive onboarding / configuration UX
  (contract‑first, explicit intent capture)
* Safer defaults and clearer architectural signals

**Design intent (early):**

* Architecture dialects (Hexagonal / Standard variants) are selected **up‑front**
* Vocabulary choices become **explicit contract input**, not implicit convention
* UX guides teams to choose *one* dialect — not invent new ones

📌 Goal → broaden accessibility **without diluting architectural contracts**

---

### 🔹 Phase 3 — Capability Integration (Platform‑Level, Planned)

> **Platform‑level concern — not implemented in this repository**

This phase operates at the **Blueprint Platform level**, not inside the engine.

Cross‑cutting concerns are **not generated as code**.
They are delivered as **versioned capabilities**, governed centrally.

**codegen‑blueprint acts as the entry point and wiring engine**, enabling:

* Capability selection
* Dependency and configuration wiring
* Governance and compatibility validation

Planned capability areas:

* 🔐 Security (OAuth2 / Keycloak)
* 🔍 Observability (tracing, logs, metrics)
* 📡 Resilience (timeouts, retries, policies)
* 🏛️ Architecture policy packs
  *(initially delivered as versioned ArchUnit rule sets)*
* 🔁 Generics‑aware OpenAPI clients
  *(separate Blueprint module)*

📌 Goal → consistent behavior and upgrades **in one place**, not per service

---

### 🔹 Phase 4 — Profile Expansion (Roadmap)

Profiles accelerate adoption but **increase surface area**.
They are introduced only after contracts and governance mature.

* Gradle profile
* Kotlin profile
* Quarkus and future stacks
* Visual UI — configure → generate → download
* Governance at scale (drift detection ideas)
* Opt‑in platform telemetry for architecture health

📌 Goal → expand stacks **after** proof and contracts are stable

---

## 🧩 Why This Matters

**codegen‑blueprint provides the executable foundation for these outcomes:**

| Without Blueprint            | With Blueprint                     |
| ---------------------------- | ---------------------------------- |
| Architecture drifts silently | Guardrails make drift visible      |
| Boilerplate everywhere       | Capabilities via libraries         |
| Onboarding takes weeks       | Day‑zero structure + contracts     |
| Standards rely on discipline | Standards enforced by construction |

---

## 🏁 Closing

**codegen‑blueprint is not a template generator.**
It is the **executable engine** that proves architecture can be treated as a **first‑class product** — explicit, observable, and verifiable at build time.
