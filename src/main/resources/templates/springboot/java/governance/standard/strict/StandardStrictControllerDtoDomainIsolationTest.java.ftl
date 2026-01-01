package ${projectPackageName}.architecture.archunit;

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
 * - Uses fully qualified package patterns under the generated base package to avoid accidental matches.
 */
@AnalyzeClasses(
        packages = StandardStrictControllerDtoDomainIsolationTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictControllerDtoDomainIsolationTest {

    static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String CONTROLLER_DTOS = BASE_PACKAGE + "..controller..dto..";
    private static final String DOMAIN = BASE_PACKAGE + "..domain..";

    @ArchTest
    static final ArchRule controller_dtos_must_not_depend_on_domain =
            noClasses()
                    .that()
                    .resideInAnyPackage(CONTROLLER_DTOS)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN)
                    .allowEmptyShould(true);
}