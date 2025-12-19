# Contributing Guide

Thank you for your interest in contributing to **Codegen Blueprint**.

This project is not a traditional code generator.
It is an **architecture-first engine** whose primary responsibility is to turn architectural intent into **deterministic, enforceable output**.

Contributions are welcome — but only when they **preserve and strengthen the guarantees** this project exists to provide.

> If you’re looking to add templates quickly or experiment freely, this may not be the right repository.
> If you care about **architecture as a product**, you’re in the right place.

---

## Table of Contents

* [Questions & discussion](#questions--discussion)
* [Contribution philosophy](#contribution-philosophy)
* [What you can contribute](#what-you-can-contribute)
* [Non‑negotiable rules](#nonnegotiable-rules)
* [Development setup](#development-setup)
* [Project structure](#project-structure)
* [Commit & PR conventions](#commit--pr-conventions)
* [Pull request checklist](#pull-request-checklist)
* [Security](#security)
* [License](#license)

---

## Questions & Discussion

Before opening an issue or pull request:

* Use **GitHub Discussions** for:

    * design ideas
    * architectural questions
    * roadmap or philosophy discussions
* Use **Issues** only for:

    * concrete bugs
    * reproducible failures
    * well‑defined improvement proposals

Please search existing discussions and issues first.

---

## Contribution Philosophy

**Codegen Blueprint treats architecture as a first‑class, executable product.**

That means:

* “It works” is **not sufficient**
* “It looks cleaner” is **not sufficient**
* “It generates more code” is often a **negative signal**

Every contribution is evaluated against a single question:

> **Does this preserve or strengthen the architectural guarantees of the generator?**

Changes that weaken determinism, blur boundaries, or bypass enforcement will not be accepted — even if they are technically correct.

---

## What You Can Contribute

Typical accepted contribution categories:

* **docs** — README, guides, architecture explanations
* **profile** — new or improved stack profiles
* **artifact** — new artifacts or pipeline improvements
* **governance** — architecture enforcement rules (ArchUnit)
* **cli** — CLI options or contract improvements
* **engine** — domain / application / adapter internals
* **ci** — build, test, or verification automation

Large or cross‑cutting changes should be discussed **before** implementation.

---

## Non‑Negotiable Rules

The following rules are **hard constraints**.
Pull requests violating any of these will be rejected.

* Generated projects **must pass `mvn verify`** without manual intervention
* The **domain layer remains framework‑free** (no Spring, no adapters)
* Dependency direction is preserved:

```
domain ← application ← adapter ← bootstrap
```

* Generation is **deterministic**:

    * same input → same output
    * no ordering randomness
    * no environment‑dependent behavior
* Generated code is changed **only via templates or models** — never manually
* Architecture enforcement rules must:

    * be explicit
    * be testable
    * fail the build deterministically

These are product guarantees, not style preferences.

---

## Development Setup

### Prerequisites

* Java **21** (Temurin recommended)
* Maven **3.9+**

### Build & Verify

From the repository root:

```bash
mvn -q -ntp clean verify
```

This command:

* builds the generator engine
* runs unit and integration tests
* generates **standard** and **hexagonal** sample projects
* verifies that generated projects themselves build successfully

If this command fails locally, the PR will fail in CI.

---

## Project Structure

```
domain/       # Pure domain model and rules
application/  # Use cases and orchestration
adapter/      # Inbound/outbound adapters
bootstrap/    # Spring wiring and runtime configuration
```

Additional concepts:

* **Profiles** define the **technology stack** (framework, build tool, language)
  and the **ordered artifact pipeline**.
* **Blueprint options** (layout, enforcement, sample-code) drive **template
  selection and output shape** within artifacts.
* **Artifacts** define the concrete generated outputs
  (build files, source layout, config, samples, governance tests).
* **Governance** defines **executable architecture enforcement**
  (e.g. generated ArchUnit rules).

The generator engine must never depend on generated projects.

---

## Commit & PR Conventions

### Commit Prefixes

Use clear, descriptive prefixes:

* `feature:` — new capability
* `bugfix:` — bug fix
* `docs:` — documentation
* `refactor:` — internal restructuring
* `test:` — tests only
* `ci:` — CI or automation
* `chore:` — maintenance

Examples:

* `feature(governance): add strict hexagonal boundary rules`
* `docs: clarify enforcement guarantees in README`

### Pull Request Scope

* Keep PRs **small and focused**
* One concern per PR
* Architectural changes should include rationale

Large changes without prior discussion may be closed unreviewed.

---

## Pull Request Checklist

Before submitting a PR, confirm:

* [ ] Scope is minimal and intentional
* [ ] `mvn -q -ntp clean verify` passes locally
* [ ] Generated output changes are intentional and explained
* [ ] Architectural guarantees remain intact
* [ ] Documentation updated if behavior changed
* [ ] Commit messages follow conventions

PRs missing these basics will not be merged.

---

## Security

Security issues **must not** be reported via public issues or PRs.

Please follow the instructions in **SECURITY.md**.

---

## License

This project is licensed under the **MIT License**.

By contributing, you agree that your contributions are licensed under the same terms.

---

> Codegen Blueprint values **clarity over velocity** and **integrity over convenience**.
> Thank you for contributing with that mindset.
