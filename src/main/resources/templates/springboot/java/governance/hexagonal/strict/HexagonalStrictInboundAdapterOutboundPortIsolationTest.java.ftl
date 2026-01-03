package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.ADAPTER_IN;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.DOMAIN_PORT_OUT;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict inbound adapter → domain outbound port isolation (HEXAGONAL).
 * Guarantees:
 * - Inbound adapters must not depend on domain outbound ports
 * Notes:
 * - This rule enforces correct direction of control flow
 * - Works for both flat package roots and nested sub-root structures
 * Contract note:
 * - Rule scope is the generated application base package
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictInboundAdapterOutboundPortIsolationTest {

    @ArchTest
    static final ArchRule inbound_adapters_must_not_depend_on_domain_outbound_ports =
            noClasses()
                    .that()
                    .resideInAnyPackage(ADAPTER_IN)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN_PORT_OUT)
                    .allowEmptyShould(true);
}