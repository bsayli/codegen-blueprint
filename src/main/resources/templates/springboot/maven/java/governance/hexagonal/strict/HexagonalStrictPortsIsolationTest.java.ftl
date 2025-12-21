package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict ports & adapters isolation.
 * Guarantees:
 * - Adapters do not depend on application implementation classes
 *   (application classes outside application.port.*)
 */
@AnalyzeClasses(
        packages = "${projectPackageName}",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictPortsIsolationTest {

    private static final String ADAPTERS = "..adapter..";

    @ArchTest
    static final ArchRule adapters_must_not_depend_on_application_implementation =
            noClasses()
                    .that().resideInAPackage(ADAPTERS)
                    .should().dependOnClassesThat(
                            describe(
                                    "reside in application but are not ports (no '.port.' segment)",
                                    HexagonalStrictPortsIsolationTest::isApplicationImplementationType
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
}