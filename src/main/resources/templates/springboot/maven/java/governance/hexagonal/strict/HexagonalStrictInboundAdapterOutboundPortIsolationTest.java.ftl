package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict inbound adapter -> domain outbound port isolation (HEXAGONAL).
 * Guarantees:
 * - Inbound adapters must not depend on domain outbound ports.
 */
@AnalyzeClasses(
        packages = "${projectPackageName}",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictInboundAdapterOutboundPortIsolationTest {

    private static final String INBOUND_ADAPTERS = "..adapter..in..";
    private static final String DOMAIN_OUTBOUND_PORTS = "..domain..port..out..";

    @ArchTest
    static final ArchRule inbound_adapters_must_not_depend_on_domain_outbound_ports =
            noClasses()
                    .that()
                    .resideInAPackage(INBOUND_ADAPTERS)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN_OUTBOUND_PORTS)
                    .allowEmptyShould(true);
}