# Engine Scope — What Codegen Blueprint Enforces in 1.0.0

> **Short version:**
> In **1.0.0**, Codegen Blueprint is a **profile-driven, architecture-aware project generator**.
> It guarantees a **clean, consistent starting point** — and prepares the ground for future *enforcement* of architectural rules, without over-claiming.

---

## 1. Why this document exists

This document describes:

* what the **blueprint engine actually enforces** in `1.0.0`
* what is **explicitly out of scope for now**
* how this connects to the long-term vision of **“Executable Architecture”**

It is the internal contract between:

* what the README promises to users, and
* what the engine currently guarantees at generation time.

---

## 2. Mental model — Platform, profiles, blueprints

At a high level:

* **Platform:** `blueprint-platform` — the long-term home for reusable engines, templates, and governance.
* **Engine:** `codegen-blueprint` — the CLI-driven blueprint engine.
* **Profiles:** e.g. `springboot-maven-java` — define **stack + build tool + language**.
* **Blueprints:** internal, deterministic keys that map to template artifacts
  *(e.g. `SPRING_BOOT_MAIN_CLASS`, `APPLICATION_YAML`, `POM_XML`).*

In 1.0.0, the primary focus is:

> “Given a profile and metadata, consistently generate a **clean Spring Boot project skeleton** that is ready to evolve into a stricter architecture.”

---

## 3. What is enforced in 1.0.0

### 3.1 Generation-time guarantees

For the `springboot-maven-java` profile, the engine **guarantees**:

1. **Deterministic project layout**

    * Single module Spring Boot project
    * Standard Maven layout:

      ```text
      src/main/java
      src/test/java
      src/main/resources
      src/test/resources
      ```
    * Base package derived from `groupId` + `artifactId` (or explicit `packageName`).

2. **Consistent naming & identity**

    * `groupId`, `artifactId`, project `name`, and `packageName` are normalized and used consistently across:

        * `pom.xml`
        * main application class
        * package structure
    * Generated main class follows a predictable convention, e.g.:

      ```text
      <BasePackage>.<PascalCasedArtifact>Application
      ```

3. **Minimal, ready-to-extend Spring Boot setup**

    * A runnable **Spring Boot 3 + Java 21** application with:

        * `spring-boot-starter` dependencies wired in via the generated `pom.xml`
        * minimal `application.yml` with environment-ready placeholders
    * No extra “demo noise” controllers or random sample code.

4. **Test entry points from day zero**

    * A basic test entrypoint is generated so that:

        * `mvn test` / `mvn verify` runs successfully on a fresh project
        * test package mirrors main package layout

5. **Separation between engine and templates**

    * The core engine does **not** embed Spring or file-system details.
    * All stack-specific concerns are pushed into:

        * profile configuration
        * template layer (FTL / other templating)
    * This is crucial for future multi-stack support.

6. **Profiles as first-class configuration**

      * The CLI commands you run are **profile-driven**, activated explicitly with `--cli`:

   ```bash
   java -jar codegen-blueprint-1.0.0.jar \
     --cli \
     springboot \
     --group-id com.example \
     --artifact-id demo \
     --name "Demo App" \
     --description "Demo application for Acme" \
     --package-name com.example.demo \
     --dependency WEB
   ```
   
   The selected profile controls:
   
   * which templates are used
   * which generated artifacts are included
   * how metadata is applied to the project layout

---

### 3.2 What “architecture-aware” means *today*

In 1.0.0, “architecture-aware” means:

* The engine is **designed to support architecture layouts** (like hexagonal) as **profiles / options**.

* The README and diagrams show a **target hexagonal layout**:

  ```text
  domain       // business rules (no Spring dependencies)
  application  // orchestrates ports
  adapters     // inbound & outbound adapters
  bootstrap    // Spring wiring & configuration
  ```

* However, enforcement is **deliberately minimal**:

    * the engine currently generates a **clean, standard Spring Boot skeleton**,
    * it **does not yet** generate or enforce full hexagonal layering for all profiles.

In other words:

> The engine is **architecture-ready**, not yet **architecture-policing**.

---

## 4. What is *not* enforced (yet)

To avoid over-promising, the following are **explicitly out of scope** in 1.0.0:

1. **No hard enforcement of hexagonal boundaries**

   The engine does **not yet**:

    * generate full `domain / application / adapters / bootstrap` code structure for all profiles
    * enforce “no Spring in domain” at compile-time
    * fail generation if certain boundaries are violated

2. **No static architecture tests shipped by default**

    * No built-in ArchUnit rules are currently included in the generated projects.
    * No out-of-the-box “architecture test suite” runs in the generated service.

3. **No policy engine / rule language (yet)**

    * There is **no DSL or rule engine** that lets you say:

      > “Repositories must live under `adapters.outbound.persistence` and may only depend on `domain`.”
    * Such policies are **future work**, not part of the 1.0.0 contract.

4. **No cross-service governance**

    * The engine does **not yet** scan your GitHub org or multiple repos to enforce organization-wide rules.
    * Governance is still local to the generated project.

---

## 5. Why the scope is intentionally narrow

The goal of 1.0.0 is to:

* provide a **solid, production-ready starting point** for new services
* avoid the trap of an **overly clever, under-adopted tool**
* **separate concerns properly** so future enforcement can be layered on without rewrites

By keeping enforcement narrow and honest, Codegen Blueprint:

* builds **trust** with teams evaluating it,
* stays **compatible** with real-world CI/CD and development workflows,
* leaves room for **incremental evolution** of architectural rules.

---

## 6. Roadmap for stronger enforcement

Beyond 1.0.0, the engine is intentionally positioned to grow into:

1. **Layout-aware generation**

    * Profile variants that generate explicit hexagonal structures:

        * `domain`, `application`, `adapters`, `bootstrap`
    * Opinionated starter code that demonstrates boundaries.

2. **First-class architecture tests**

    * Auto-generated ArchUnit test suites per layout profile.
    * Optional Maven/Gradle plugin to run these tests as quality gates.

3. **Policy-driven enforcement**

    * A small configuration language (YAML / DSL) to express architectural rules, e.g.:

      ```yaml
      rules:
        - name: "Domain must not depend on Spring"
          forbid:
            - package: "org.springframework.."
          from:
            - package: "..domain.."
      ```
    * Engine and/or build plugins use these rules to fail CI if violated.

4. **Organization-level profiles**

    * Centralized, shared profiles for a whole organization:

        * naming conventions
        * module structure
        * mandatory dependencies
        * baseline observability / security / testing standards.

---

## 7. How to read this document in PRs

When reviewing changes to Codegen Blueprint:

* Use this document as the **source of truth** for:

    * what the engine **must** guarantee in 1.0.0
    * what is still “vision / roadmap”
* Any new feature that **claims enforcement** should:

    * either **update this document**, or
    * stay clearly marked as “experimental / non-enforced”.

This keeps the project coherent as it grows from:

> **“Profile-driven project generator”**
> ➜ into
> **“Executable architectural platform for new services.”**