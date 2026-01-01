package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaType;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;

/**
 * Strict REST boundary signature isolation for HEXAGONAL architecture.
 * Guarantees:
 * - REST controllers must NOT expose domain types in method signatures
 *   (return types, parameters, generics)
 * Notes:
 * - DTO domain isolation is enforced separately.
 * - This rule is structural and relies on the generated package layout.
 */
@AnalyzeClasses(
        packages = HexagonalStrictRestBoundarySignatureIsolationTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictRestBoundarySignatureIsolationTest {

    static final String BASE_PACKAGE = "${projectPackageName}";
    private static final String DOMAIN_PREFIX = BASE_PACKAGE + "..domain..";
    private static final String INBOUND_ADAPTERS = BASE_PACKAGE + "..adapter.in..";

    @ArchTest
    static final ArchRule rest_controllers_must_not_expose_domain_types_in_signatures =
            methods()
                    .that()
                    .areDeclaredInClassesThat()
                    .resideInAnyPackage(INBOUND_ADAPTERS)
                    .and()
                    .areDeclaredInClassesThat()
                    .areAnnotatedWith(RestController.class)
                    .should(notExposeDomainTypesInSignature())
                    .allowEmptyShould(true);

    private static ArchCondition<JavaMethod> notExposeDomainTypesInSignature() {
        return new ArchCondition<>("not expose domain types in REST controller method signatures") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                for (var violation : SignatureDomainLeakage.findViolations(method)) {
                    events.add(SimpleConditionEvent.violated(method, violation));
                }
            }
        };
    }

    private static final class SignatureDomainLeakage {

        private SignatureDomainLeakage() {}

        static List<String> findViolations(JavaMethod method) {
            var violations = new ArrayList<String>();

            var rawReturn = method.getRawReturnType();
            if (isDomainType(rawReturn)) {
                violations.add(message("return type leaks domain", method, rawReturn));
            }

            for (var p : method.getRawParameterTypes()) {
                if (isDomainType(p)) {
                    violations.add(message("parameter type leaks domain", method, p));
                }
            }

            var returnType = method.getReturnType();
            if (containsDomainInTypeTree(returnType)) {
                violations.add(message("generic return type leaks domain", method, returnType));
            }

            for (var pt : method.getParameterTypes()) {
                if (containsDomainInTypeTree(pt)) {
                    violations.add(message("generic parameter type leaks domain", method, pt));
                }
            }

            return List.copyOf(violations);
        }

        private static boolean containsDomainInTypeTree(JavaType type) {
            if (type == null) {
                return false;
            }
            for (var raw : type.getAllInvolvedRawTypes()) {
                if (isDomainType(raw)) {
                    return true;
                }
            }
            return false;
        }

        private static boolean isDomainType(JavaClass c) {
            if (c == null) {
                return false;
            }
            var pkg = c.getPackageName();
            return pkg != null && pkg.startsWith(DOMAIN_PREFIX);
        }

        private static String message(String reason, JavaMethod method, Object type) {
            return reason + ": " + method.getFullName() + " -> " + type;
        }
    }
}