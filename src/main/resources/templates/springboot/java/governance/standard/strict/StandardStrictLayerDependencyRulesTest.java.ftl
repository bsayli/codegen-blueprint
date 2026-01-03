package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.CONTROLLER;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.DOMAIN_SERVICE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.REPOSITORY;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.SERVICE;
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
 * - Works for both flat package roots and nested sub-root structures
 * Contract note:
 * - Rule scope is the generated application base package
 * - Package matchers are fully qualified to avoid accidental matches
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictLayerDependencyRulesTest {

    @ArchTest
    static final ArchRule controllers_must_not_depend_on_repositories =
            noClasses()
                    .that()
                    .resideInAnyPackage(CONTROLLER)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(REPOSITORY)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllers_must_not_depend_on_domain_services =
            noClasses()
                    .that()
                    .resideInAnyPackage(CONTROLLER)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN_SERVICE)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule services_must_not_depend_on_controllers =
            noClasses()
                    .that()
                    .resideInAnyPackage(SERVICE)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(CONTROLLER)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositories_must_not_depend_on_services_or_controllers =
            noClasses()
                    .that()
                    .resideInAnyPackage(REPOSITORY)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(SERVICE, CONTROLLER)
                    .allowEmptyShould(true);
}