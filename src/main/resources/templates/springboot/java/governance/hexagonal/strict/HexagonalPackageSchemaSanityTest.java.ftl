package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.ADAPTER;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.APPLICATION;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.DOMAIN;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.PackageMatcher;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;

/**
 * Hexagonal package schema sanity check (bounded context completeness).
 * Purpose:
 * - Prevent silent guardrails degradation when teams introduce multiple sub-roots (bounded contexts).
 * - Ensure every detected hexagonal bounded context contains the canonical families.
 * Heuristic A (context detection):
 * - A sub-root is considered a bounded context if it contains {@code application} code.
 * Guarantees:
 * - For every detected bounded context, the following canonical families MUST exist:
 *   {@code adapter}, {@code application}, {@code domain}.
 * Notes:
 * - Works for both flat roots and nested sub-root structures.
 * - Does not restrict how many bounded contexts exist.
 * - This is a schema integrity rule, not a dependency rule.
 * Contract note:
 * - If this test fails, at least one bounded context no longer conforms to the
 *   generated hexagonal architecture contract.
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalPackageSchemaSanityTest {

    @ArchTest
    static void each_hexagonal_bounded_context_must_contain_canonical_families(JavaClasses classes) {
        var contexts = detectContexts(classes);

        if (contexts.isEmpty()) {
            Assertions.fail(
                    "No hexagonal bounded context was detected under scope '" + BASE_PACKAGE + "'. "
                            + "Expected at least one context containing '" + APPLICATION + "'. "
                            + "This may indicate that the root package or canonical family names were changed."
            );
        }

        var violations = new ArrayList<String>();

        for (var context : contexts) {
            boolean hasApplication = containsInContext(classes, context, APPLICATION);
            boolean hasAdapter = containsInContext(classes, context, ADAPTER);
            boolean hasDomain = containsInContext(classes, context, DOMAIN);

            var missing = new ArrayList<String>();
            if (!hasAdapter) missing.add("adapter");
            if (!hasApplication) missing.add("application");
            if (!hasDomain) missing.add("domain");

            if (!missing.isEmpty()) {
                violations.add(context + " missing: " + String.join(", ", missing));
            }
        }

        if (violations.isEmpty()) {
            return;
        }

        Assertions.fail(
                "Hexagonal guardrails require each bounded context to contain canonical package families "
                        + "under scope '" + BASE_PACKAGE + "'. Violations:\n - "
                        + String.join("\n - ", violations)
                        + "\nIf you renamed a package family (e.g., 'adapter' -> 'adapters') or moved code into a sub-root, "
                        + "either revert to canonical family names or update the guardrails contract intentionally."
        );
    }

    private static Set<String> detectContexts(JavaClasses classes) {
        var contexts = new LinkedHashSet<String>();

        var applicationMatcher = PackageMatcher.of(APPLICATION);

        for (var c : classes) {
            var pkg = c.getPackageName();
            if (pkg == null || pkg.isBlank()) {
                continue;
            }
            if (!applicationMatcher.matches(pkg)) {
                continue;
            }
            contexts.add(extractContextPrefix(pkg));
        }

        return contexts;
    }

    private static boolean containsInContext(JavaClasses classes, String contextPrefix, String familyPattern) {
        var matcher = PackageMatcher.of(familyPattern);
        return classes.stream()
                .map(c -> c.getPackageName())
                .filter(p -> p != null && p.startsWith(contextPrefix))
                .anyMatch(matcher::matches);
    }

    private static String extractContextPrefix(String packageName) {
        if (packageName.equals(BASE_PACKAGE)) {
            return BASE_PACKAGE;
        }

        var basePrefix = BASE_PACKAGE + ".";
        if (!packageName.startsWith(basePrefix)) {
            return BASE_PACKAGE;
        }

        var remainder = packageName.substring(basePrefix.length());

        int adapterIdx = remainder.indexOf(".adapter.");
        int appIdx = remainder.indexOf(".application.");
        int domainIdx = remainder.indexOf(".domain.");

        int idx = minPositive(adapterIdx, appIdx, domainIdx);
        if (idx < 0) {
            return BASE_PACKAGE;
        }

        var subRoot = remainder.substring(0, idx);
        if (subRoot.isBlank()) {
            return BASE_PACKAGE;
        }

        return basePrefix + subRoot;
    }

    private static int minPositive(int... values) {
        int min = Integer.MAX_VALUE;
        for (int v : values) {
            if (v >= 0 && v < min) {
                min = v;
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }
}