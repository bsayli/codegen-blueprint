package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict domain purity for STANDARD (layered) layout.
 * Guarantees:
 * - Domain depends only on JDK types
 * - Domain depends only on other domain types (any package under base package containing '.domain.')
 * Contract note:
 * - Rule scope is the generated base package.
 */
@AnalyzeClasses(
        packages = StandardStrictDomainPurityTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictDomainPurityTest {

    static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String DOMAIN = BASE_PACKAGE + "..domain..";
    private static final String BASE_PACKAGE_PREFIX = BASE_PACKAGE + ".";

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

        if (!pkg.startsWith(BASE_PACKAGE_PREFIX)) {
            return false;
        }

        return pkg.contains(".domain.");
    }
}