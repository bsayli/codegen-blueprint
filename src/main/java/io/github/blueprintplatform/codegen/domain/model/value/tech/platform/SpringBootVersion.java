package io.github.blueprintplatform.codegen.domain.model.value.tech.platform;

public enum SpringBootVersion {
  V3_5_8("3.5.8"),
  V3_4_12("3.4.12");

  private final String value;

  SpringBootVersion(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
