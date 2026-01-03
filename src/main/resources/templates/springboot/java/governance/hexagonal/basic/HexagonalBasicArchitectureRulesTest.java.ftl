package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.ADAPTER;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.APPLICATION;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BOOTSTRAP;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Basic Hexagonal guardrails.
 * Guarantees:
 * - Application does not depend on adapters
 * - Bootstrap is a leaf (nobody depends on it)
 * - No cycles across top-level packages
 * Notes:
 * - Works for both flat package roots and nested sub-root structures
 * - Architectural rules remain valid even if domain sub-packages are introduced
 * Contract note:
 * - Rule scope is the generated application base package
 * - Package matchers are fully qualified to avoid accidental matches
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalBasicArchitectureRulesTest {

    @ArchTest
    static final ArchRule application_must_not_depend_on_adapters =
            noClasses()
                    .that()
                    .resideInAnyPackage(APPLICATION)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(ADAPTER)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule bootstrap_must_not_be_depended_on =
            noClasses()
                    .that()
                    .resideOutsideOfPackage(BOOTSTRAP)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(BOOTSTRAP)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule top_level_packages_must_be_free_of_cycles =
            slices()
                    .matching(BASE_PACKAGE + ".(*)..")
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);
}