package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.ADAPTER_IN_DTO;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.DOMAIN;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict inbound DTO isolation (HEXAGONAL).
 * Guarantees:
 * - Inbound adapter DTOs must not depend on domain types
 * Notes:
 * - DTOs represent boundary data structures and remain framework-facing only
 * - Works for both flat package roots and nested sub-root structures
 * Contract note:
 * - Rule scope is the generated application base package
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictInboundDtoDomainIsolationTest {

    @ArchTest
    static final ArchRule inbound_adapter_dtos_must_not_depend_on_domain =
            noClasses()
                    .that()
                    .resideInAnyPackage(ADAPTER_IN_DTO)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN)
                    .allowEmptyShould(true);
}