<#if guardrails == "none">
## 🧩 Architecture Guardrails

> ⚠️ **Guardrails disabled** (`--guardrails none`)

This project ships **no generated ArchUnit guardrails**.
Architecture correctness relies on conventions and reviews.

To enable guardrails, regenerate with:

```bash
--guardrails basic   # adoption-friendly baseline
--guardrails strict  # contract-first, fail-fast
````
<#else>
## 🧩 Architecture Guardrails

Guardrails are **generated ArchUnit tests** evaluated at **build time**.
They provide **deterministic** feedback during:

```bash
./mvnw verify
```

**References**

* Rulebook (semantics): [Architecture Guardrails Rulebook](https://github.com/blueprint-platform/codegen-blueprint/blob/main/docs/architecture/architecture-guardrails-rulebook.md)
* Canonical vocabulary (generated contract surface):
<#if layout == "hexagonal">
  * `src/test/java/${packageName?replace('.', '/')}/architecture/archunit/HexagonalGuardrailsScope.java`
<#elseif layout == "standard">
  * `src/test/java/${packageName?replace('.', '/')}/architecture/archunit/StandardGuardrailsScope.java`
</#if>
* Generated rules live under:
* `src/test/java/${packageName?replace('.', '/')}/architecture/archunit/`

---

<#if layout == "hexagonal" && guardrails == "basic">
### Guardrails status: enabled (`hexagonal` · `basic`)

Basic is an **adoption-friendly baseline**:

* Prevents obvious drift and schema bypass at build time
* Keeps freedom **inside** canonical families

Enforced (high level):

* `application` must **not** depend on `adapter`
* `bootstrap` is a **leaf**
* Context schema sanity: canonical families must exist per detected context (`adapter/application/domain`)
* Cycle prevention at bounded-context slice level
</#if>

<#if layout == "hexagonal" && guardrails == "strict">

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
</#if>

<#if layout == "standard" && guardrails == "basic">
### Guardrails status: enabled (`standard` · `basic`)

Basic is an **adoption-friendly baseline**:

* Stops obvious layered drift at build time
* Keeps internal freedom while preserving minimal structure

Enforced (high level):

* `controller` must **not** depend on `repository`
* `domain` must **not** depend on `controller/service/repository`
* Context schema sanity: if any required family exists, require full set (`controller/service/domain`)
* Cycle prevention at bounded-context slice level

> Note: `repository` is intentionally optional in Basic.
>
</#if>

<#if layout == "standard" && guardrails == "strict">
### Guardrails status: enabled (`standard` · `strict`)

Strict treats architecture as a **contract** (fail-fast):

* Full layered direction rules + schema integrity (rename-bypass prevention)
* Domain purity + boundary isolation (when applicable)

Enforced (high level):

* Layer direction rules across `controller/service/repository/domain/config`
* Domain purity: domain depends only on `java.*` + domain types
* Schema & rename enforcement per detected bounded context
* REST boundary isolation **only if** `spring-boot-starter-web` is present
</#if>

</#if>