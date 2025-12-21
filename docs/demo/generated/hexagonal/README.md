# Greeting

Greeting sample built with hexagonal architecture

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
| | Enforcement | `strict` |
| | Sample Code | `basic` |

---

## 📑 Table of Contents

- [Project Summary](#-project-summary)
- [Coordinates](#-coordinates)
- [Quick Start](#-quick-start)
- [Auto Configuration Notes](#-auto-configuration-notes)
- [Project Layout](#-project-layout)
- [Architecture Enforcement](#-architecture-enforcement)
- [Included Sample (Basic)](#-included-sample-basic)
- [Selected Dependencies](#-selected-dependencies)
- [Scope & Intent](#scope--intent)

---

## 📦 Coordinates

| Key          | Value            |
| ------------ | ---------------- |
| `groupId`    | `io.github.blueprintplatform`     |
| `artifactId` | `greeting`  |
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

* JDBC URL: `jdbc:h2:mem:greeting`
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
   └─ java/io/github/blueprintplatform/greeting
```

> This project follows **Hexagonal Architecture (Ports & Adapters)**.
>
> * `domain` contains pure business rules (framework-free)
> * `application` orchestrates use cases and defines ports
> * `adapter` contains inbound/outbound implementations (REST, persistence, messaging)
> * `bootstrap` wires everything together

---

## 🧩 Architecture Enforcement
Architecture enforcement is **enabled (strict)**.

This project includes **strict, fail-fast architectural guardrails** generated as executable ArchUnit tests.

Any architectural drift will **break the build deterministically**.

### What is enforced (strict)
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

---

## 📚 Selected Dependencies
| Dependency | Scope |
|-----------|-------|
| `org.springframework.boot:spring-boot-starter-actuator` | default |
| `org.springframework.boot:spring-boot-starter-data-jpa` | default |
| `org.springframework.boot:spring-boot-starter-web` | default |

---

## Scope & Intent

This generated project intentionally focuses on **structural correctness and architectural enforcement**.

Delivery concerns (CI/CD, containerization, deployment strategies) are left to the consuming team and platform standards.

🏗 Generated by **Blueprint Platform — Codegen Blueprint CLI**
