package ${projectPackageName}.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Basic architecture rules for STANDARD (layered) layout.
 * Guarantees:
 * - Controllers must not depend on repositories
 * - Repositories must not depend on controllers
 * - Controllers must not depend on domain services (service layer only)
 * - Domain must not depend on controller/service/repository layers
 * - No cycles across top-level packages
 * Notes:
 * - These rules are structural and rely on the generated package layout.
 * - Wiring/config isolation is intentionally not enforced at BASIC level to keep adoption friction low.
 */
@AnalyzeClasses(
        packages = StandardBasicArchitectureRulesTest.BASE_PACKAGE,
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardBasicArchitectureRulesTest {

    static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String CONTROLLER = BASE_PACKAGE + "..controller..";
    private static final String SERVICE = BASE_PACKAGE + "..service..";
    private static final String REPOSITORY = BASE_PACKAGE + "..repository..";
    private static final String DOMAIN = BASE_PACKAGE + "..domain..";

    private static final String DOMAIN_SERVICES = BASE_PACKAGE + "..domain.service..";

    private static final String TOP_LEVEL_SLICE_PATTERN = BASE_PACKAGE + ".(*)..";

    @ArchTest
    static final ArchRule controllers_must_not_depend_on_repositories =
            noClasses()
                    .that()
                    .resideInAnyPackage(CONTROLLER)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(REPOSITORY)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositories_must_not_depend_on_controllers =
            noClasses()
                    .that()
                    .resideInAnyPackage(REPOSITORY)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(CONTROLLER)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllers_must_not_depend_on_domain_services =
            noClasses()
                    .that()
                    .resideInAnyPackage(CONTROLLER)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(DOMAIN_SERVICES)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule domain_must_not_depend_on_controller_service_or_repository_layers =
            noClasses()
                    .that()
                    .resideInAnyPackage(DOMAIN)
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage(CONTROLLER, SERVICE, REPOSITORY)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule top_level_packages_must_be_free_of_cycles =
            slices()
                    .matching(TOP_LEVEL_SLICE_PATTERN)
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);
}