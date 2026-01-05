# Greeting

Proof: standard strict guardrails

---

## ✨ Project Summary

| Domain | Aspect | Value |
|------|--------|-------|
| 🔧 Tech Stack | Framework | `spring-boot` |
| | Language | `java` |
| | Build Tool | `maven` |
| | Java | `21` |
| | Spring Boot | `3.5.9` |
| 🏗 Architecture | Layout | `standard` |
| | Guardrails | `strict` |
| | Sample Code | `basic` |

---

## 📑 Table of Contents

- [Project Summary](#-project-summary)
- [Coordinates](#-coordinates)
- [Quick Start](#-quick-start)
- [Project Layout](#-project-layout)
- [Architecture Guardrails](#-architecture-guardrails)
- [Included Sample (Basic)](#-included-sample-basic)
- [Selected Dependencies](#-selected-dependencies)
- [Scope & Intent](#scope--intent)

---

## 📦 Coordinates

| Key          | Value            |
| ------------ | ---------------- |
| `groupId`    | `io.github.blueprintplatform`     |
| `artifactId` | `greeting-standard`  |
| `package`    | `io.github.blueprintplatform.greeting` |

---

## 🚀 Quick Start

```bash
# Build (fast feedback)
./mvnw clean package
```

```bash
# Verify architecture guardrails (recommended)
# Runs generated ArchUnit tests and fails fast on architectural violations
./mvnw verify
```

> **Why `mvn verify` matters**
>
> This project includes **generated architecture guardrails**
> that are evaluated at **build time**.
>
> Running `mvn verify` executes these guardrails and serves as
> the **proof of the architectural contract**.
>
> Any boundary violation will **fail the build immediately**.

```bash
# Run the application
./mvnw spring-boot:run
```

> **macOS / Linux note**
>
> If you get a `permission denied` error when running `./mvnw`, make the wrapper executable:
>
> ```bash
> chmod +x mvnw
> ```

> If Maven is installed globally, you may also use `mvn` instead of the wrapper.

---

## 📁 Project Layout
```text
src
├─ main
│  ├─ java/io/github/blueprintplatform/greeting
│  │  ├─ controller
│  │  │  └─ dto
│  │  ├─ service
│  │  ├─ repository
│  │  ├─ domain
│  │  │  ├─ model
│  │  │  └─ service
│  │  └─ config
│  └─ resources
└─ test
   └─ java/io/github/blueprintplatform/greeting
```

> This project follows a **Standard (Layered) Architecture**.
>
> * `controller` handles HTTP/API requests (including `controller/dto` boundary models)
> * `service` contains orchestration / application services
> * `repository` manages persistence and side effects
> * `domain` holds core domain models and domain services
> * `config` contains application and framework configuration

---

## 🧩 Architecture Guardrails

Guardrails are **generated ArchUnit tests** evaluated at **build time**.
They provide **deterministic** feedback during:

```bash
./mvnw verify
```

**References**

* Rulebook (semantics): [Architecture Guardrails Rulebook](https://github.com/blueprint-platform/codegen-blueprint/blob/main/docs/architecture/architecture-guardrails-rulebook.md)
* Canonical vocabulary (generated contract surface):
  * `src/test/java/io/github/blueprintplatform/greeting/architecture/archunit/StandardGuardrailsScope.java`
* Generated rules live under:
* `src/test/java/io/github/blueprintplatform/greeting/architecture/archunit/`

---




### Guardrails status: enabled (`standard` · `strict`)

Strict treats architecture as a **contract** (fail-fast):

* Full layered direction rules + schema integrity (rename-bypass prevention)
* Domain purity + boundary isolation (when applicable)

Enforced (high level):

* Layer direction rules across `controller/service/repository/domain/config`
* Domain purity: domain depends only on `java.*` + domain types
* Schema & rename enforcement per detected bounded context
* REST boundary isolation **only if** `spring-boot-starter-web` is present


---

## 🧪 Included Sample (Basic)

Because `--sample-code basic` was selected, the project includes a minimal
end-to-end **Greeting** slice that demonstrates a classic
**standard (layered) architecture** in its simplest, most readable form.

This sample is intentionally small.
Its purpose is **not** to showcase advanced patterns,
but to provide a clear baseline for how layers collaborate
in a traditional Spring Boot application.

The sample is intentionally isolated under a dedicated package
to keep your own application code clean and to make removal straightforward.

```text
io.github.blueprintplatform.greeting.bp.sample
```

This keeps user-owned packages clean and makes the sample removable as a single subtree.

### What this sample demonstrates

* an inbound **REST controller**
* an **application/service layer** orchestrating a use case
* a **pure domain model** (no Spring, no IO)
* a simple **audit side effect** triggered after the use case
* mapping from domain objects to HTTP response DTOs

The flow is deliberately straightforward:

```text
HTTP → Controller → Service → Domain → Service → Controller → DTO
```

This makes the sample easy to read, easy to debug, and easy to evolve.

### Sample REST endpoints

Base path:

```text
/api/v1/sample/greetings
```

### Available endpoints

**GET** `/api/v1/sample/greetings/default`

Returns a default greeting.

**GET** `/api/v1/sample/greetings?name=John`

Returns a personalized greeting.

Example calls:

```bash
curl -s http://localhost:8080/api/v1/sample/greetings/default | jq
curl -s "http://localhost:8080/api/v1/sample/greetings?name=John" | jq
```

### Where to look in the code

To understand the sample, start with these classes:

* **REST controller**
  * `bp/sample/controller/GreetingController`

---

* **Application / use-case service**
  * `bp/sample/service/GreetingService`

---

* **Pure domain logic**
  * `bp/sample/domain/service/GreetingDomainService`
  * `bp/sample/domain/model/Greeting`

---

* **Audit side effect**
  * `bp/sample/repository/GreetingAuditRepository`

Each layer has a single, focused responsibility and communicates only with the layer directly below it.

### How to use this sample

You can use this sample in two ways:

* as a **baseline reference** for a standard layered Spring Boot project
* as a **starting point** to gradually introduce more advanced concepts
(transactions, persistence, messaging, or even a future migration to hexagonal architecture)

This sample is intentionally conservative by design.
It favors clarity over abstraction, and explicit flow over indirection.

---

## 📚 Selected Dependencies
| Dependency | Scope |
|-----------|-------|
| `org.springframework.boot:spring-boot-starter-web` | default |

---

## Scope & Intent

This generated project intentionally focuses on **structural correctness and observable architectural guardrails**.

Delivery concerns (CI/CD, containerization, deployment strategies) are left to the consuming team and platform standards.

🏗 Generated by **Blueprint Platform — Codegen Blueprint CLI**