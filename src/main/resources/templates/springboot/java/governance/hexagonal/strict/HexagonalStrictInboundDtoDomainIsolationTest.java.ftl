package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict inbound DTO isolation (HEXAGONAL).
 * Guarantees:
 * - Inbound adapter DTOs must not depend on domain types.
 * Contract note:
 * - DTOs represent boundary data structures and must remain framework-facing only.
 * - Rule scope is the generated base package.
 */
@AnalyzeClasses(
        packages = HexagonalStrictInboundDtoDomainIsolationTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictInboundDtoDomainIsolationTest {

    static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String INBOUND_ADAPTER_DTOS = BASE_PACKAGE + "..adapter.in..dto..";
    private static final String DOMAIN = BASE_PACKAGE + "..domain..";

    @ArchTest
    static final ArchRule inbound_adapter_dtos_must_not_depend_on_domain =
            noClasses()
                    .that()
                    .resideInAnyPackage(INBOUND_ADAPTER_DTOS)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN)
                    .allowEmptyShould(true);
}