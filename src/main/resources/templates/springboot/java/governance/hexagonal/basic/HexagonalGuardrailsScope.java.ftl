package ${projectPackageName}.architecture.archunit;

/**
 * This file is part of the generated project contract surface.
 * Canonical vocabulary for HEXAGONAL architecture guardrails.
 * <h2>What this is</h2>
 * This class is the authoritative <b>package contract</b> for the generated HEXAGONAL layout.
 * It defines the canonical names of top-level families and their canonical subpackages.
 * <h2>What this is NOT</h2>
 * <ul>
 *   <li>Not a rule set (rules live in ArchUnit tests).</li>
 *   <li>Not a matcher library (tests may build matchers using these constants).</li>
 *   <li>Not a suggestion (renaming these is a contract change, by design).</li>
 * </ul>
 * <h2>Contract intent</h2>
 * The generator emits this package schema. Guardrails validate it.
 * Any change to these names is not a refactor; it is an intentional contract change.
 */
final class HexagonalGuardrailsScope {

    /**
     * Base package of the generated application.
     * Guardrails analyze classes under this root.
     */
    static final String BASE_PACKAGE = "${projectPackageName}";

    // ---------------------------------------------------------------------------------------------
    // Top-level families (canonical, contract)
    // ---------------------------------------------------------------------------------------------

    /**
     * External-facing adapters (inbound/outbound).
     * Contains delivery mechanisms and infrastructure integrations.
     */
    static final String FAMILY_ADAPTER = "adapter";

    /**
     * Application layer: use cases + application ports.
     * Orchestrates domain; must not contain framework-specific delivery concerns.
     */
    static final String FAMILY_APPLICATION = "application";

    /**
     * Domain model and domain behavior.
     * Framework-agnostic and free of delivery/infrastructure concerns.
     */
    static final String FAMILY_DOMAIN = "domain";

    /**
     * Bootstrap/wiring.
     * Composition root and framework initialization (e.g., Spring Boot application class).
     */
    static final String FAMILY_BOOTSTRAP = "bootstrap";

    // ---------------------------------------------------------------------------------------------
    // Adapter sub-vocabulary (canonical, contract)
    // ---------------------------------------------------------------------------------------------

    /**
     * Inbound adapters (e.g., REST controllers, messaging consumers).
     * They translate from delivery model into application input ports.
     */
    static final String ADAPTER_IN = "in";

    /**
     * Outbound adapters (e.g., persistence, messaging producers, external HTTP clients).
     * They implement application output ports.
     */
    static final String ADAPTER_OUT = "out";

    /**
     * Boundary DTO namespace (typically under inbound adapters, e.g. {@code adapter.in..dto}).
     *
     * Contract note:
     * - This is a canonical vocabulary token used by strict boundary isolation rules.
     * - It does NOT mean every adapter must have DTOs; it only standardizes the name if DTOs exist.
     */
    static final String ADAPTER_DTO = "dto";

    // ---------------------------------------------------------------------------------------------
    // Application sub-vocabulary (canonical, contract)
    // ---------------------------------------------------------------------------------------------

    /**
     * Port namespace under application.
     */
    static final String APPLICATION_PORT = "port";

    /**
     * Inbound ports (use-case API) exposed by the application layer.
     * Inbound adapters depend on these.
     */
    static final String APPLICATION_PORT_IN = "in";

    /**
     * Outbound ports required by the application layer.
     * Implemented by outbound adapters.
     */
    static final String APPLICATION_PORT_OUT = "out";

    /**
     * Use-case implementations / handlers / interactors.
     * Concrete orchestration logic for inbound ports.
     *
     * Contract note: this is intentionally named "usecase" (not "service"/"handler").
     * Changing this is a contract change and must be done intentionally across generator + guardrails.
     */
    static final String APPLICATION_USECASE = "usecase";

    // ---------------------------------------------------------------------------------------------
    // Domain sub-vocabulary (canonical, contract)
    // ---------------------------------------------------------------------------------------------

    /**
     * Domain model (entities/value objects/aggregates).
     */
    static final String DOMAIN_MODEL = "model";

    /**
     * Domain port namespace (domain-level ports are allowed in this blueprint).
     */
    static final String DOMAIN_PORT = "port";

    /**
     * Domain inbound ports (domain services can depend on these abstractions if modeled).
     */
    static final String DOMAIN_PORT_IN = "in";

    /**
     * Domain outbound ports (infrastructure abstractions at domain level, if modeled).
     */
    static final String DOMAIN_PORT_OUT = "out";

    /**
     * Domain services (pure domain behavior; framework-agnostic).
     *
     * Contract note: this is a domain-level concept, not application use cases.
     */
    static final String DOMAIN_SERVICE = "service";

    // ---------------------------------------------------------------------------------------------
    // Convenience: fully-qualified family roots (for messages / expectations)
    // ---------------------------------------------------------------------------------------------

    static final String ROOT_ADAPTER = BASE_PACKAGE + "." + FAMILY_ADAPTER;
    static final String ROOT_APPLICATION = BASE_PACKAGE + "." + FAMILY_APPLICATION;
    static final String ROOT_DOMAIN = BASE_PACKAGE + "." + FAMILY_DOMAIN;
    static final String ROOT_BOOTSTRAP = BASE_PACKAGE + "." + FAMILY_BOOTSTRAP;

    private HexagonalGuardrailsScope() {}
}