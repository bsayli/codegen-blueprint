package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict layered dependency rules for STANDARD (layered) architecture.
 * Enforces (build-time, deterministic):
 * - Controllers must NOT depend on repositories
 * - Controllers must NOT depend on domain services
 * - Services must NOT depend on controllers
 * - Repositories must NOT depend on services or controllers
 * Intent:
 * - Controllers act as HTTP / delivery boundary only
 * - Business orchestration belongs to the service layer
 * - Domain services must never be invoked directly from controllers
 * Notes:
 * - Domain *models* may still be used internally by mappers
 * - REST signature leakage (return / parameter types) is enforced separately
 * - This rule prevents bypassing the service layer while allowing clean mapping
 * This rule is evaluated at build time via generated ArchUnit tests.
 */
@AnalyzeClasses(
        packages = "${projectPackageName}",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictLayerDependencyRulesTest {

    private static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String CONTROLLER_PATTERN = BASE_PACKAGE + ".controller..";
    private static final String SERVICE_PATTERN = BASE_PACKAGE + ".service..";
    private static final String REPOSITORY_PATTERN = BASE_PACKAGE + ".repository..";

    private static final String DOMAIN_SERVICE_PATTERN = BASE_PACKAGE + ".domain..service..";

    @ArchTest
    static final ArchRule controllers_must_not_depend_on_repositories =
            noClasses()
                    .that()
                    .resideInAnyPackage(CONTROLLER_PATTERN)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(REPOSITORY_PATTERN)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllers_must_not_depend_on_domain_services =
            noClasses()
                    .that()
                    .resideInAnyPackage(CONTROLLER_PATTERN)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN_SERVICE_PATTERN)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule services_must_not_depend_on_controllers =
            noClasses()
                    .that()
                    .resideInAnyPackage(SERVICE_PATTERN)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(CONTROLLER_PATTERN)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositories_must_not_depend_on_services_or_controllers =
            noClasses()
                    .that()
                    .resideInAnyPackage(REPOSITORY_PATTERN)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(SERVICE_PATTERN, CONTROLLER_PATTERN)
                    .allowEmptyShould(true);
}