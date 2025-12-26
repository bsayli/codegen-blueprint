package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Basic Hexagonal governance.
 * Guarantees:
 * - Application does not depend on adapters
 * - Bootstrap is a leaf (nobody depends on it)
 * - Adapters depend only on application ports (no app implementation leakage)
 * - No cycles across top-level packages
 */
@AnalyzeClasses(
        packages = "${projectPackageName}",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalBasicArchitectureRulesTest {

    private static final String APPLICATION = "..application..";
    private static final String ADAPTERS = "..adapter..";
    private static final String BOOTSTRAP = "..bootstrap..";

    @ArchTest
    static final ArchRule application_must_not_depend_on_adapters =
            noClasses()
                    .that()
                    .resideInAPackage(APPLICATION)
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
                    .resideInAPackage(BOOTSTRAP)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule adapters_must_not_depend_on_application_implementation =
            noClasses()
                    .that()
                    .resideInAnyPackage(ADAPTERS)
                    .should()
                    .dependOnClassesThat(
                            describe(
                                    "reside in application but are not ports (no '.port.' segment)",
                                    HexagonalBasicArchitectureRulesTest::isApplicationImplementationType
                            )
                    )
                    .allowEmptyShould(true);

    private static boolean isApplicationImplementationType(JavaClass c) {
        String pkg = c.getPackageName();
        if (pkg == null || pkg.isBlank()) {
            return false;
        }
        return pkg.contains(".application.") && !pkg.contains(".port.");
    }

    @ArchTest
    static final ArchRule top_level_packages_must_be_free_of_cycles =
            slices()
                    .matching("${projectPackageName}.(*)..")
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);
}