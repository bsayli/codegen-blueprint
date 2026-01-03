package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.CONTROLLER;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.PackageMatcher;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.Assertions;

/**
 * Standard (layered) package schema sanity check (bounded-context heuristic).
 * Purpose:
 * - Prevent silent architecture drift when teams rename or relocate core package families after generation.
 * - Validate schema completeness per bounded context, without requiring every possible family everywhere.
 * Heuristic (context detection):
 * - Any sub-root that contains a {@code controller} package is treated as a bounded context.
 * Guarantees (per detected context):
 * - If a context has {@code controller}, it MUST also have {@code service} and {@code domain}.
 * Notes:
 * - Works with both flat roots and nested sub-root structures.
 * - {@code repository} is intentionally NOT required (persistence is optional).
 * Contract note:
 * - This is a schema integrity rule, not a dependency rule.
 * - If this test fails, at least one detected bounded context no longer conforms to the generated STANDARD contract.
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardPackageSchemaSanityTest {

    @ArchTest
    static void bounded_contexts_with_controller_must_also_have_service_and_domain(JavaClasses classes) {
        var contexts = detectControllerContexts(classes);

        if (contexts.isEmpty()) {
            return;
        }

        var violations = new TreeMap<String, String>();
        for (var entry : contexts.entrySet()) {
            var contextRoot = entry.getKey();

            boolean hasService = containsAnyClassInPackagePattern(classes, contextRoot + "..service..");
            boolean hasDomain = containsAnyClassInPackagePattern(classes, contextRoot + "..domain..");

            if (hasService && hasDomain) {
                continue;
            }

            var missing = new StringBuilder();
            if (!hasService) {
                missing.append("service ");
            }
            if (!hasDomain) {
                missing.append("domain ");
            }

            violations.put(
                    contextRoot,
                    "missing: " + missing.toString().trim()
                            + " (expected under '" + contextRoot + "')"
            );
        }

        if (violations.isEmpty()) {
            return;
        }

        var message = new StringBuilder()
                .append("Standard guardrails schema violation under base scope '").append(BASE_PACKAGE).append("'. ")
                .append("Bounded contexts are inferred by presence of 'controller'. ")
                .append("For each detected context, 'service' and 'domain' must also exist.\n\n")
                .append("Violations:\n");

        for (var v : violations.entrySet()) {
            message.append(" - ").append(v.getKey()).append(": ").append(v.getValue()).append("\n");
        }

        message.append("\nRemediation: revert to canonical family names (controller/service/domain) within each context, ")
                .append("or restructure intentionally and update the guardrails contract accordingly.");

        Assertions.fail(message.toString());
    }

    private static Map<String, Boolean> detectControllerContexts(JavaClasses classes) {
        var matcher = PackageMatcher.of(CONTROLLER);
        var contexts = new LinkedHashMap<String, Boolean>();

        for (var c : classes) {
            var pkg = c.getPackageName();
            if (pkg == null || pkg.isBlank()) {
                continue;
            }
            if (!matcher.matches(pkg)) {
                continue;
            }

            var contextRoot = contextRootForControllerPackage(pkg);
            if (contextRoot != null && !contextRoot.isBlank()) {
                contexts.putIfAbsent(contextRoot, Boolean.TRUE);
            }
        }

        return contexts;
    }

    private static String contextRootForControllerPackage(String packageName) {
        var token = ".controller.";
        int idx = packageName.indexOf(token);
        if (idx >= 0) {
            return packageName.substring(0, idx);
        }

        token = ".controller";
        idx = packageName.lastIndexOf(token);
        if (idx >= 0 && idx + token.length() == packageName.length()) {
            return packageName.substring(0, idx);
        }

        return null;
    }

    private static boolean containsAnyClassInPackagePattern(JavaClasses classes, String archUnitPackagePattern) {
        var matcher = PackageMatcher.of(archUnitPackagePattern);
        return classes.stream().anyMatch(c -> matcher.matches(c.getPackageName()));
    }
}