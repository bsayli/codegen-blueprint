<#if sampleCode == "none">
## 🧪 Included Sample

> ℹ️ **Sample code disabled** (`--sample-code none`)
>
> This project includes **no generated sample slice**.
> To include the basic sample, regenerate with:
>
> ```bash
> --sample-code basic
> ```
<#else>
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