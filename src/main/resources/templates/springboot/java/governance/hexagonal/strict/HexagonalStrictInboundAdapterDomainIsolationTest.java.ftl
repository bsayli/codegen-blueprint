package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict inbound adapter -> domain isolation (HEXAGONAL).
 * Guarantees:
 * - Inbound adapters must not depend on domain services
 * Notes:
 * - Domain models may still be used by inbound adapters if desired.
 * - REST method signature leakage is enforced separately.
 * Contract note:
 * - Rule scope is the generated base package.
 */
@AnalyzeClasses(
        packages = HexagonalStrictInboundAdapterDomainIsolationTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictInboundAdapterDomainIsolationTest {

    static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String INBOUND_ADAPTERS = BASE_PACKAGE + "..adapter.in..";
    private static final String DOMAIN_SERVICES = BASE_PACKAGE + "..domain.service..";

    @ArchTest
    static final ArchRule inbound_adapters_must_not_depend_on_domain_services =
            noClasses()
                    .that()
                    .resideInAnyPackage(INBOUND_ADAPTERS)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN_SERVICES)
                    .allowEmptyShould(true);
}