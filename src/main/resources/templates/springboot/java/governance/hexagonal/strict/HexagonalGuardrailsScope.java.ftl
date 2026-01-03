package ${projectPackageName}.architecture.archunit;

/**
 * Central scope definition for HEXAGONAL architecture guardrails.
 * Purpose:
 * - Provide a single, authoritative package contract for all hexagonal rules
 * - Prevent duplication of package patterns and slice patterns across rule classes
 * Design note:
 * - This scope defines structural boundaries, not dependency rules
 * - Patterns are intentionally broad to support both flat roots and sub-roots
 * - Derived constants are grouped explicitly to keep intent clear
 */
final class HexagonalGuardrailsScope {

    static final String BASE_PACKAGE = "${projectPackageName}";

    // ArchUnit package patterns (for resideInAnyPackage)
    static final String APPLICATION = BASE_PACKAGE + "..application..";
    static final String APPLICATION_PORT = BASE_PACKAGE + "..application.port..";
    static final String APPLICATION_USECASE = BASE_PACKAGE + "..application.usecase..";

    static final String ADAPTER = BASE_PACKAGE + "..adapter..";
    static final String ADAPTER_IN = BASE_PACKAGE + "..adapter.in..";
    static final String ADAPTER_OUT = BASE_PACKAGE + "..adapter.out..";
    static final String ADAPTER_IN_DTO = BASE_PACKAGE + "..adapter.in..dto..";

    static final String DOMAIN = BASE_PACKAGE + "..domain..";
    static final String DOMAIN_MODEL = BASE_PACKAGE + "..domain.model..";
    static final String DOMAIN_SERVICE = BASE_PACKAGE + "..domain.service..";
    static final String DOMAIN_PORT = BASE_PACKAGE + "..domain.port..";
    static final String DOMAIN_PORT_IN = BASE_PACKAGE + "..domain.port.in..";
    static final String DOMAIN_PORT_OUT = BASE_PACKAGE + "..domain.port.out..";

    static final String BOOTSTRAP = BASE_PACKAGE + "..bootstrap..";

    // ArchUnit slice patterns (for slices().matching)
    static final String ADAPTER_SLICE = ADAPTER + "(*)..";
    static final String APPLICATION_SLICE = APPLICATION + "(*)..";
    static final String DOMAIN_SLICE = DOMAIN + "(*)..";
    static final String BOOTSTRAP_SLICE = BOOTSTRAP + "(*)..";

    static final String ADAPTER_IN_SLICE = ADAPTER_IN + "(*)..";
    static final String ADAPTER_OUT_SLICE = ADAPTER_OUT + "(*)..";

    // String-based analysis helpers (for JavaClass#getPackageName)
    static final String BASE_PREFIX = BASE_PACKAGE + ".";
    static final String APPLICATION_SEGMENT = ".application.";
    static final String PORT_SEGMENT = ".port.";
    static final String DOMAIN_SEGMENT = ".domain.";

    private HexagonalGuardrailsScope() {}
}