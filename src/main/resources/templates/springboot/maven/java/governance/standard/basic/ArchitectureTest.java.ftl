package ${projectPackageName}.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
* Basic architecture rules for STANDARD layout.
* Enforces a simple layered structure:
* - controller
* - service
* - repository
* - domain
* This is a lightweight governance level.
*/
@AnalyzeClasses(
packages = "${projectPackageName}",
importOptions = ImportOption.DoNotIncludeTests.class
)
class ArchitectureTest {

private static final String CONTROLLER = "..controller..";
private static final String SERVICE = "..service..";
private static final String REPOSITORY = "..repository..";
private static final String DOMAIN = "..domain..";

@ArchTest
static final ArchRule controllers_should_not_access_repositories_directly =
noClasses()
.that().resideInAPackage(CONTROLLER)
.should().dependOnClassesThat()
.resideInAPackage(REPOSITORY);

@ArchTest
static final ArchRule domain_should_not_depend_on_other_layers =
noClasses()
.that().resideInAPackage(DOMAIN)
.should().dependOnClassesThat()
.resideInAnyPackage(CONTROLLER, SERVICE, REPOSITORY);
}