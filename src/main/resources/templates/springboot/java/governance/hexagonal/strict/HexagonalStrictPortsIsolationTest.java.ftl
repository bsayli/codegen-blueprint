package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.ADAPTER;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.APPLICATION_SEGMENT;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.PORT_SEGMENT;
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
 * - Adapters must not depend on application implementation classes
 *   (use cases, services, or any non-port application types)
 * Notes:
 * - This rule enforces the core hexagonal dependency direction
 * - Structural by design and independent of package root shape
 * - Works for both flat package roots and nested sub-root structures
 * Contract note:
 * - Rule scope is the generated application base package
 * - Package matching is fully qualified to avoid accidental matches
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictPortsIsolationTest {

    private static final DescribedPredicate<JavaClass> APPLICATION_IMPLEMENTATION_TYPES =
            new DescribedPredicate<>("reside in application but are not ports (outside '.port.')") {
                @Override
                public boolean test(JavaClass c) {
                    var pkg = c.getPackageName();
                    if (pkg == null || pkg.isBlank()) {
                        return false;
                    }
                    return pkg.startsWith(BASE_PACKAGE + ".")
                            && pkg.contains(APPLICATION_SEGMENT)
                            && !pkg.contains(PORT_SEGMENT);
                }
            };

    @ArchTest
    static final ArchRule adapters_must_not_depend_on_application_implementation =
            noClasses()
                    .that()
                    .resideInAnyPackage(ADAPTER)
                    .should()
                    .dependOnClassesThat(APPLICATION_IMPLEMENTATION_TYPES)
                    .allowEmptyShould(true);
}