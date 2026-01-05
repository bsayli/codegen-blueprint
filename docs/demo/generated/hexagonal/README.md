# Greeting

Proof: hexagonal strict guardrails

---

## ✨ Project Summary

| Domain | Aspect | Value |
|------|--------|-------|
| 🔧 Tech Stack | Framework | `spring-boot` |
| | Language | `java` |
| | Build Tool | `maven` |
| | Java | `21` |
| | Spring Boot | `3.5.9` |
| 🏗 Architecture | Layout | `hexagonal` |
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
| `artifactId` | `greeting-hex`  |
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
│  │  ├─ adapter
│  │  │  ├─ in
│  │  │  └─ out
│  │  ├─ application
│  │  │  ├─ port
│  │  │  │  ├─ in
│  │  │  │  └─ out
│  │  │  └─ usecase
│  │  ├─ domain
│  │  │  ├─ model
│  │  │  └─ service
│  │  └─ bootstrap
│  └─ resources
└─ test
   └─ java/io/github/blueprintplatform/greeting
```

> This project follows **Hexagonal Architecture (Ports & Adapters)**.
>
> * `domain` contains pure business rules (framework-free)
> * `application` orchestrates use cases and defines ports
>
>   * `application/port` defines inbound and outbound ports
>   * `application/usecase` contains use case implementations
> * `adapter` contains inbound/outbound implementations (REST, persistence, messaging)
> * `bootstrap` wires everything together

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
  * `src/test/java/io/github/blueprintplatform/greeting/architecture/archunit/HexagonalGuardrailsScope.java`
* Generated rules live under:
* `src/test/java/io/github/blueprintplatform/greeting/architecture/archunit/`

---



### Guardrails status: enabled (`hexagonal` · `strict`)

Strict treats architecture as a **contract** (fail-fast):

* Full direction rules + schema integrity (rename-bypass prevention)
* Domain purity + boundary isolation (when applicable)

Enforced (high level):

* `application` must **not** depend on `adapter`
* `bootstrap` is a **strict leaf**
* `adapter.in` ↔ `adapter.out` isolation (no cross-deps)
* Domain purity: domain depends only on `java.*` + domain types
* Schema & rename enforcement per detected bounded context
* REST boundary isolation **only if** `spring-boot-starter-web` is present




---

## 🧪 Included Sample (Basic)

Because `--sample-code basic` was selected, the project includes a minimal
end-to-end **Greeting** slice.

The sample is intentionally isolated under a dedicated package to keep
your own application code clean and to make removal straightforward.

```text
io.github.blueprintplatform.greeting.bp.sample
```

This keeps user-owned packages clean and makes the sample removable as a single subtree.

### What this sample demonstrates

* an inbound **REST adapter**
* an application **use case** exposed via an **input port**
* domain + application models
* mapping of use case output to HTTP response DTOs

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

Start here:

* **Inbound REST adapter**
  * `bp/sample/adapter/in/rest/GreetingController`

---

* **Application port (input)**
  * `bp/sample/application/port/in/GetGreetingPort`

---

* **Use case implementation**
  * `bp/sample/application/usecase/...`

---

* **Domain model + domain service**
  * `bp/sample/domain/model/...`
  * `bp/sample/domain/service/...`

You can use this sample in two ways:

* as a **teaching reference** for hexagonal boundaries
* as a **starting slice** you can copy/adapt into your own user-owned packages

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