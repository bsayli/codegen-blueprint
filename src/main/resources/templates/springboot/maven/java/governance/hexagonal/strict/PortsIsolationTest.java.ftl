package ${projectPackageName}.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
* Strict ports & adapters isolation.
* Adapters must not depend on application implementation classes
* (application classes outside application.port.*).
*/
@AnalyzeClasses(
packages = "${projectPackageName}",
importOptions = ImportOption.DoNotIncludeTests.class
)
class PortsIsolationTest {

private static final String ADAPTER = "..adapter..";

@ArchTest
static final ArchRule adapters_must_not_depend_on_application_implementation =
noClasses()
.that().resideInAPackage(ADAPTER)
.should()
.dependOnClassesThat(
describe(
"reside in application outside application.port",
PortsIsolationTest::isApplicationImplementationType));

private static boolean isApplicationImplementationType(JavaClass c) {
String pkg = c.getPackageName();
if (pkg == null || pkg.isBlank()) {
return false;
}
return pkg.contains(".application.") && !pkg.contains(".application.port.");
}
}