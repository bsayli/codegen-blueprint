package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

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
 * Enforced ONLY when:
 * - EnforcementMode = STRICT
 * - spring-boot-starter-web is present
 * Guarantees:
 * - REST controllers must NOT expose domain types in method signatures
 *   (return types, parameters, generics)
 * Explicitly allowed:
 * - Mapper / assembler classes may depend on domain
 * - Internal adapter helpers may touch domain
 * Rationale:
 * - REST controllers represent the public HTTP boundary
 * - Domain types must never leak through that boundary
 * - Mapping is allowed internally but must terminate before the controller API
 */
@AnalyzeClasses(
        packages = "${projectPackageName}",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalStrictRestBoundarySignatureIsolationTest {

    private static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String DOMAIN_PREFIX = BASE_PACKAGE + ".domain.";
    private static final String DOMAIN_PACKAGE_PATTERN = BASE_PACKAGE + ".domain..";

    private static final String INBOUND_ADAPTER_PATTERN = BASE_PACKAGE + ".adapter.in..";
    private static final String INBOUND_ADAPTER_DTO_PATTERN =
            BASE_PACKAGE + ".adapter.in..dto..";

    @ArchTest
    static final ArchRule inbound_adapter_dtos_must_not_depend_on_domain =
            noClasses()
                    .that()
                    .resideInAnyPackage(INBOUND_ADAPTER_DTO_PATTERN)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN_PACKAGE_PATTERN)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule rest_controllers_must_not_expose_domain_types_in_signatures =
            methods()
                    .that()
                    .areDeclaredInClassesThat()
                    .resideInAnyPackage(INBOUND_ADAPTER_PATTERN)
                    .and()
                    .areDeclaredInClassesThat()
                    .areAnnotatedWith(RestController.class)
                    .should(notExposeDomainTypesInSignature())
                    .allowEmptyShould(true);

    private static ArchCondition<JavaMethod> notExposeDomainTypesInSignature() {
        return new ArchCondition<>("not expose domain types in REST controller method signatures") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                for (String violation : SignatureDomainLeakage.findViolations(method)) {
                    events.add(SimpleConditionEvent.violated(method, violation));
                }
            }
        };
    }

    private static final class SignatureDomainLeakage {

        private SignatureDomainLeakage() {}

        static List<String> findViolations(JavaMethod method) {
            List<String> violations = new ArrayList<>();

            JavaClass rawReturn = method.getRawReturnType();
            if (isDomainType(rawReturn)) {
                violations.add(message(method, "return type leaks domain", rawReturn));
            }

            for (JavaClass p : method.getRawParameterTypes()) {
                if (isDomainType(p)) {
                    violations.add(message(method, "parameter type leaks domain", p));
                }
            }

            if (containsDomainInTypeTree(method.getReturnType())) {
                violations.add(
                        message(method, "generic return type leaks domain", method.getReturnType()));
            }

            for (JavaType pt : method.getParameterTypes()) {
                if (containsDomainInTypeTree(pt)) {
                    violations.add(
                            message(method, "generic parameter type leaks domain", pt));
                }
            }

            return violations;
        }

        private static boolean containsDomainInTypeTree(JavaType type) {
            if (type == null) {
                return false;
            }
            for (JavaClass raw : type.getAllInvolvedRawTypes()) {
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
            String pkg = c.getPackageName();
            return pkg != null && pkg.startsWith(DOMAIN_PREFIX);
        }

        private static String message(JavaMethod method, String reason, Object type) {
            return reason + ": " + method.getFullName() + " -> " + type;
        }
    }
}