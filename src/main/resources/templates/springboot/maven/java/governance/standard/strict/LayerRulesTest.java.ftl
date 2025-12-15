package ${projectPackageName}.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
* Strict layered architecture rules for STANDARD layout.
* Enforces dependency direction:
* controller -> service -> repository
*/
@AnalyzeClasses(
packages = "${projectPackageName}",
importOptions = ImportOption.DoNotIncludeTests.class
)
class LayerRulesTest {

private static final String CONTROLLER = "..controller..";
private static final String SERVICE = "..service..";
private static final String REPOSITORY = "..repository..";

@ArchTest
static final ArchRule controllers_should_only_depend_on_services =
noClasses()
.that().resideInAPackage(CONTROLLER)
.should().dependOnClassesThat()
.resideInAnyPackage(REPOSITORY);

@ArchTest
static final ArchRule services_should_not_depend_on_controllers =
noClasses()
.that().resideInAPackage(SERVICE)
.should().dependOnClassesThat()
.resideInAPackage(CONTROLLER);
}