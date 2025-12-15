package ${projectPackageName}.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
* Dependency purity rules for STANDARD layout.
* Domain must stay independent from infrastructure and web layers.
*/
@AnalyzeClasses(
packages = "${projectPackageName}",
importOptions = ImportOption.DoNotIncludeTests.class
)
class DependencyRulesTest {

private static final String DOMAIN = "..domain..";

@ArchTest
static final ArchRule domain_should_depend_only_on_domain_and_jdk =
noClasses()
.that().resideInAPackage(DOMAIN)
.should().dependOnClassesThat(
c -> !isAllowed(c)
);

private static boolean isAllowed(JavaClass c) {
String pkg = c.getPackageName();
if (pkg == null || pkg.isBlank()) {
return true;
}
return pkg.startsWith("java.")
|| pkg.startsWith("javax.")
|| pkg.contains(".domain.");
}
}