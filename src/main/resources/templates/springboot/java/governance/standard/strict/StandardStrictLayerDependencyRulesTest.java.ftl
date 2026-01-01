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
 * - Domain models may still be used internally by mappers
 * - REST signature leakage is enforced separately
 * Contract note:
 * - Rule scope is the generated base package.
 * - Package matchers are fully qualified to avoid accidental matches.
 */
@AnalyzeClasses(
        packages = StandardStrictLayerDependencyRulesTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictLayerDependencyRulesTest {

    static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String CONTROLLERS = BASE_PACKAGE + "..controller..";
    private static final String SERVICES = BASE_PACKAGE + "..service..";
    private static final String REPOSITORIES = BASE_PACKAGE + "..repository..";
    private static final String DOMAIN_SERVICES = BASE_PACKAGE + "..domain.service..";

    @ArchTest
    static final ArchRule controllers_must_not_depend_on_repositories =
            noClasses()
                    .that()
                    .resideInAnyPackage(CONTROLLERS)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(REPOSITORIES)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllers_must_not_depend_on_domain_services =
            noClasses()
                    .that()
                    .resideInAnyPackage(CONTROLLERS)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN_SERVICES)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule services_must_not_depend_on_controllers =
            noClasses()
                    .that()
                    .resideInAnyPackage(SERVICES)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(CONTROLLERS)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositories_must_not_depend_on_services_or_controllers =
            noClasses()
                    .that()
                    .resideInAnyPackage(REPOSITORIES)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(SERVICES, CONTROLLERS)
                    .allowEmptyShould(true);
}