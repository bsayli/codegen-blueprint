package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.ADAPTER_IN;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.ADAPTER_OUT;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BASE_PACKAGE;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict adapter direction rules (HEXAGONAL).
 * Guarantees:
 * - Inbound adapters do not depend on outbound adapters
 * - Outbound adapters do not depend on inbound adapters
 * Notes:
 * - Works for both flat package roots and nested sub-root structures
 * Contract note:
 * - Rule scope is the generated application base package
 * - Package matchers are fully qualified to avoid accidental matches
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictAdapterDirectionRulesTest {

    @ArchTest
    static final ArchRule inbound_adapters_must_not_depend_on_outbound_adapters =
            noClasses()
                    .that()
                    .resideInAnyPackage(ADAPTER_IN)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(ADAPTER_OUT)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule outbound_adapters_must_not_depend_on_inbound_adapters =
            noClasses()
                    .that()
                    .resideInAnyPackage(ADAPTER_OUT)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(ADAPTER_IN)
                    .allowEmptyShould(true);
}