package io.github.blueprintplatform.codegen.domain.model.value.dependency;

public enum DependencyScope {
  COMPILE("compile"),
  PROVIDED("provided"),
  RUNTIME("runtime"),
  TEST("test"),
  SYSTEM("system"),
  IMPORT("import");

  private final String value;

  DependencyScope(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }
}
