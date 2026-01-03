package ${projectPackageName}.architecture.archunit;

import static ${projectPackageName}.architecture.archunit.HexagonalGuardrailsScope.BASE_PACKAGE;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.junit.jupiter.api.Assertions;

/**
 * Guardrails scope sanity check (HEXAGONAL).
 * Purpose:
 * - Fail fast if the guardrails scope is empty (no classes found under the configured base package).
 * Why this exists:
 * - A common post-generation change is refactoring the root package name.
 * - If the root package changes but {@link HexagonalGuardrailsScope#BASE_PACKAGE} is not updated,
 *   ArchUnit may import zero classes and rules can appear "green" while guardrails are effectively disabled.
 * Contract note:
 * - This is not an architectural dependency rule; it's an integrity check for the guardrails contract.
 * - If this test fails, update {@link HexagonalGuardrailsScope#BASE_PACKAGE} to match the new root package
 *   (or re-generate the project with the intended package name).
 */
@AnalyzeClasses(
        packages = BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalGuardrailsScopeSanityTest {

    @ArchTest
    static void guardrails_scope_must_not_be_empty(JavaClasses classes) {
        Assertions.assertFalse(
                classes.isEmpty(),
                "No classes were found under the configured base package. "
                        + "This usually means the root package was changed after generation. "
                        + "Update HexagonalGuardrailsScope.BASE_PACKAGE to match the new root package "
                        + "to avoid a silent green build with disabled architecture guardrails."
        );
    }
}