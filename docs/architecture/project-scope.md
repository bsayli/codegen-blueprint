## Project Scope — Generated Output for 1.0.0 (Target)

🔹 This document defines what a generated project **must include** for the 1.0.0 GA release.
🔹 It is used to validate the output of Codegen Blueprint for architectural consistency.

---

### 🎯 Goal

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
--layout hexagonal-basic
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

Later versions → arch enforcement tests (e.g., ArchUnit)

---

## 📙 Minimal Documentation (Included)

`README.md` must contain:

* Build & run instructions
* Version badges (Java, Spring Boot)
* CLI usage example of Codegen Blueprint:

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli \
  springboot \
  --group-id com.example \
  --artifact-id demo-app \
  --name "Demo App" \
  --description "Demo application for Acme" \
  --package-name com.example.demo \
  --dependency WEB
```

Optional architecture-aware generation must be documented:

```bash
--layout hexagonal-basic
```

> When omitted, standard Spring Boot layout is generated.

---

## ❌ What is **not** included in 1.0.0

| Not Included            | Reason                               |
| ----------------------- | ------------------------------------ |
| REST inbound adapter    | Planned for post-1.0.0               |
| Security defaults       | Delayed — avoid opinionated coupling |
| Observability setup     | Future profile variation             |
| Multi-module generation | Later major iteration                |

These remain **explicitly out of scope** to keep GA concise.

---

## ✔ Definition of Done (DoD)

A project generated with current profile must:

* Compile & run with no modification
* Contain a correct package namespace
* Contain minimal test scaffolding
* Follow optional hexagonal layout **when selected**
* Ship as a releasable starter template for production use

---

### Status Tracking

| Requirement Area | Status    | Notes                                  |
| ---------------- | --------- | -------------------------------------- |
| Standard layout  | 🚧 In Dev | Validate ZIP output naming             |
| Hexagonal layout | 🚧 In Dev | Structure exists, refine templates     |
| App metadata     | ✔ Done    | Already enforced by CLI                |
| Tests            | ⚠ Partial | Basic tests exist, validate guarantees |
| Documentation    | 🚧 In Dev | Needs stabilization                    |

---

> This document evolves with each milestone and defines the quality bar for 1.0.0 GA.
