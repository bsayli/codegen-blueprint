package io.github.blueprintplatform.codegen.architecture.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
    packages = "io.github.blueprintplatform.codegen",
    importOptions = ImportOption.DoNotIncludeTests.class)
class InboundAdapterOutboundPortsIsolationArchitectureTest {

  private static final String INBOUND_ADAPTERS = "..adapter.in..";

  private static final String DOMAIN_OUTBOUND_PORTS = "..domain.port.out..";
  @ArchTest
  static final ArchRule inbound_adapters_must_not_depend_on_domain_outbound_ports =
      noClasses()
          .that()
          .resideInAPackage(INBOUND_ADAPTERS)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(DOMAIN_OUTBOUND_PORTS)
          .allowEmptyShould(true);
  private static final String APPLICATION_OUTBOUND_PORTS = "..application.port.out..";
  @ArchTest
  static final ArchRule inbound_adapters_must_not_depend_on_application_outbound_ports =
      noClasses()
          .that()
          .resideInAPackage(INBOUND_ADAPTERS)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(APPLICATION_OUTBOUND_PORTS)
          .allowEmptyShould(true);
}
