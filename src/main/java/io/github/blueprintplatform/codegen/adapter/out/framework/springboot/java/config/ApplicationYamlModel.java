package io.github.blueprintplatform.codegen.adapter.out.framework.springboot.java.config;

import io.github.blueprintplatform.codegen.adapter.out.shared.dependency.DependencyFeature;

final class ApplicationYamlModel {

  static final String KEY_APP_NAME = "applicationName";
  static final String KEY_FEATURES = "features";
  private static final String ORG_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";
  static final DependencyFeature H2 =
      new DependencyFeature("h2", ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-data-jpa");
  static final DependencyFeature ACTUATOR =
      new DependencyFeature("actuator", ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-actuator");
  static final DependencyFeature SECURITY =
      new DependencyFeature("security", ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-security");
  static final DependencyFeature[] FEATURES = {H2, ACTUATOR, SECURITY};

  private ApplicationYamlModel() {}
}
