package ${projectPackageName}.architecture.archunit;

/**
 * Central scope definition for STANDARD (layered) architecture guardrails.
 * Purpose:
 * - Provide a single, authoritative package contract for all standard rules
 * - Prevent duplication of package patterns across rule classes
 * Design note:
 * - This scope defines structural boundaries, not dependency rules
 * - Patterns are intentionally broad to support both flat roots and sub-roots
 * - Segment constants are provided for string-based package analysis
 */
final class StandardGuardrailsScope {

    static final String BASE_PACKAGE = "${projectPackageName}";

    // ArchUnit package patterns (for resideInAnyPackage)
    static final String CONTROLLER = BASE_PACKAGE + "..controller..";
    static final String CONTROLLER_DTO = BASE_PACKAGE + "..controller..dto..";

    static final String SERVICE = BASE_PACKAGE + "..service..";
    static final String REPOSITORY = BASE_PACKAGE + "..repository..";

    static final String DOMAIN = BASE_PACKAGE + "..domain..";
    static final String DOMAIN_MODEL = BASE_PACKAGE + "..domain.model..";
    static final String DOMAIN_SERVICE = BASE_PACKAGE + "..domain.service..";

    static final String CONFIG = BASE_PACKAGE + "..config..";

    // ArchUnit slice patterns (for slices().matching)
    static final String TOP_LEVEL_SLICE = BASE_PACKAGE + ".(*)..";

    static final String CONTROLLER_SLICE = BASE_PACKAGE + "..controller.(*)..";
    static final String SERVICE_SLICE = BASE_PACKAGE + "..service.(*)..";
    static final String REPOSITORY_SLICE = BASE_PACKAGE + "..repository.(*)..";
    static final String DOMAIN_SLICE = BASE_PACKAGE + "..domain.(*)..";
    static final String CONFIG_SLICE = BASE_PACKAGE + "..config.(*)..";

    // String-based package segments (for JavaClass#getPackageName analysis)
    static final String BASE_PREFIX = BASE_PACKAGE + ".";
    static final String DOMAIN_SEGMENT = ".domain.";

    private StandardGuardrailsScope() {}
}