package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.FAMILY_ADAPTER;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.FAMILY_APPLICATION;
import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.FAMILY_DOMAIN;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;

/**
 * Guardrails anchor drift detector (HEXAGONAL) - BASE PACKAGE ONLY.
 * <h2>Single responsibility</h2>
 * Detect rename-based escapes at the <b>base package</b> level that can disable guardrails discovery
 * (e.g., {@code application -> apps/applicationx}) and lead to a silent green build.
 * <h2>What it does</h2>
 * <ul>
 *   <li>If any canonical anchor family exists directly under {@link HexagonalGuardrailsScope#BASE_PACKAGE},
 *       this test does nothing.</li>
 *   <li>If no canonical anchor exists but near-miss families exist directly under the base package,
 *       it fails with actionable evidence.</li>
 * </ul>
 * <h2>What it does NOT do</h2>
 * <ul>
 *   <li>It does NOT reason about bounded contexts. Context schema completeness is handled elsewhere.</li>
 *   <li>It does NOT enforce architectural dependency rules.</li>
 * </ul>
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalGuardrailsAnchorDriftTest {

    private static final List<String> CANONICAL_ANCHOR_FAMILIES = List.of(
            FAMILY_ADAPTER,
            FAMILY_APPLICATION,
            FAMILY_DOMAIN
    );

    private static final int MAX_NEAR_MISS_EXAMPLES = 3;

    @ArchTest
    static void must_fail_if_base_anchor_families_are_missing_but_near_miss_families_exist(JavaClasses classes) {
        if (hasAnyBaseAnchorEvidence(classes)) {
            return;
        }

        var nearMiss = detectBaseNearMisses(classes, CANONICAL_ANCHOR_FAMILIES);

        // No base-level canonical evidence AND no base-level near-miss evidence:
        // This may be a multi-context project,
        // or simply a minimal project where base-level families are not used.
        // This test stays silent in that case.
        if (nearMiss.isEmpty()) {
            return;
        }

        Assertions.fail(buildBaseAnchorDriftMessage(nearMiss));
    }

    private static boolean hasAnyBaseAnchorEvidence(JavaClasses classes) {
        for (var c : classes) {
            var pkg = c.getPackageName();
            if (pkg == null || pkg.isBlank()) {
                continue;
            }
            if (!pkg.startsWith(BASE_PACKAGE + ".")) {
                continue;
            }

            var first = firstSegmentAfterContext(pkg, BASE_PACKAGE);
            if (first == null) {
                continue;
            }

            if (CANONICAL_ANCHOR_FAMILIES.contains(first)) {
                return true;
            }
        }
        return false;
    }

    private static Map<String, List<String>> detectBaseNearMisses(JavaClasses classes, List<String> families) {
        var out = new LinkedHashMap<String, List<String>>();

        for (var family : families) {
            var examples = new LinkedHashSet<String>();

            for (var c : classes) {
                var pkg = c.getPackageName();
                if (pkg == null || pkg.isBlank()) {
                    continue;
                }
                if (!pkg.startsWith(BASE_PACKAGE + ".")) {
                    continue;
                }

                var first = firstSegmentAfterContext(pkg, BASE_PACKAGE);
                if (first == null) {
                    continue;
                }

                if (isNearMissFamily(first, family) && !first.equals(family)) {
                    examples.add(first + " (e.g. " + c.getFullName() + ")");
                    if (examples.size() >= MAX_NEAR_MISS_EXAMPLES) {
                        break;
                    }
                }
            }

            if (!examples.isEmpty()) {
                out.put(family, List.copyOf(examples));
            }
        }

        return out;
    }

    private static String buildBaseAnchorDriftMessage(Map<String, List<String>> nearMiss) {
        var sb = new StringBuilder()
                .append("HEXAGONAL guardrails base-anchor drift suspected under base scope '")
                .append(BASE_PACKAGE)
                .append("'.\n\n")
                .append("No canonical anchor family was detected directly under the base package (")
                .append(String.join(", ", CANONICAL_ANCHOR_FAMILIES))
                .append("), but near-miss package families were found. ")
                .append("This usually indicates a rename-based escape (e.g., application -> apps/applicationx) ")
                .append("that can disable base-level guardrails discovery and lead to a silent green build.\n\n")
                .append("Near-miss evidence:\n");

        for (var e : nearMiss.entrySet()) {
            sb.append(" - expected family: ").append(e.getKey()).append("\n");
            for (var ex : e.getValue()) {
                sb.append("     - ").append(ex).append("\n");
            }
        }

        sb.append("\nRemediation:\n")
                .append(" - Keep canonical family names as defined by the generated contract.\n")
                .append(" - If renaming is intentional, update the contract + guardrails together.\n");

        return sb.toString();
    }

    private static boolean isNearMissFamily(String firstSegment, String canonicalFamily) {
        if (firstSegment == null || firstSegment.isBlank()) {
            return false;
        }

        // plural drift: application -> applications, domain -> domains, adapter -> adapters
        if (firstSegment.equals(canonicalFamily + "s")) {
            return true;
        }

        // common adapter pluralization typo: adapters/adaptors, adapt*
        if (canonicalFamily.equals(FAMILY_ADAPTER) && firstSegment.startsWith("adapt")) {
            return true;
        }

        // "suffix escape" patterns that keep the canonical token but add extra letters:
        // applicationx, domainx, adapterx, application1, domain2, etc.
        // We intentionally do NOT match "domain-model" here, because that's a different (and often intentional) naming scheme.
        if (firstSegment.startsWith(canonicalFamily)) {
            String tail = firstSegment.substring(canonicalFamily.length());
            // tail must be alphanumeric only, and non-empty
            if (!tail.isEmpty() && tail.chars().allMatch(Character::isLetterOrDigit)) {
                return true;
            }
        }

        return false;
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
}