package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.CONFIG_SLICE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.CONTROLLER_SLICE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.DOMAIN_SLICE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.REPOSITORY_SLICE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.SERVICE_SLICE;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict package cycle rules (STANDARD).
 * Guarantees:
 * - No cyclic dependencies inside each layered package family
 *   (controller/service/repository/domain/config).
 * Notes:
 * - Works for both flat package roots and nested sub-root structures.
 * - Empty projects are allowed (no false negatives).
 * Contract note:
 * - Rule scope is the generated application base package.
 * - Slice matchers are fully qualified to ensure deterministic behavior.
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictPackageCyclesTest {

    @ArchTest
    static final ArchRule controller_packages_must_be_free_of_cycles =
            slices()
                    .matching(CONTROLLER_SLICE)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule service_packages_must_be_free_of_cycles =
            slices()
                    .matching(SERVICE_SLICE)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repository_packages_must_be_free_of_cycles =
            slices()
                    .matching(REPOSITORY_SLICE)
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
    static final ArchRule config_packages_must_be_free_of_cycles =
            slices()
                    .matching(CONFIG_SLICE)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);
}