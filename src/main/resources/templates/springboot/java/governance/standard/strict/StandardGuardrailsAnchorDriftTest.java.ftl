package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.BASE_PACKAGE;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.FAMILY_CONTROLLER;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.FAMILY_DOMAIN;
import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.FAMILY_SERVICE;

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
 * Guardrails anchor drift detector (STANDARD) - BASE PACKAGE ONLY.
 * <h2>Single responsibility</h2>
 * Detect rename-based escapes at the <b>base package</b> level that can disable guardrails discovery
 * (e.g., {@code controller -> controllers/controllerx}) and lead to a silent green build.
 * <h2>Anchor definition</h2>
 * For STANDARD, the meaningful anchors are the REQUIRED canonical families:
 * {@code controller}, {@code service}, {@code domain}.
 * <h2>What it does</h2>
 * <ul>
 *   <li>If any canonical required family exists directly under {@link StandardGuardrailsScope#BASE_PACKAGE},
 *       this test does nothing.</li>
 *   <li>If no canonical required family exists but near-miss families exist directly under the base package,
 *       it fails with actionable evidence.</li>
 * </ul>
 * <h2>What it does NOT do</h2>
 * <ul>
 *   <li>It does NOT reason about bounded contexts. Context schema completeness is handled elsewhere.</li>
 *   <li>It does NOT enforce layered dependency rules.</li>
 * </ul>
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardGuardrailsAnchorDriftTest {

    private static final List<String> REQUIRED_CANONICAL_FAMILIES = List.of(
            FAMILY_CONTROLLER,
            FAMILY_SERVICE,
            FAMILY_DOMAIN
    );

    private static final int MAX_NEAR_MISS_EXAMPLES = 3;

    @ArchTest
    static void must_fail_if_required_base_anchor_families_are_missing_but_near_miss_families_exist(JavaClasses classes) {
        if (hasAnyRequiredBaseFamilyEvidence(classes)) {
            return;
        }

        var nearMiss = detectBaseNearMisses(classes, REQUIRED_CANONICAL_FAMILIES);

        // No base-level canonical evidence AND no base-level near-miss evidence:
        // This may be a multi-context project,
        // or simply a minimal project where base-level families are not used.
        // This test stays silent in that case.
        if (nearMiss.isEmpty()) {
            return;
        }

        Assertions.fail(buildBaseAnchorDriftMessage(nearMiss));
    }

    private static boolean hasAnyRequiredBaseFamilyEvidence(JavaClasses classes) {
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

            if (REQUIRED_CANONICAL_FAMILIES.contains(first)) {
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
                .append("STANDARD guardrails base-anchor drift suspected under base scope '")
                .append(BASE_PACKAGE)
                .append("'.\n\n")
                .append("No REQUIRED canonical family evidence was detected directly under the base package (")
                .append(String.join(", ", REQUIRED_CANONICAL_FAMILIES))
                .append("), but near-miss package families were found. ")
                .append("This usually indicates a rename-based escape (e.g., controller -> controllers/controllerx) ")
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

    /**
     * Narrow near-miss heuristic for BASE-LEVEL escape detection.
     * We intentionally avoid broad prefix matching to reduce false positives.
     */
    private static boolean isNearMissFamily(String firstSegment, String canonicalFamily) {
        if (firstSegment == null || firstSegment.isBlank()) {
            return false;
        }

        // plural drift: controller -> controllers, service -> services, domain -> domains
        if (firstSegment.equals(canonicalFamily + "s")) {
            return true;
        }

        // suffix escape patterns: controllerx, service1, domain2, etc. (alphanumeric tail only)
        if (firstSegment.startsWith(canonicalFamily)) {
            String tail = firstSegment.substring(canonicalFamily.length());
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