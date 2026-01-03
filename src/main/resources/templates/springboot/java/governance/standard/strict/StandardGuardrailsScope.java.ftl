package ${projectPackageName}.architecture.archunit;

/**
 * This file is part of the generated project contract surface.
 *
 * Canonical vocabulary for STANDARD (layered) architecture guardrails.
 * <h2>What this is</h2>
 * This class is the authoritative <b>package contract</b> for the generated STANDARD layout.
 * It defines canonical names of top-level families and their canonical subpackages.
 * <h2>What this is NOT</h2>
 * <ul>
 *   <li>Not a rule set (rules live in ArchUnit tests).</li>
 *   <li>Not a matcher library (tests may build patterns using these constants).</li>
 *   <li>Not a suggestion (renaming these is a contract change, by design).</li>
 * </ul>
 * <h2>Contract intent</h2>
 * The generator emits this package schema. Guardrails validate it.
 * Any change to these names is not a refactor; it is an intentional contract change.
 */
final class StandardGuardrailsScope {

    /**
     * Base package of the generated application.
     * Guardrails analyze classes under this root.
     */
    static final String BASE_PACKAGE = "${projectPackageName}";

    // ---------------------------------------------------------------------------------------------
    // Top-level families (canonical, contract)
    // ---------------------------------------------------------------------------------------------

    /**
     * Delivery layer (entrypoints).
     * Example: REST controllers.
     */
    static final String FAMILY_CONTROLLER = "controller";

    /**
     * Application services (use-case orchestration in layered style).
     */
    static final String FAMILY_SERVICE = "service";

    /**
     * Persistence layer (repositories, data access).
     */
    static final String FAMILY_REPOSITORY = "repository";

    /**
     * Domain layer (domain model + domain services).
     * Framework-agnostic as a rule of thumb, within the layered model.
     */
    static final String FAMILY_DOMAIN = "domain";

    /**
     * Configuration / wiring (framework configuration, bean wiring, etc.).
     */
    static final String FAMILY_CONFIG = "config";

    // ---------------------------------------------------------------------------------------------
    // Controller sub-vocabulary (canonical, contract)
    // ---------------------------------------------------------------------------------------------

    /**
     * DTO namespace under controller.
     * Boundary objects for delivery layer (request/response models).
     */
    static final String CONTROLLER_DTO = "dto";

    // ---------------------------------------------------------------------------------------------
    // Domain sub-vocabulary (canonical, contract)
    // ---------------------------------------------------------------------------------------------

    /**
     * Domain model namespace (entities/value objects/aggregates).
     */
    static final String DOMAIN_MODEL = "model";

    /**
     * Domain services namespace (domain behavior that does not naturally belong to a single entity).
     */
    static final String DOMAIN_SERVICE = "service";

    // ---------------------------------------------------------------------------------------------
    // Convenience: fully-qualified family roots (for messages / expectations)
    // ---------------------------------------------------------------------------------------------

    static final String ROOT_CONTROLLER = BASE_PACKAGE + "." + FAMILY_CONTROLLER;
    static final String ROOT_SERVICE = BASE_PACKAGE + "." + FAMILY_SERVICE;
    static final String ROOT_REPOSITORY = BASE_PACKAGE + "." + FAMILY_REPOSITORY;
    static final String ROOT_DOMAIN = BASE_PACKAGE + "." + FAMILY_DOMAIN;
    static final String ROOT_CONFIG = BASE_PACKAGE + "." + FAMILY_CONFIG;

    private StandardGuardrailsScope() {}
}