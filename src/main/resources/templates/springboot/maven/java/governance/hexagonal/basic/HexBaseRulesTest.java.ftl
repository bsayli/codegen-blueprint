package ${projectPackageName}.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
* Basic governance for HEXAGONAL layout.
* Enforces the fundamental dependency direction:
* application must not depend on adapter
* bootstrap must not be depended on
*/
@AnalyzeClasses(
packages = "${projectPackageName}",
importOptions = ImportOption.DoNotIncludeTests.class
)
class HexBaseRulesTest {

private static final String APPLICATION = "..application..";
private static final String ADAPTER = "..adapter..";
private static final String BOOTSTRAP = "..bootstrap..";

@ArchTest
static final ArchRule application_must_not_depend_on_adapters =
noClasses()
.that().resideInAPackage(APPLICATION)
.should().dependOnClassesThat()
.resideInAnyPackage(ADAPTER);

@ArchTest
static final ArchRule bootstrap_must_not_be_depended_on =
noClasses()
.that().resideOutsideOfPackage(BOOTSTRAP)
.should().dependOnClassesThat()
.resideInAPackage(BOOTSTRAP);
}