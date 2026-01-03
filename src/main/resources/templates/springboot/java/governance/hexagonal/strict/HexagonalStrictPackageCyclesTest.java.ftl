package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.ADAPTER_IN_SLICE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.ADAPTER_OUT_SLICE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.ADAPTER_SLICE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.APPLICATION_SLICE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BOOTSTRAP_SLICE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.DOMAIN_SLICE;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict package cycle rules (HEXAGONAL).
 * Guarantees:
 * - No cyclic dependencies inside adapter packages
 * - No cyclic dependencies inside application packages
 * - No cyclic dependencies inside domain packages
 * - No cyclic dependencies inside bootstrap packages
 * - No cyclic dependencies inside architecture packages
 * - No cyclic dependencies inside adapter.in subpackages
 * - No cyclic dependencies inside adapter.out subpackages
 * Notes:
 * - Rules are applied per hexagonal layer to prevent local cycles
 *   without enforcing unnecessary global coupling constraints
 * - Works for both flat package roots and nested sub-root structures
 * Contract note:
 * - Rule scope is the generated application base package
 * - Slice matching is fully qualified to ensure deterministic behavior
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictPackageCyclesTest {

    @ArchTest
    static final ArchRule adapter_packages_must_be_free_of_cycles =
            slices()
                    .matching(ADAPTER_SLICE)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule application_packages_must_be_free_of_cycles =
            slices()
                    .matching(APPLICATION_SLICE)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule domain_packages_must_be_free_of_cycles =
            slices()
                    .matching(DOMAIN_SLICE)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule bootstrap_packages_must_be_free_of_cycles =
            slices()
                    .matching(BOOTSTRAP_SLICE)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);


    @ArchTest
    static final ArchRule adapter_in_packages_must_be_free_of_cycles =
            slices()
                    .matching(ADAPTER_IN_SLICE)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule adapter_out_packages_must_be_free_of_cycles =
            slices()
                    .matching(ADAPTER_OUT_SLICE)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);
}