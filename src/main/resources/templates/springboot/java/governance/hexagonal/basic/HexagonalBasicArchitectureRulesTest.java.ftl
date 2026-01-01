package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

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
 * Contract note:
 * - Rule scope is the generated base package: ${projectPackageName}
 * - Package matchers use fully qualified patterns to avoid accidental matches.
 */
@AnalyzeClasses(
        packages = HexagonalBasicArchitectureRulesTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalBasicArchitectureRulesTest {

    static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String APPLICATION = BASE_PACKAGE + "..application..";
    private static final String ADAPTERS = BASE_PACKAGE + "..adapter..";
    private static final String BOOTSTRAP = BASE_PACKAGE + "..bootstrap..";

    @ArchTest
    static final ArchRule application_must_not_depend_on_adapters =
            noClasses()
                    .that()
                    .resideInAnyPackage(APPLICATION)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(ADAPTERS)
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