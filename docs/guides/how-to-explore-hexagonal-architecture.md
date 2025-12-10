# 🚀 Codegen Blueprint — Hexagonal Architecture Deep Dive

**Executable Architecture in Action — A Production‑Grade Reference**

This deep‑dive explains **exactly how Hexagonal Architecture (Ports & Adapters)** is enforced in Codegen Blueprint — not as documentation, but **as behavior**.

Architecture decisions are **compiled into the generator itself**:

* Domain stays 🔒 framework‑free
* Technology swaps 🔁 without core changes
* Best practices 🚧 enforced automatically
* Generated services 🧱 inherit structure by design

> **Architecture is not a guideline — it executes.**

---

## 🧭 Why Hexagonal Here?

Most project templates generate: **folders**.
Blueprint generates: **architectural intent**.

Hexagonal was chosen because it delivers:

| Principle                   | Value Delivered                  |
| --------------------------- | -------------------------------- |
| Strict dependency direction | Pure, independent domain model   |
| Ports define contract       | Tech swap without refactor       |
| Adapter isolation           | Framework choice does not leak   |
| Test‑first boundaries       | Faster evolution with confidence |

> The output already **protects the future architecture** of your service.

---

## 🧱 Layered Execution Flow

Strict inward dependency:

```
adapter (delivery + tech)
        ↓
application (use cases, orchestration)
        ↓
domain (business rules only)
```

Runtime wiring is delivered via `bootstrap` (Spring only at the edges).

📌 No Spring inside `domain`
📌 No FreeMarker inside `domain` or `application`
📌 No file system assumptions inside business logic

<p align="center"><em>See also: Architecture Overview diagram</em></p>

---

## 🔌 Ports & Adapters — Where the Power Lives

Ports define all allowed interactions.
Adapters implement them — nothing more.

### Domain → Outbound Ports

| Port                  | Purpose                                          |
| --------------------- | ------------------------------------------------ |
| `ProjectRootPort`     | Prepare target output structure                  |
| `ProjectWriterPort`   | Persist generated resources                      |
| `ProjectArchiverPort` | Package delivery output (ZIP, future OCI images) |

➡ Domain never touches IO.

---

### Application → Artifact Generation Ports

Each generated output has a **dedicated port**:

| Port                           | Generated Output                         |
| ------------------------------ | ---------------------------------------- |
| `BuildConfigurationPort`       | `pom.xml`                                |
| `BuildToolFilesPort`           | Maven wrapper + tooling                  |
| `SourceLayoutPort`             | Package + directory conventions          |
| `MainSourceEntrypointPort`     | Main application bootstrap               |
| `TestSourceEntrypointPort`     | Test conventions                         |
| `ApplicationConfigurationPort` | `application.yml`                        |
| `IgnoreRulesPort`              | `.gitignore`                             |
| `ProjectDocumentationPort`     | README inside generated project          |
| `SampleCodePort`               | Optional greeting service + REST adapter |

Execution engine:

| Component                  | Responsibility                              |
| -------------------------- | ------------------------------------------- |
| `ProjectArtifactsSelector` | Selects stack profile                       |
| `ProjectArtifactsPort`     | Executes ports in exact architectural order |

> Nothing is generated accidentally — every artifact is **intentional**.

---

## 🧩 Profiles — The Architecture Contract

Profiles externalize **what** is generated and **in which order**.

Example — `springboot‑maven‑java` profile pipeline:

```
build-config → build-tool-files → ignore-rules
→ source-layout → app-config
→ main-source-entrypoint → test-source-entrypoint
→ sample-code (optional)
→ project-documentation
```

Profiles are:

✔ Organizational **architecture standards**
✔ Reusable across **many products**
✔ Extensible with **zero core refactor**

> Architecture governance, expressed as configuration.

---

## 📐 Source Layout Enforcement

Two evolution paths:

### Standard

```
src/main/java/<basepkg>/
src/main/resources/
src/test/java/<basepkg>/
src/test/resources/
```

### Hexagonal (opt‑in evolution kit)

```
adapter/
  ├─ in/
  └─ out/
application/
domain/
bootstrap/
```

> Directories are treated as **domain objects** — guaranteed correctness.

---

## 📂 Resource Model — Better than "Just Files"

Every output is represented in the domain as:

| Type      | Domain Model              | Why                              |
| --------- | ------------------------- | -------------------------------- |
| Directory | `GeneratedDirectory`      | Structure is validated           |
| Text      | `GeneratedTextResource`   | Safe content models              |
| Binary    | `GeneratedBinaryResource` | Maven wrapper + future artifacts |

Supports: templates, non‑template content, binary, ZIP, future OCI.

---

## 🧪 Verified Architecture — Testing Strategy

| Test Type            | Ensures                                    |
| -------------------- | ------------------------------------------ |
| Unit                 | Rule enforcement inside domain/application |
| Integration (Spring) | Correct wiring + ordered pipelines         |
| E2E CLI tests        | Project structure validity post‑generation |
| Template tests       | Token correctness + UTF‑8 + placeholders   |

CI Quality:

* CodeQL security scanning
* Codecov coverage
* Contract test discipline
* **ArchUnit architectural guards — coming soon**

> Tests protect **architecture**, not just syntax.

---

## 🎯 What You Learn from This Repo

| Skill                    | How This Repo Teaches It            |
| ------------------------ | ----------------------------------- |
| Hexagonal mastery        | True isolation + enforced contracts |
| Maintainable scaffolding | Evolution paths from day zero       |
| Architecture automation  | "Governance as Code" patterns       |
| Multi‑stack enablement   | Add stacks without core edits       |
| Testing for architecture | Contract + pipeline validation      |

This is a **production reference architecture**, not a classroom demo.

---

## 🎮 Try It — CLI Delivery Adapter

```bash
java -jar codegen-blueprint-1.0.0.jar \
  --cli \
  springboot \
  --group-id io.github.blueprintplatform.samples \
  --artifact-id greeting-service \
  --name "Greeting Service" \
  --description "Hexagonal greeting sample powered by Blueprint" \
  --package-name io.github.blueprintplatform.samples.greeting \
  --layout hexagonal \
  --sample-code basic \
  --dependency web \
  --dependency data_jpa
```

```bash
cd greeting-service
./mvnw spring-boot:run
```

➡ Fully working REST sample included
GET `/api/v1/sample/greetings/default`

---

## 🔍 Architecture Execution Path (Mental Model)

```
CLI input
 ↓
ProjectBlueprint
 ↓
ProjectArtifactsSelector (selects profile)
 ↓
ProjectArtifactsPort (executes ordered ports)
 ↓
ProjectWriterPort (physical output)
```

> Architecture → compiled → executed.

---

## ⭐ Final Thoughts

**Executable Architecture** means:

* Architecture cannot drift accidentally
* Domain is always protected
* Tech can evolve independently
* Standards are repeatable across the organization

For teams who believe:

> "Architecture isn't a diagram — it's a behavior."

🚀 Happy generating with Codegen Blueprint! ✨
