package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict package cycle rules.
 * Guarantees:
 * - No cycles across top-level slices
 * - No cycles inside adapter subpackages
 * Notes:
 * - For "empty" generated projects, these slices may match no classes.
 *   In that case, allowEmptyShould(true) prevents false-negative failures.
 */
@AnalyzeClasses(
        packages = "${projectPackageName}",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictPackageCyclesTest {

    @ArchTest
    static final ArchRule layers_must_be_free_of_cycles =
            slices()
                    .matching("${projectPackageName}.(*)..")
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule adapter_subpackages_must_be_free_of_cycles =
            slices()
                    .matching("${projectPackageName}.adapter.(*)..")
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);
}