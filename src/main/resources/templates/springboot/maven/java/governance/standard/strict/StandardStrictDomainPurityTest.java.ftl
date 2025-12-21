package ${projectPackageName}.architecture.archunit.archunit;

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
 * - Domain depends only on other domain types
 */
@AnalyzeClasses(
        packages = "${projectPackageName}",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictDomainPurityTest {

    private static final String DOMAIN_PACKAGE_PATTERN = "${projectPackageName}.domain..";
    private static final String DOMAIN_ROOT_PREFIX = "${projectPackageName}.domain.";

    @ArchTest
    static final ArchRule domain_must_depend_only_on_jdk_and_domain =
            noClasses()
                    .that()
                    .resideInAnyPackage(DOMAIN_PACKAGE_PATTERN)
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

        return pkg.startsWith(DOMAIN_ROOT_PREFIX);
    }
}