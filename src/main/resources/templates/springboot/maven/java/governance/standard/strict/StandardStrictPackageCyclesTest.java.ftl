package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict package cycle rules for STANDARD (layered) architecture.
 * Guarantees:
 * - No cyclic dependencies between top-level packages
 * Notes:
 * - Applies to controller/service/repository/domain/config style layouts
 * - Empty projects are allowed (no false negatives)
 */
@AnalyzeClasses(
        packages = "${projectPackageName}",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictPackageCyclesTest {

    @ArchTest
    static final ArchRule top_level_packages_must_be_free_of_cycles =
            slices()
                    .matching("${projectPackageName}.(*)..")
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);
}