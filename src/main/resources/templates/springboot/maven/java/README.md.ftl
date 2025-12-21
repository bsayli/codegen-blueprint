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
| | Enforcement | `${enforcement}` |
| | Sample Code | `${sampleCode}` |
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
# Build (using Maven Wrapper)
./mvnw clean package
```

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
│  │  │  └─ service
│  │  ├─ domain
│  │  │  ├─ model
│  │  │  └─ policy
│  │  └─ bootstrap
│  └─ resources
└─ test
   └─ java/${packageName?replace('.', '/')}
```

> This project follows **Hexagonal Architecture (Ports & Adapters)**.
>
> * `domain` contains pure business rules (framework-free)
> * `application` orchestrates use cases and defines ports
> * `adapter` contains inbound/outbound implementations (REST, persistence, messaging)
> * `bootstrap` wires everything together
<#else>

```text
src
├─ main
│  ├─ java/${packageName?replace('.', '/')}
│  │  ├─ controller
│  │  ├─ service
│  │  ├─ repository
│  │  ├─ domain
│  │  │  └─ model
│  │  └─ config
│  └─ resources
└─ test
   └─ java/${packageName?replace('.', '/')}
```

> This project follows a **Standard Layered Architecture**.
>
> * `controller` handles HTTP/API requests
> * `service` contains business logic
> * `repository` manages persistence
> * `domain` holds core domain models
> * `config` contains application and framework configuration
</#if>
<#if layout == "hexagonal">

---

## 🧱 Hexagonal Architecture (Ports & Adapters)

This project was generated with the **hexagonal** layout:

```bash
--layout hexagonal
```

Your `src/main/java/${packageName?replace('.', '/')}` structure follows these top-level modules:

* `domain` – core business rules (no Spring dependencies)
* `application` – use cases + port contracts
* `adapter` – inbound/outbound integrations (REST, DB, messaging, etc.)
* `bootstrap` – wiring/configuration (Spring Boot entrypoint)

### What this means in practice

* Dependencies point **inward** (adapters depend on application/domain; domain depends on nothing).
* The application layer exposes **ports**; adapters implement or call those ports.
</#if>

---

## 🧩 Architecture Enforcement
<#if enforcement == "none">
Architecture enforcement is **disabled** for this project.

* No architectural rules are generated
* The build will not fail due to boundary violations
* Architecture is guided by conventions and reviews only

You can enable build-time guardrails by generating the project with:

```bash
--enforcement basic   # core architectural guardrails
--enforcement strict  # strict, fail-fast enforcement
```
</#if>
<#if enforcement == "basic">
Architecture enforcement is **enabled (basic)**.

This project includes **generated ArchUnit tests** that enforce **core architectural guardrails** at build time.

### What is enforced

* Core dependency direction between architectural layers
* Protection of the domain from outward dependencies
* Prevention of the most common architectural shortcuts
* Early detection of structural drift

### How enforcement works

* Rules are generated as **executable tests**
* Violations are detected during:

```bash
mvn verify
```

* If a rule is violated, **the build fails immediately**

### Where the rules live

```text
src/test/java/${packageName?replace('.', '/')}/architecture/archunit/
```
</#if>
<#if enforcement == "strict">
Architecture enforcement is **enabled (strict)**.

This project includes **strict, fail-fast architectural guardrails** generated as executable ArchUnit tests.
Any architectural drift will **break the build deterministically**.

### What is enforced (strict)
<#if layout == "hexagonal">

For **Hexagonal Architecture**, strict enforcement guarantees:

* **Dependency direction**
    * Application does not depend on adapters
    * Bootstrap is a dependency leaf

---

* **Adapter direction isolation**
  * Inbound adapters must not depend on outbound adapters
  * Outbound adapters must not depend on inbound adapters

---

* **Inbound adapter → domain isolation**
  * Inbound adapters must not depend on domain services
  * Inbound adapters must not depend on domain outbound ports

---

* **Domain purity**
  * Domain depends only on JDK types and other domain types

---

* **Ports isolation**
  * Adapters may depend only on application **ports**, not implementations

---

* **REST boundary isolation** (when `spring-boot-starter-web` is present)
  * REST controllers must not expose domain types in method signatures
  * Adapter DTOs must not depend on domain

---

* **Package cycle prevention**
  * No cyclic dependencies across top-level packages
  * No cycles inside adapter subpackages
</#if>
<#if layout == "standard">
For **Standard (Layered) Architecture**, strict enforcement guarantees:

* **Layer dependency direction and bypass prevention**
  * Controllers must not depend on repositories
  * Controllers must not depend on domain services
  * Services must not depend on controllers
  * Repositories must not depend on services or controllers

---

* **Domain purity**
  * Domain depends only on JDK types and other domain types

---

* **REST boundary isolation** (when `spring-boot-starter-web` is present)
  * REST controllers must not expose domain types in method signatures
  * Controller DTOs must not depend on domain

---

* **Package cycle prevention**
  * No cyclic dependencies between top-level packages
</#if>

### How enforcement works

* Rules are generated automatically based on:

* selected **layout**
* selected **enforcement mode**
* selected **dependencies**

* Enforcement happens at **build time only** — no runtime checks

```bash
mvn verify  # fails immediately on violation
```

### Where the rules live

```text
src/test/java/${packageName?replace('.', '/')}/architecture/archunit/
```

> These rules are generated code.
> They are part of the project contract and should not be edited manually.
</#if>
<#if layout == "hexagonal" && sampleCode == "basic">

---

## 🧪 Included Sample (Basic)

Because `--sample-code basic` was selected, the project includes a minimal end-to-end **Greeting** slice that demonstrates:

* an inbound **REST adapter**
* an application **use case** via an input port
* domain/application models
* mapping of use case output to HTTP response DTO

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

* **Inbound REST adapter**
  * `adapter/sample/in/rest/GreetingController`

---

* **Application port (input)**
  * `application/sample/port/in/GetGreetingPort`

---

* **Use case implementation**
  * `application/sample/usecase/...` (varies by generator version)

You can use this sample in two ways:

* as a **teaching reference** for the hexagonal boundaries in this codebase
* as a **starting slice** to evolve into your real business modules
</#if>
<#if layout == "standard" && sampleCode == "basic">

---

## 🧪 Included Sample (Basic)

Because `--sample-code basic` was selected, the project includes a minimal end-to-end **Greeting** slice that demonstrates a classic **standard (layered) architecture** in its simplest, most readable form.

This sample is intentionally small. Its purpose is **not** to showcase advanced patterns, but to provide a clear baseline for how layers collaborate in a traditional Spring Boot application.

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
  * `controller/sample/GreetingController`

---

* **Application / use-case service**
  * `service/sample/GreetingService`

---

* **Pure domain logic**
  * `domain/sample/service/GreetingDomainService`
  * `domain/sample/model/Greeting`

---

* **Audit side effect**
  * `repository/sample/GreetingAuditRepository`

Each layer has a single, focused responsibility and communicates only with the layer directly below it.

### How to use this sample

You can use this sample in two ways:

* as a **baseline reference** for a standard layered Spring Boot project
* as a **starting point** to gradually introduce more advanced concepts
(transactions, persistence, messaging, or even a future migration to hexagonal architecture)

This sample is intentionally conservative by design.
It favors clarity over abstraction, and explicit flow over indirection.

If you are comfortable with this structure, you are well-positioned to understand and adopt more advanced architectural styles later on.

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

## 🧩 Next Steps

* ✔ Structure your domain and use case logic
* ✔ Add CI/CD pipelines or Docker support
* ✔ Configure profiles in `application.yml`
* ✔ Add more Spring Boot starters if needed

---

🏗 Generated by **Blueprint Platform — Codegen Blueprint CLI**
