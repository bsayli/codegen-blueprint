## 👀 How to Explore This Project (Hexagonal Architecture Guide)

If you're here to understand **how Hexagonal Architecture (Ports & Adapters)** is applied in a **real, fully-tested, production‑grade Java project**, this section will guide your exploration.

The repository demonstrates how to build a **framework‑agnostic, testable, cleanly layered architecture** — while still generating real output (project scaffolding).

---

### 🧱 Core Architectural Structure

**Layered by strict responsibilities:**

* **`domain`** → Pure business rules: aggregate, value objects, naming policies, dependency rules
* **`application`** → Executes generation pipelines using defined ports
* **`adapter`** → Technology-specific implementations (CLI, REST, FreeMarker, filesystem, Maven, docs)
* **`bootstrap`** → Spring wiring: profile → adapters → renderer binding

Each package enforces **one direction** of dependency: toward the domain.

---

### 🔌 Ports & Adapters (Decoupled Delivery)

Generation behavior is defined by **ports**:

* `ArtifactPort` → Generates a single artifact
* `ProjectArtifactsPort` → Orchestrates ordered artifact pipeline

Concrete behavior is in **outbound adapters**, mapped via keys:

* `BUILD_CONFIG` → Maven POM generator
* `IGNORE_RULES` → .gitignore generator
* `APP_CONFIG` → application.yml generator
* `MAIN_SOURCE_ENTRY_POINT` → Main class scaffolder
* `TEST_SOURCE_ENTRY_POINT` → Test scaffolder
* `PROJECT_DOCUMENTATION` → README generator

Adding support for a new tech stack (e.g., Gradle) requires **only new adapters + templates** — no core changes.

---

### 🧩 Profile‑Driven Architecture

Profiles define the generation rules:

* Template namespacing
* Which artifacts are generated
* The exact processing order

These stay externalized in configuration (`application.yml`), keeping the engine **evolution‑friendly**.

---

### 🧲 Inbound Adapters (CLI currently implemented)

Inbound adapters trigger **use cases** from external channels.

Currently implemented:

* **CLI Adapter (active)** → Powered by Picocli + Spring Context

📌 Usage example:

```bash
java -jar codegen-blueprint.jar \
  --spring.profiles.active=cli \
  springboot \
  --group-id com.example \
  --artifact-id demo-app \
  --name "Demo App" \
  --package-name com.example.demo \
  --dependency WEB \
  --dependency DATA_JPA
```

The CLI maps arguments → domain commands → artifact pipeline → project zip output.

Planned inbound adapter:

* REST API (HTTP-driven generation service)

---

### 🧪 Testing Strategy (CI‑Ready)

* **Unit tests** → Domain rules + adapter behavior
* **Integration tests** → Full Spring Context + end‑to‑end artifact generation
* JaCoCo + Codecov coverage reporting
* CodeQL for static security scanning

Every major component is validated **without mocking core logic**.

---

### 🎯 Why This Repo Matters

This project serves as a concrete reference for:

| Learning Goal                | How this repo helps                               |
| ---------------------------- | ------------------------------------------------- |
| Apply Hexagonal Architecture | Clean separation of domain, application, adapters |
| Reduce framework coupling    | Domain has zero Spring dependencies               |
| Improve maintainability      | Technology swaps don’t cause refactors            |
| Ensure high testability      | Full integration test pipeline + CI validation    |
| Build generation engines     | Profile‑driven artifact pipeline architecture     |

If you're evaluating engineering skills or searching for a scalable architecture pattern — this repository is designed to showcase **the real thing**, not a toy example.

---

📌 *Tip:* Begin with `ProjectBlueprint` (domain), then follow how it flows into `ProjectArtifactsPort`, down to each registered adapter in the `springboot-maven-java` profile.
