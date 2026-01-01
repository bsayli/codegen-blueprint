package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict domain purity.
 * Guarantees:
 * - Domain depends only on JDK types
 * - Domain depends only on other domain types
 * Contract note:
 * - Rule scope is the generated base package
 * - Domain is treated as a pure, framework-free core
 */
@AnalyzeClasses(
        packages = HexagonalStrictDomainPurityTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictDomainPurityTest {

    static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String DOMAIN = BASE_PACKAGE + "..domain..";

    private static final String BASE_PREFIX = BASE_PACKAGE + ".";
    private static final String DOMAIN_SEGMENT = ".domain.";

    @ArchTest
    static final ArchRule domain_must_depend_only_on_jdk_and_domain =
            noClasses()
                    .that()
                    .resideInAnyPackage(DOMAIN)
                    .should()
                    .dependOnClassesThat(
                            describe(
                                    "reside outside domain and are not JDK types",
                                    c -> !isAllowedForDomain(c)
                            )
                    )
                    .allowEmptyShould(true);

    private static boolean isAllowedForDomain(JavaClass c) {
        String pkg = c.getPackageName();

        if (pkg == null || pkg.isBlank()) {
            return true;
        }

        if (pkg.startsWith("java.")) {
            return true;
        }

        return pkg.startsWith(BASE_PREFIX) && pkg.contains(DOMAIN_SEGMENT);
    }
}