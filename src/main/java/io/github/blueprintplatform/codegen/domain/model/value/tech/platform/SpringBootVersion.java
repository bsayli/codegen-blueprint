package io.github.blueprintplatform.codegen.domain.model.value.tech.platform;

public enum SpringBootVersion {
  V3_5("3.5.8"), // Latest known stable patch for 3.5.x
  V3_4("3.4.12"); // Latest known stable patch for 3.4.x

  private final String defaultPatch;

  SpringBootVersion(String defaultPatch) {
    this.defaultPatch = defaultPatch;
  }

  public String majorMinor() {
    return name().substring(1).replace('_', '.');
  }

  public String defaultVersion() {
    return defaultPatch;
  }

  @Override
  public String toString() {
    return defaultVersion();
  }
}
