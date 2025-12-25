package io.github.blueprintplatform.codegen.domain.error.code;

public enum Field {
  PROJECT_NAME(project("name")),
  PROJECT_DESCRIPTION(project("description")),
  GROUP_ID(project("group-id")),
  ARTIFACT_ID(project("artifact-id")),
  PACKAGE_NAME(project("package-name")),
  DEPENDENCY_VERSION(dependency("version"));

  private final String key;

  Field(String key) {
    this.key = key;
  }

  private static String project(String suffix) {
    return "project." + suffix;
  }

  private static String dependency(String suffix) {
    return "dependency." + suffix;
  }

  public String key() {
    return key;
  }
}
