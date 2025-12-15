apackage ${projectPackageName}.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
* Strict adapter direction rules.
* inbound adapters must not depend on outbound adapters
* outbound adapters must not depend on inbound adapters
*/
@AnalyzeClasses(
packages = "${projectPackageName}",
importOptions = ImportOption.DoNotIncludeTests.class
)
class AdapterDirectionTest {

@ArchTest
static final ArchRule inbound_adapters_must_not_depend_on_outbound_adapters =
noClasses()
.that().resideInAPackage("..adapter.in..")
.should().dependOnClassesThat()
.resideInAnyPackage("..adapter.out..");

@ArchTest
static final ArchRule outbound_adapters_must_not_depend_on_inbound_adapters =
noClasses()
.that().resideInAPackage("..adapter.out..")
.should().dependOnClassesThat()
.resideInAnyPackage("..adapter.in..");
}