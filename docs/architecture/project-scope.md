# Project Scope — Generated Output for 1.0.0 GA

> This document defines what a generated project **must include** for the 1.0.0 GA release — a checklist for architectural consistency.

---

## 🎯 Goal

Ensure every new generated Spring Boot service starts **clean**, **testable**, and **architecture-aligned** — not just a folder dump.

Current target profile:

```
springboot-maven-java
```

---

## 🏗 Standard Project Structure (Required)

The generated project **must include**:

```
<artifactId>/
 ├── pom.xml
 ├── src/main/java/<basePackage>/
 │    └── Application.java (Main class)
 ├── src/test/java/<basePackage>/
 │    └── ApplicationTests.java (Basic test entrypoint)
 ├── src/main/resources/
 │    └── application.yml
 ├── .gitignore
 └── README.md (minimal usage notes)
```

---

## 🔌 Technology Baseline

| Component   | Target Version | Required |
| ----------- | -------------- | -------- |
| Java        | 21             | ✔        |
| Spring Boot | 3.5.x          | ✔        |
| Maven       | 3.9+           | ✔        |

---

## 🧩 Architecture Option (Opt-In)

If user selects:

```bash
--layout hexagonal
```

then structure becomes:

```
<artifactId>/
 ├── domain/        // pure business rules
 ├── application/   // orchestrates ports
 ├── adapters/      // inbound & outbound
 └── bootstrap/     // wiring & config
```

Requirements:

* No Spring APIs inside **domain**
* Basic unit tests scaffolding provided
* Naming templates consistent with conventions

---

## 📦 Application Metadata Generation

| Artifact     | Status                         |
| ------------ | ------------------------------ |
| groupId      | ✔ mandatory param              |
| artifactId   | ✔ mandatory param              |
| package name | ✔ enforced format              |
| project name | ✔ optional, defaults correctly |

Rules:

* `basePackage` **must** reflect provided groupId / artifactId
* Naming must be validated and normalized (no invalid characters)

---

## 🧪 Testing Guarantees

Generated project must include:

* A working test pipeline via `mvn verify`
* `@SpringBootTest` example test
* Structure that encourages future unit testing

> Future releases → architecture rule enforcement (ArchUnit)

---

## 📙 Minimal Documentation (Included)

`README.md` must contain:

* Build & run instructions
* Version badges (Java / Spring Boot)
* CLI usage example of Codegen Blueprint:

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli \
  springboot \
  --group-id com.acme \
  --artifact-id demo \
  --name "Demo App" \
  --description "Demo application for Acme" \
  --package-name com.acme.demo \
  --layout hexagonal \   # optional architecture flag
  --dependency web \
  --dependency data_jpa \
  --dependency validation
```

Optional architecture-aware generation must be documented:

```bash
--layout hexagonal
```

> If omitted → standard Spring Boot layout

---

## ❌ Explicitly Out of Scope (1.0.0 GA)

| Not Included            | Reason                             |
| ----------------------- | ---------------------------------- |
| REST inbound adapter    | Planned for post-1.0.0             |
| Security defaults       | Avoid opinionated coupling (later) |
| Observability setup     | Future profile variation           |
| Multi-module generation | Larger iteration required          |

These remain out of GA scope **to keep the release focused**.

---

## ✔ Definition of Done (DoD)

A generated project must:

* Compile & run immediately
* Contain correct package namespace
* Include minimal test scaffolding
* Apply optional hexagonal layout **when selected**
* Be releasable as a **production‑starter template**

---

## 📊 Status Tracking — GA Confidence

| Requirement Area | Status             | Notes                                            |
| ---------------- | ------------------ | ------------------------------------------------ |
| Standard layout  | ✔ GA-ready         | ZIP output naming validated via CLI tests        |
| Hexagonal layout | ✔ Opt-in (Limited) | Structure generates correctly — enforcement next |
| App metadata     | ✔ Complete         | Rules apply consistently                         |
| Tests            | ✔ Minimal Complete | Test entrypoints verified                        |
| Documentation    | ✔ Ready            | README usage aligned                             |

---

> This document evolves with each milestone and defines the quality bar for **1.0.0 GA**.
