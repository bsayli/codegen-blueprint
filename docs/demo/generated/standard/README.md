# Greeting (Standard Layered)

Greeting sample built with standard layered architecture

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
| | Enforcement | `strict` |
| | Sample Code | `basic` |
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


---

## ⚙️ Auto Configuration Notes


### H2 (for JPA)

This project includes an **in-memory H2 database** configuration because `spring-boot-starter-data-jpa` was selected.

* JDBC URL: `jdbc:h2:mem:greeting-standard`
* Console: `/h2-console` (if enabled)



### Actuator

Basic actuator exposure is enabled:

* `/actuator/health`
* `/actuator/info`



---

## 📁 Project Layout

```text
src
├─ main
│  ├─ java/io/github/blueprintplatform/greeting
│  │  ├─ controller
│  │  ├─ service
│  │  ├─ repository
│  │  ├─ domain
│  │  │  └─ model
│  │  └─ config
│  └─ resources
└─ test
   └─ java/io/github/blueprintplatform/greeting
```

> This project follows a **Standard Layered Architecture**.
>
> * `controller` handles HTTP/API requests
> * `service` contains business logic
> * `repository` manages persistence
> * `domain` holds core domain models
> * `config` contains application and framework configuration

---

## 🧩 Architecture Enforcement


Architecture enforcement is **enabled (strict)**.

This project includes **strict, fail-fast architectural guardrails** generated as executable ArchUnit tests.
Any architectural drift will **break the build deterministically**.

### What is enforced (strict)


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
src/test/java/io/github/blueprintplatform/greeting/architecture/archunit/
```

> These rules are generated code.
> They are part of the project contract and should not be edited manually.





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


---

## 📚 Selected Dependencies

| Dependency | Scope |
|-----------|-------|
    | `org.springframework.boot:spring-boot-starter-actuator` | default |
    | `org.springframework.boot:spring-boot-starter-data-jpa` | default |
    | `org.springframework.boot:spring-boot-starter-web` | default |

---

## 🧩 Next Steps

* ✔ Structure your domain and use case logic
* ✔ Add CI/CD pipelines or Docker support
* ✔ Configure profiles in `application.yml`
* ✔ Add more Spring Boot starters if needed

---

🏗 Generated by **Blueprint Platform — Codegen Blueprint CLI**
