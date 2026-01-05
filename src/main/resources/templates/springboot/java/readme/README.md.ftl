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

> This project follows a **Standard (Layered) Architecture**.
>
> * `controller` handles HTTP/API requests (including `controller/dto` boundary models)
> * `service` contains orchestration / application services
> * `repository` manages persistence and side effects
> * `domain` holds core domain models and domain services
> * `config` contains application and framework configuration
</#if>

---

<#include "_readme_guardrails.ftl">

---

<#include "_readme_sample.ftl">

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