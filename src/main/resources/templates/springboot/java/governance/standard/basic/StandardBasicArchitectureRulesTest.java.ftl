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
 * - Domain must not depend on controller/service/repository layers
 * - No cycles across top-level packages
 */
@AnalyzeClasses(
        packages = "${projectPackageName}",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class StandardBasicArchitectureRulesTest {

    private static final String BASE_PACKAGE = "${projectPackageName}";

    private static final String CONTROLLER = "..controller..";
    private static final String SERVICE = "..service..";
    private static final String REPOSITORY = "..repository..";
    private static final String DOMAIN = "..domain..";

    private static final String DOMAIN_SERVICE = BASE_PACKAGE + ".domain..service..";

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
                    .resideInAnyPackage(DOMAIN_SERVICE)
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule domain_must_not_depend_on_controller_or_service_or_repository_layers =
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
                    .matching("${projectPackageName}.(*)..")
                    .should()
                    .beFreeOfCycles()
                    .allowEmptyShould(true);
}