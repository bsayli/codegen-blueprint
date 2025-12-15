package ${projectPackageName}.architecture;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
* Strict no-cycles rules.
* Prevents package cycles across the project and within adapter subpackages.
*/
@AnalyzeClasses(
packages = "${projectPackageName}",
importOptions = ImportOption.DoNotIncludeTests.class
)
class CyclesTest {

@ArchTest
static final ArchRule layers_must_be_free_of_cycles =
slices().matching("${projectPackageName}.(*)..").should().beFreeOfCycles();

@ArchTest
static final ArchRule adapter_subpackages_must_be_free_of_cycles =
slices().matching("${projectPackageName}.adapter.(*)..").should().beFreeOfCycles();
}