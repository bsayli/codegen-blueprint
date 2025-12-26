package io.github.blueprintplatform.codegen.adapter.out.framework.springboot.java.doc;

import io.github.blueprintplatform.codegen.adapter.out.shared.dependency.DependencyFeature;

final class ProjectDocumentationModel {

  static final String PROJECT_NAME = "projectName";
  static final String PROJECT_DESCRIPTION = "projectDescription";
  static final String GROUP_ID = "groupId";
  static final String ARTIFACT_ID = "artifactId";
  static final String PACKAGE_NAME = "packageName";
  static final String BUILD_TOOL = "buildTool";
  static final String LANGUAGE = "language";
  static final String FRAMEWORK = "framework";
  static final String JAVA_VERSION = "javaVersion";
  static final String LAYOUT = "layout";
  static final String ENFORCEMENT = "enforcement";
  static final String SAMPLE_CODE = "sampleCode";
  static final String SPRING_BOOT_VERSION = "springBootVersion";
  static final String DEPENDENCIES = "dependencies";
  static final String FEATURES = "features";
  private static final String ORG_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";
  static final DependencyFeature H2 =
      new DependencyFeature("h2", ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-data-jpa");
  static final DependencyFeature ACTUATOR =
      new DependencyFeature("actuator", ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-actuator");
  static final DependencyFeature SECURITY =
      new DependencyFeature("security", ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-security");
  static final DependencyFeature[] FEATURES_SET = {H2, ACTUATOR, SECURITY};

  private ProjectDocumentationModel() {}
}
