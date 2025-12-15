package ${projectPackageName}.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
* Strict domain purity for HEXAGONAL layout.
* Domain must depend only on:
* - JDK types
* - other domain types
*/
@AnalyzeClasses(
packages = "${projectPackageName}",
importOptions = ImportOption.DoNotIncludeTests.class
)
class DomainPurityTest {

private static final String DOMAIN = "..domain..";

@ArchTest
static final ArchRule domain_must_depend_only_on_jdk_and_domain =
noClasses()
.that().resideInAPackage(DOMAIN)
.should()
.dependOnClassesThat(
describe(
"reside outside domain and are not JDK types",
DomainPurityTest::isNotAllowedForDomain));

private static boolean isNotAllowedForDomain(JavaClass c) {
return !isAllowedForDomain(c);
}

private static boolean isAllowedForDomain(JavaClass c) {
String pkg = c.getPackageName();
if (pkg == null || pkg.isBlank()) {
return true;
}
if (pkg.startsWith("java.") || pkg.startsWith("javax.")) {
return true;
}
return pkg.contains(".domain.");
}
}