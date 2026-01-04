package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.FAMILY_ADAPTER;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.FAMILY_APPLICATION;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.FAMILY_BOOTSTRAP;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.FAMILY_DOMAIN;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.Assertions;

/**
 * Hexagonal package schema sanity check (bounded context completeness).
 * <h2>Single responsibility</h2>
 * For every detected HEXAGONAL bounded context, require the canonical families to exist:
 * {@code adapter}, {@code application}, {@code domain}.
 * <h2>Bounded Context Detection</h2>
 * A bounded context is inferred by finding any canonical family token as a <b>package segment</b>
 * under the base package. The context root is the package prefix preceding the first detected family segment.
 * <h2>What this does NOT do</h2>
 * <ul>
 *   <li>It does NOT attempt rename/near-miss drift detection. That belongs to the anchor drift test.</li>
 *   <li>It does NOT enforce dependency rules (those live in dedicated ArchUnit rule tests).</li>
 * </ul>
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalPackageSchemaSanityTest {

    private static final List<String> CANONICAL_FAMILIES = List.of(
            FAMILY_ADAPTER,
            FAMILY_APPLICATION,
            FAMILY_DOMAIN,
            FAMILY_BOOTSTRAP
    );

    private static final List<String> REQUIRED_FAMILIES_PER_CONTEXT = List.of(
            FAMILY_ADAPTER,
            FAMILY_APPLICATION,
            FAMILY_DOMAIN
    );

    @ArchTest
    static void each_hexagonal_bounded_context_must_contain_canonical_families(JavaClasses classes) {
        var contexts = detectContexts(classes);

        // No detected bounded contexts => nothing to validate here.
        // Base-level rename drift is handled by HexagonalGuardrailsAnchorDriftTest.
        if (contexts.isEmpty()) {
            return;
        }

        var allContexts = contexts.stream().collect(java.util.stream.Collectors.toMap(c -> c, c -> Boolean.TRUE));
        var violations = new ArrayList<ContextViolation>();

        for (var contextRoot : contexts) {
            boolean hasAdapter = hasCanonicalFamilyRootInExactContext(classes, contextRoot, FAMILY_ADAPTER, allContexts);
            boolean hasApplication = hasCanonicalFamilyRootInExactContext(classes, contextRoot, FAMILY_APPLICATION, allContexts);
            boolean hasDomain = hasCanonicalFamilyRootInExactContext(classes, contextRoot, FAMILY_DOMAIN, allContexts);

            if (hasAdapter && hasApplication && hasDomain) {
                continue;
            }

            var evidence = bestEvidenceUnderContext(classes, contextRoot, allContexts);

            var missing = new ArrayList<String>();
            if (!hasAdapter) missing.add(FAMILY_ADAPTER);
            if (!hasApplication) missing.add(FAMILY_APPLICATION);
            if (!hasDomain) missing.add(FAMILY_DOMAIN);

            violations.add(new ContextViolation(
                    contextRoot,
                    evidence,
                    hasAdapter,
                    hasApplication,
                    hasDomain,
                    List.copyOf(missing)
            ));
        }

        if (violations.isEmpty()) {
            return;
        }

        Assertions.fail(buildViolationMessage(violations));
    }

    private static Set<String> detectContexts(JavaClasses classes) {
        var contexts = new TreeSet<String>();

        for (var c : classes) {
            var pkg = c.getPackageName();
            if (pkg == null || pkg.isBlank()) {
                continue;
            }
            if (!pkg.equals(BASE_PACKAGE) && !pkg.startsWith(BASE_PACKAGE + ".")) {
                continue;
            }

            var segments = splitSegments(pkg);

            // find first occurrence of any canonical family segment, after BASE_PACKAGE segments
            int baseSegCount = splitSegments(BASE_PACKAGE).length;
            int familyIndex = indexOfFirstCanonicalFamilySegment(segments, baseSegCount);

            if (familyIndex < 0) {
                continue;
            }

            var contextRoot = joinSegments(segments, 0, familyIndex);
            if (contextRoot == null || contextRoot.isBlank()) {
                contextRoot = BASE_PACKAGE;
            }

            // Defensive: only accept roots under BASE_PACKAGE.
            if (!contextRoot.equals(BASE_PACKAGE) && !contextRoot.startsWith(BASE_PACKAGE + ".")) {
                continue;
            }

            contexts.add(contextRoot);
        }

        return contexts;
    }

    private static int indexOfFirstCanonicalFamilySegment(String[] segments, int startAt) {
        for (int i = Math.max(0, startAt); i < segments.length; i++) {
            if (CANONICAL_FAMILIES.contains(segments[i])) {
                return i;
            }
        }
        return -1;
    }

    private static boolean hasCanonicalFamilyRootInExactContext(
            JavaClasses classes,
            String contextRoot,
            String family,
            Map<String, Boolean> allContexts
    ) {
        for (var c : classes) {
            var pkg = c.getPackageName();
            if (pkg == null || pkg.isBlank()) {
                continue;
            }
            if (!isUnderContext(pkg, contextRoot)) {
                continue;
            }
            if (belongsToMoreSpecificContext(pkg, contextRoot, allContexts)) {
                continue;
            }

            // Canonical family must be the FIRST segment after the context root.
            var first = firstSegmentAfterContext(pkg, contextRoot);
            if (family.equals(first)) {
                return true;
            }
        }
        return false;
    }

    private static boolean belongsToMoreSpecificContext(
            String packageName,
            String currentContextRoot,
            Map<String, Boolean> allContexts
    ) {
        for (var other : allContexts.keySet()) {
            if (other.equals(currentContextRoot)) {
                continue;
            }
            if (isNestedContext(other, currentContextRoot) && isUnderContext(packageName, other)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNestedContext(String maybeNested, String maybeParent) {
        return maybeNested.startsWith(maybeParent + ".");
    }

    private static boolean isUnderContext(String packageName, String contextRoot) {
        return packageName.equals(contextRoot) || packageName.startsWith(contextRoot + ".");
    }

    private static String bestEvidenceUnderContext(JavaClasses classes, String contextRoot, Map<String, Boolean> allContexts) {
        String best = null;
        int bestDepth = Integer.MAX_VALUE;

        for (var c : classes) {
            var pkg = c.getPackageName();
            if (pkg == null || pkg.isBlank()) {
                continue;
            }
            if (!isUnderContext(pkg, contextRoot)) {
                continue;
            }
            if (belongsToMoreSpecificContext(pkg, contextRoot, allContexts)) {
                continue;
            }

            // Evidence: the shallowest package under this context (stable and deterministic)
            int depth = segmentCount(pkg);
            if (depth < bestDepth) {
                best = pkg;
                bestDepth = depth;
            } else if (depth == bestDepth && best != null && pkg.compareTo(best) < 0) {
                best = pkg;
            } else if (depth == bestDepth && best == null) {
                best = pkg;
            }
        }

        return best;
    }

    private static String buildViolationMessage(List<ContextViolation> violations) {
        var sb = new StringBuilder()
                .append("HEXAGONAL package schema integrity failure under base scope '")
                .append(BASE_PACKAGE)
                .append("'.\n\n")
                .append("Bounded contexts are inferred by evidence of canonical family segments: ")
                .append(CANONICAL_FAMILIES)
                .append(". For each detected context root, the required canonical families must exist: ")
                .append(REQUIRED_FAMILIES_PER_CONTEXT)
                .append(".\n\n")
                .append("Violations:\n");

        for (var v : violations) {
            sb.append(" - context: ").append(v.contextRoot()).append("\n");
            sb.append("     context evidence: ").append(v.contextEvidence() == null ? "<unknown>" : v.contextEvidence()).append("\n");

            sb.append("     present: ")
                    .append(FAMILY_ADAPTER).append(v.hasAdapter() ? " ✅" : " ❌").append(", ")
                    .append(FAMILY_APPLICATION).append(v.hasApplication() ? " ✅" : " ❌").append(", ")
                    .append(FAMILY_DOMAIN).append(v.hasDomain() ? " ✅" : " ❌")
                    .append("\n");

            sb.append("     missing: ").append(String.join(", ", v.missingFamilies())).append("\n");
        }

        sb.append("\nRemediation:\n")
                .append(" - If you introduced a bounded context root, ensure it contains ")
                .append(FAMILY_ADAPTER).append("/").append(FAMILY_APPLICATION).append("/").append(FAMILY_DOMAIN).append(" families.\n")
                .append(" - If a canonical family was renamed, revert the rename or update the guardrails contract intentionally.\n")
                .append(" - If you refactored the root package name, ensure BASE_PACKAGE matches the new root.\n");

        return sb.toString();
    }

    private static String[] splitSegments(String packageName) {
        if (packageName == null || packageName.isBlank()) {
            return new String[0];
        }
        return packageName.split("\\.");
    }

    private static String joinSegments(String[] segments, int fromInclusive, int toExclusive) {
        if (segments == null || segments.length == 0) {
            return null;
        }
        if (fromInclusive < 0 || toExclusive > segments.length || fromInclusive >= toExclusive) {
            return null;
        }
        var sb = new StringBuilder();
        for (int i = fromInclusive; i < toExclusive; i++) {
            if (i > fromInclusive) {
                sb.append('.');
            }
            sb.append(segments[i]);
        }
        return sb.toString();
    }

    private static String firstSegmentAfterContext(String packageName, String contextRoot) {
        if (packageName == null || packageName.isBlank()) {
            return null;
        }
        if (packageName.equals(contextRoot)) {
            return null;
        }
        var prefix = contextRoot + ".";
        if (!packageName.startsWith(prefix)) {
            return null;
        }

        int start = prefix.length();
        int dot = packageName.indexOf('.', start);
        if (dot < 0) {
            return packageName.substring(start);
        }
        return packageName.substring(start, dot);
    }

    private static int segmentCount(String packageName) {
        if (packageName == null || packageName.isBlank()) {
            return 0;
        }
        int segments = 1;
        for (int i = 0; i < packageName.length(); i++) {
            if (packageName.charAt(i) == '.') {
                segments++;
            }
        }
        return segments;
    }

    private record ContextViolation(
            String contextRoot,
            String contextEvidence,
            boolean hasAdapter,
            boolean hasApplication,
            boolean hasDomain,
            List<String> missingFamilies
    ) {}
}