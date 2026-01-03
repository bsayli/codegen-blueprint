package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.DOMAIN_SERVICE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.FAMILY_CONTROLLER;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.FAMILY_DOMAIN;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.FAMILY_REPOSITORY;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.FAMILY_SERVICE;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.PackageMatcher;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import java.util.ArrayList;
import java.util.TreeSet;
import org.junit.jupiter.api.Assertions;

/**
 * Basic architecture rules for STANDARD (layered) layout.
 * <h2>Guarantees</h2>
 * <ul>
 *   <li>Controllers must not depend on repositories.</li>
 *   <li>Repositories must not depend on controllers.</li>
 *   <li>Controllers must not depend on domain services (service layer only).</li>
 *   <li>Domain must not depend on controller/service/repository layers.</li>
 *   <li>Each detected bounded context root is free of cyclic dependencies across its top-level packages.</li>
 * </ul>
 * <h2>Bounded Context Detection (deterministic)</h2>
 * <ul>
 *   <li>A bounded context root is defined as the package prefix before {@code ".controller."}.</li>
 *   <li>Contexts are detected by the presence of at least one class under a {@code controller} package.</li>
 *   <li>Multiple bounded contexts (nested or sibling) are supported.</li>
 * </ul>
 * <h2>Notes</h2>
 * <ul>
 *   <li>These rules are structural and rely on the generated package schema.</li>
 *   <li>Config isolation is intentionally not enforced at BASIC level to keep adoption friction low.</li>
 *   <li>Works for both flat package roots and nested sub-root structures.</li>
 * </ul>
 * <h2>Contract note</h2>
 * <ul>
 *   <li>Canonical family names are defined centrally in {@link StandardGuardrailsScope}.</li>
 *   <li>This rule intentionally derives patterns locally to keep behavior explicit and rule-scoped.</li>
 * </ul>
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardBasicArchitectureRulesTest {

    private static final String CONTROLLER_TOKEN = familyToken(FAMILY_CONTROLLER);
    private static final PackageMatcher CONTROLLER_MATCHER = PackageMatcher.of(familyPattern(FAMILY_CONTROLLER));

    @ArchTest
    static final ArchRule controllers_must_not_depend_on_repositories =
            noClasses()
                    .that()
                    .resideInAnyPackage(familyPattern(FAMILY_CONTROLLER))
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(familyPattern(FAMILY_REPOSITORY))
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositories_must_not_depend_on_controllers =
            noClasses()
                    .that()
                    .resideInAnyPackage(familyPattern(FAMILY_REPOSITORY))
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(familyPattern(FAMILY_CONTROLLER))
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllers_must_not_depend_on_domain_services =
            noClasses()
                    .that()
                    .resideInAnyPackage(familyPattern(FAMILY_CONTROLLER))
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(domainServicePattern())
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule domain_must_not_depend_on_controller_service_or_repository_layers =
            noClasses()
                    .that()
                    .resideInAnyPackage(familyPattern(FAMILY_DOMAIN))
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(
                            familyPattern(FAMILY_CONTROLLER),
                            familyPattern(FAMILY_SERVICE),
                            familyPattern(FAMILY_REPOSITORY)
                    )
                    .allowEmptyShould(true);

    @ArchTest
    static void each_bounded_context_root_must_be_free_of_cycles(JavaClasses classes) {
        var contexts = detectContextRoots(classes);

        if (contexts.isEmpty()) {
            Assertions.fail(
                    "No STANDARD bounded context was detected under scope '" + BASE_PACKAGE + "'. "
                            + "A bounded context is inferred by presence of a '" + FAMILY_CONTROLLER + "' package."
            );
        }

        var violations = new ArrayList<String>();

        for (var contextRoot : contexts) {
            var rule =
                    slices()
                            .matching(contextRoot + ".(*)..")
                            .should()
                            .beFreeOfCycles()
                            .allowEmptyShould(true);

            var evaluation = rule.evaluate(classes);

            if (evaluation.hasViolation()) {
                violations.add(contextRoot);
            }
        }

        if (!violations.isEmpty()) {
            Assertions.fail(
                    "STANDARD cyclic dependency violation(s) detected under base scope '" + BASE_PACKAGE + "'.\n\n"
                            + "The following bounded context root(s) have cycles across their top-level packages:\n - "
                            + String.join("\n - ", violations)
                            + "\n\nRemediation:\n"
                            + " - Break the cycle(s) by refactoring dependencies and/or package boundaries.\n"
                            + " - Keep STANDARD family boundaries explicit ("
                            + FAMILY_CONTROLLER + "/" + FAMILY_SERVICE + "/" + FAMILY_DOMAIN + ") to preserve guardrails integrity."
            );
        }
    }

    private static TreeSet<String> detectContextRoots(JavaClasses classes) {
        var contexts = new TreeSet<String>();

        for (var c : classes) {
            var pkg = c.getPackageName();
            if (pkg == null || pkg.isBlank()) {
                continue;
            }
            if (!CONTROLLER_MATCHER.matches(pkg)) {
                continue;
            }
            contexts.add(contextRootForControllerPackage(pkg));
        }

        return contexts;
    }

    private static String contextRootForControllerPackage(String packageName) {
        int idx = packageName.indexOf(CONTROLLER_TOKEN);
        if (idx < 0) {
            return BASE_PACKAGE;
        }

        var root = packageName.substring(0, idx);
        return root.isBlank() ? BASE_PACKAGE : root;
    }

    private static String domainServicePattern() {
        return BASE_PACKAGE + ".." + FAMILY_DOMAIN + "." + DOMAIN_SERVICE + "..";
    }

    private static String familyToken(String family) {
        return "." + family + ".";
    }

    private static String familyPattern(String family) {
        return BASE_PACKAGE + ".." + family + "..";
    }
}