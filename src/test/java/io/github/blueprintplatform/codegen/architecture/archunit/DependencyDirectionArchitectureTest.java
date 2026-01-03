package io.github.blueprintplatform.codegen.architecture.archunit;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
    packages = DependencyDirectionArchitectureTest.BASE_PACKAGE,
    importOptions = ImportOption.DoNotIncludeTests.class)
class DependencyDirectionArchitectureTest {

  static final String BASE_PACKAGE = "io.github.blueprintplatform.codegen";

  private static final String APPLICATION_ROOT = BASE_PACKAGE + ".application..";
  private static final String APPLICATION_PORTS = BASE_PACKAGE + ".application.port..";
  private static final String ADAPTER_ROOT = BASE_PACKAGE + ".adapter..";
  private static final String BOOTSTRAP_ROOT = BASE_PACKAGE + ".bootstrap..";

  @ArchTest
  static final ArchRule application_implementation_must_not_depend_on_adapters =
      noClasses()
          .that()
          .resideInAnyPackage(APPLICATION_ROOT)
          .and()
          .resideOutsideOfPackage(APPLICATION_PORTS)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(ADAPTER_ROOT)
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule adapters_must_not_depend_on_application_implementation =
      noClasses()
          .that()
          .resideInAnyPackage(ADAPTER_ROOT)
          .should()
          .dependOnClassesThat(applicationImplementation())
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule bootstrap_must_not_be_depended_on =
      noClasses()
          .that()
          .resideOutsideOfPackage(BOOTSTRAP_ROOT)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(BOOTSTRAP_ROOT)
          .allowEmptyShould(true);

  private static com.tngtech.archunit.base.DescribedPredicate<JavaClass>
      applicationImplementation() {
    return describe(
        "reside in application but outside application.port",
        c -> {
          String pkg = c.getPackageName();
          if (pkg == null || pkg.isBlank()) {
            return false;
          }
          return pkg.startsWith(BASE_PACKAGE + ".application.")
              && !pkg.startsWith(BASE_PACKAGE + ".application.port.");
        });
  }
}
