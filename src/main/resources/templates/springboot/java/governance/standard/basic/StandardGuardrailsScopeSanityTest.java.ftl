package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.StandardGuardrailsScope.BASE_PACKAGE;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.junit.jupiter.api.Assertions;

/**
 * Guardrails scope sanity check (STANDARD).
 * Purpose:
 * - Fail fast if the guardrails scope is empty (no classes found under the configured base package).
 * Why this exists:
 * - After generation, teams may refactor the root package name.
 * - If the root package changes but {@link StandardGuardrailsScope#BASE_PACKAGE} is not updated,
 *   ArchUnit evaluates an empty scope and architecture guardrails silently stop working.
 * Contract note:
 * - This is NOT a layered dependency rule.
 * - This is a guardrails integrity check to prevent a false-green build.
 * Remediation:
 * - Update {@link StandardGuardrailsScope#BASE_PACKAGE} to match the new root package
 *   (or re-generate the project with the intended package name).
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardGuardrailsScopeSanityTest {

    @ArchTest
    static void guardrails_scope_must_not_be_empty(JavaClasses classes) {
        Assertions.assertFalse(
                classes.isEmpty(),
                "No classes were found under the configured base package. "
                        + "This usually means the root package was changed after generation. "
                        + "Update StandardGuardrailsScope.BASE_PACKAGE to match the new root package "
                        + "to avoid a silent green build with disabled architecture guardrails."
        );
    }
}