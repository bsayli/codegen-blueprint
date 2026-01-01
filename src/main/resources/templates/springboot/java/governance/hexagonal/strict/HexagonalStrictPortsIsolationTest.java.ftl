package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict ports & adapters isolation (HEXAGONAL).
 * Guarantees:
 * - Adapters may depend on application ports
 * - Adapters must not depend on application implementation classes (usecases, services, etc.)
 * Notes:
 * - This rule is structural and relies on the generated package layout.
 */
@AnalyzeClasses(
        packages = HexagonalStrictPortsIsolationTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictPortsIsolationTest {

    static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String ADAPTERS = BASE_PACKAGE + "..adapter..";

    private static final String BASE_PREFIX = BASE_PACKAGE + ".";
    private static final String APPLICATION_SEGMENT = ".application.";
    private static final String PORT_SEGMENT = ".port.";

    private static final DescribedPredicate<JavaClass> APPLICATION_IMPLEMENTATION_TYPES =
            new DescribedPredicate<>("reside in application but are not ports (outside '.port.')") {
                @Override
                public boolean test(JavaClass c) {
                    var pkg = c.getPackageName();
                    if (pkg == null || pkg.isBlank()) {
                        return false;
                    }
                    return pkg.startsWith(BASE_PREFIX)
                            && pkg.contains(APPLICATION_SEGMENT)
                            && !pkg.contains(PORT_SEGMENT);
                }
            };

    @ArchTest
    static final ArchRule adapters_must_not_depend_on_application_implementation =
            noClasses()
                    .that()
                    .resideInAnyPackage(ADAPTERS)
                    .should()
                    .dependOnClassesThat(APPLICATION_IMPLEMENTATION_TYPES)
                    .allowEmptyShould(true);
}