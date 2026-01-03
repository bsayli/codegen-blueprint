package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.BASE_PREFIX;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.DOMAIN;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.DOMAIN_SEGMENT;
import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Strict domain purity (STANDARD).
 * Guarantees:
 * - Domain depends only on JDK types
 * - Domain depends only on other domain types
 * Notes:
 * - Works for both flat package roots and nested sub-root structures.
 * Contract note:
 * - Rule scope is the generated application base package.
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardStrictDomainPurityTest {

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
        var pkg = c.getPackageName();

        if (pkg == null || pkg.isBlank()) {
            return true;
        }
        if (pkg.startsWith("java.")) {
            return true;
        }
        return pkg.startsWith(BASE_PREFIX) && pkg.contains(DOMAIN_SEGMENT);
    }
}