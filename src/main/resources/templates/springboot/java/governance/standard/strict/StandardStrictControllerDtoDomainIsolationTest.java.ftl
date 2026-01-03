package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.CONTROLLER_DTO;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.DOMAIN;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict controller DTO isolation (STANDARD).
 * Guarantees:
 * - Controller DTOs must not depend on domain types.
 * Notes:
 * - Works for both flat package roots and nested sub-root structures.
 * Contract note:
 * - Rule scope is the generated application base package.
 * - Package matchers are fully qualified to avoid accidental matches.
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictControllerDtoDomainIsolationTest {

    @ArchTest
    static final ArchRule controller_dtos_must_not_depend_on_domain =
            noClasses()
                    .that()
                    .resideInAnyPackage(CONTROLLER_DTO)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN)
                    .allowEmptyShould(true);
}