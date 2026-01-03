# ${projectName}

${projectDescription}

---

## ✨ Project Summary

| Domain | Aspect | Value |
|------|--------|-------|
| 🔧 Tech Stack | Framework | `${framework}` |
| | Language | `${language}` |
| | Build Tool | `${buildTool}` |
| | Java | `${javaVersion}` |
| | Spring Boot | `${springBootVersion}` |
| 🏗 Architecture | Layout | `${layout}` |
| | Guardrails | `${guardrails}` |
| | Sample Code | `${sampleCode}` |

---

## 📑 Table of Contents

- [Project Summary](#-project-summary)
- [Coordinates](#-coordinates)
- [Quick Start](#-quick-start)
<#if features?has_content && ((features.h2)!false || (features.actuator)!false || (features.security)!false)>
- [Auto Configuration Notes](#-auto-configuration-notes)
</#if>
- [Project Layout](#-project-layout)
- [Architecture Guardrails](#-architecture-guardrails)
<#if sampleCode == "basic">
- [Included Sample (Basic)](#-included-sample-basic)
</#if>
- [Selected Dependencies](#-selected-dependencies)
- [Scope & Intent](#scope--intent)

---

## 📦 Coordinates

| Key          | Value            |
| ------------ | ---------------- |
| `groupId`    | `${groupId}`     |
| `artifactId` | `${artifactId}`  |
| `package`    | `${packageName}` |

---

## 🚀 Quick Start

```bash
# Build (fast feedback)
./mvnw clean package
```

<#if guardrails != "none">
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
</#if>

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

<#if features?has_content && ((features.h2)!false || (features.actuator)!false || (features.security)!false)>
---

## ⚙️ Auto Configuration Notes

<#if (features.h2)!false>
### H2 (for JPA)

This project includes an **in-memory H2 database** configuration because `spring-boot-starter-data-jpa` was selected.

* JDBC URL: `jdbc:h2:mem:${artifactId}`
* Console: `/h2-console` (if enabled)
</#if>

<#if (features.actuator)!false>
### Actuator

Basic actuator exposure is enabled:

* `/actuator/health`
* `/actuator/info`

</#if>
<#if (features.security)!false>

### Security

`spring-boot-starter-security` is included. Endpoints may require authentication depending on defaults and your configuration.
</#if>
</#if>

---

## 📁 Project Layout
<#if layout == "hexagonal">
```text
src
├─ main
│  ├─ java/${packageName?replace('.', '/')}
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
   └─ java/${packageName?replace('.', '/')}
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
<#else>
```text
src
├─ main
│  ├─ java/${packageName?replace('.', '/')}
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
   └─ java/${packageName?replace('.', '/')}
```

> This project follows a **Standard Layered Architecture**.
>
> * `controller` handles HTTP/API requests (including `controller/dto` boundary models)
> * `service` contains orchestration / application services
> * `repository` manages persistence and side effects
> * `domain` holds core domain models and domain services
> * `config` contains application and framework configuration
</#if>

---

<#if guardrails != "none">
## 🧩 Architecture Guardrails

**Authoritative references**
  * **Authoritative Rule Reference (contract semantics):**
    * [Architecture Guardrails Rulebook](https://github.com/blueprint-platform/codegen-blueprint/blob/main/docs/architecture/architecture-guardrails-rulebook.md)
    _(Defines guardrails semantics, vocabulary, and versioning guarantees)_

  * **Canonical Vocabulary (generated contract surface):**
    <#if layout == "hexagonal">
    * Hexagonal: `src/test/java/${packageName?replace('.', '/')}/architecture/archunit/HexagonalGuardrailsScope.java`
    </#if>
    <#if layout == "standard">
    * Standard: `src/test/java/${packageName?replace('.', '/')}/architecture/archunit/StandardGuardrailsScope.java`
    </#if>

> **Architecture Contract Notice**
>
> Architecture guardrails are evaluated **strictly against the package structure generated by Codegen Blueprint**.
> The generated layout is **part of the architecture contract**.
>
> Refactors that rename canonical package families, move code outside detected bounded contexts,
> or introduce unexpected top-level families may be detected as **architecture drift** and fail the build.

---

### What guardrails are (and are not)

Guardrails are:

* **Generated** (not handwritten)
* **Executable** (evaluated as tests)
* **Deterministic** (same input → same outcome)
* **Build-time only** (no runtime checks)

They are **not**:

* Documentation
* Conventions
* Runtime safety nets

Guardrails exist to make architectural boundaries **observable, enforceable, and non-bypassable**.

---

### How to run guardrails

Guardrails are evaluated during:

```bash
./mvnw verify
```

---

### Where the generated rules live

```text
src/test/java/${packageName?replace('.', '/')}/architecture/archunit/
```

These tests are **generated code**. They represent the architecture contract selected at generation time.

---

### Guardrails categories

All guardrails fall into three categories:

1. **Dependency guardrails**

Control allowed and forbidden dependency directions between layers/components.

2. **Boundary guardrails**

Protect public boundaries (e.g., REST) from leaking internal or domain types.

3. **Schema & contract guardrails**

Protect the **meaning and scope** of guardrails by validating the package schema and bounded-context detection.

> Schema guardrails are critical: without them, dependency rules can be accidentally bypassed by refactoring package names.

---

</#if>
<#if guardrails == "none">
### Guardrails status: disabled (`none`)

Architecture guardrails are **disabled** for this project.

* No architecture tests are generated
* The build will not fail due to architectural violations
* Architecture correctness relies on conventions and reviews only

To enable guardrails, regenerate the project with:

```bash
--guardrails basic   # adoption-friendly baseline
--guardrails strict  # contract-first, fail-fast enforcement
```
</#if>
<#if guardrails == "basic">
### Guardrails status: enabled (`basic`)

**Basic** is the **default** guardrails mode for Codegen Blueprint 1.0.0.

It is designed to:

* Prevent **obvious architectural violations**
* Preserve **minimal structural sanity**
* Detect **early architecture drift**
* Avoid over-constraining internal design

#### What basic guardrails enforce

Basic guardrails enforce a **baseline contract** that is intentionally adoption-friendly.

**Across both layouts (core guarantees):**
  * **Schema & scope sanity**
    * Guardrails must not be evaluated against an empty or misdetected scope (prevents “silent green builds”).
    * Detected bounded contexts must satisfy minimal schema expectations.

* **Bounded-context cycle prevention**
  * For each detected bounded context root, cyclic dependencies are forbidden across its **first-segment slices**.

---

<#if layout == "hexagonal">
#### Hexagonal — Basic (Ports & Adapters)

**Dependency guardrails**
  * `application` must **not depend on** `adapter`
  * `bootstrap` is a dependency **leaf** (non-bootstrap packages must not depend on `bootstrap`)

**Schema guarantees (per detected bounded context):**
  * The context must contain the canonical families:
    * `application`
    * `adapter`
    * `domain`

**What this means in practice**
  * You can refactor freely **inside** each family.
  * If you collapse/remove canonical families or move code outside detected contexts,
  guardrails will surface that as drift.
</#if>
<#if layout == "standard">

#### Standard — Basic (Layered)

**Dependency guardrails**
  * `controller` must **not depend on** `repository`
  * `domain` must **not depend on**:
    * `controller`
    * `service`
    * `repository`

**Schema guarantees (per detected bounded context):**
  * If a `controller` package exists, the context must also contain:
    * `service`
    * `domain`

> Persistence is intentionally optional in Basic; `repository` is not required for schema completeness.
>
</#if>

---

#### How to interpret Basic

Basic mode is a **stable floor**, not “perfect architecture”.

It aims to stop drift early while keeping adoption friction low.
If you want full contract integrity (rename-bypass prevention, boundary isolation, domain purity), use **Strict**.

To regenerate with strict guardrails:

```bash
--guardrails strict
```
</#if>

<#if guardrails == "strict">

### Guardrails status: enabled (`strict`)

**Strict** mode treats architecture as a **non-negotiable contract**.

Any structural drift, boundary leakage, or rename-based bypass will **fail the build deterministically**.

#### What strict guardrails enforce

Strict guardrails are generated **because this project was created with `--guardrails strict`**.

Strict mode enforces:

* **Full dependency direction contracts** (layout-specific)
  * Hexagonal: `application` must not depend on `adapter`; `bootstrap` remains a strict leaf
  * Standard: strict layered direction rules between `controller/service/repository/domain/config`
* **Domain purity**
  * Domain may depend only on **JDK types** and **other domain types**
  * No framework / delivery / persistence dependencies
* **Boundary isolation** (Spring Web only)
  * REST controllers must not expose domain types in method signatures
  * Boundary DTOs must not depend on domain types
* **Schema & rename enforcement**
  * Within each detected bounded context, classes must reside under the **canonical families**
  * This prevents silent bypass via package renaming or “unknown family” drift
* **Family-local cycle prevention**
  * Cycles are forbidden **inside each canonical family** (and adapter sub-families where applicable)

---

<#if layout == "hexagonal">
#### Hexagonal — Strict (Ports & Adapters)

**Dependency & direction**

* `application` must **not depend on** `adapter`
* `bootstrap` is a **strict leaf**

**Adapter isolation**

* `adapter.in` must **not depend on** `adapter.out`
* `adapter.out` must **not depend on** `adapter.in`

**Inbound adapter isolation**
  * `adapter.in` must **not depend on**:
    * `domain.service`
    * `domain.port.out`

**Application implementation isolation**
  * Adapters may depend only on **application ports** (`application.port..`)
  * Any dependency to application implementation packages is forbidden

**Domain purity**

* `domain` may depend only on:
  * JDK (`java.*`)
  * other domain types

**Schema & rename enforcement (contract integrity)**
  * Within each detected bounded context, classes must reside under the canonical families

* Within each detected bounded context, every class must reside under one of:
  * `adapter`
  * `application`
  * `domain`
  * `bootstrap`

**Cycle prevention (family-local)**

* No cyclic dependencies inside:
  * `adapter`, `application`, `domain`, `bootstrap`
* No cyclic dependencies inside:
  * `adapter.in..`, `adapter.out..`

**REST boundary isolation** (only when `spring-boot-starter-web` is present)

* `@RestController` under `adapter.in..` must not expose domain types in:
  * return types
  * parameters
  * generic signatures
* Inbound DTOs (`adapter.in..dto..`) must not depend on domain
</#if>
<#if layout == "standard">
#### Standard — Strict (Layered)

**Layer dependency enforcement**
  * `controller` must **not depend on**:
      * `repository`
      * `domain.service`
  * `service` must **not depend on** `controller`
  * `repository` must **not depend on** `service` or `controller`

**Domain purity**

* `domain` may depend only on:
  * JDK (`java.*`)
  * other domain types

**Schema & rename enforcement (contract integrity)**
  * Within each detected bounded context, classes must reside under the canonical families

* Within each detected bounded context, every class must reside under one of:
  * `controller`
  * `service`
  * `repository`
  * `domain`
  * `config`

**Cycle prevention (family-local)**

* No cyclic dependencies inside:
  * `controller`, `service`, `repository`, `domain`, `config`

**REST boundary isolation** (only when `spring-boot-starter-web` is present)

* `@RestController` under `controller..` must not expose domain types in:
  * return types
  * parameters
  * generic signatures
* Controller DTOs (`controller.dto..`) must not depend on domain
</#if>

---

#### Operational note

Strict mode is intentionally fail-fast.
If you want adoption-first enforcement with fewer constraints, use **Basic** instead.
</#if>

<#if sampleCode != "none">
<#--
Sample isolation contract (GA):

Generated sample code is placed under
${packageName}.bp.sample

This keeps user-owned packages clean and allows the sample
to be removed as a single, isolated subtree without affecting
production code.
-->
</#if>

<#if layout == "hexagonal" && sampleCode == "basic">

---

## 🧪 Included Sample (Basic)

Because `--sample-code basic` was selected, the project includes a minimal
end-to-end **Greeting** slice.

The sample is intentionally isolated under a dedicated package to keep
your own application code clean and to make removal straightforward.

```text
${packageName}.bp.sample
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
</#if>
<#if layout == "standard" && sampleCode == "basic">

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
${packageName}.bp.sample
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
</#if>

---

## 📚 Selected Dependencies
<#if dependencies?has_content>
| Dependency | Scope |
|-----------|-------|
<#list dependencies as d>
| `${d.groupId}:${d.artifactId}`<#if d.version?? && d.version?has_content>:`${d.version}`</#if> | <#if d.scope?? && d.scope?has_content>${d.scope}<#else>default</#if> |
</#list>
<#else>
> No additional dependencies were selected.
</#if>

---

## Scope & Intent

This generated project intentionally focuses on **structural correctness and observable architectural guardrails**.

Delivery concerns (CI/CD, containerization, deployment strategies) are left to the consuming team and platform standards.

🏗 Generated by **Blueprint Platform — Codegen Blueprint CLI**
