package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict package cycle rules for STANDARD (layered) architecture.
 * Guarantees:
 * - No cyclic dependencies between top-level packages (under base package)
 * Notes:
 * - Empty projects are allowed (no false negatives)
 * Contract note:
 * - Rule scope is the generated base package.
 * - Slice matching uses fully qualified patterns to avoid accidental matches.
 */
@AnalyzeClasses(
        packages = StandardStrictPackageCyclesTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictPackageCyclesTest {

    static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String TOP_LEVEL_SLICE_PATTERN = BASE_PACKAGE + ".(*)..";

    @ArchTest
    static final ArchRule top_level_packages_must_be_free_of_cycles =
            slices()
                    .matching(TOP_LEVEL_SLICE_PATTERN)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);
}