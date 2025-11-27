package io.github.bsayli.codegen.initializr.domain.model.value.tech.platform;

public enum JavaVersion {
  JAVA_21(21),
  JAVA_25(25);

  private final int major;

  JavaVersion(int major) {
    this.major = major;
  }

  public int major() {
    return major;
  }

  public String asString() {
    return String.valueOf(major);
  }
}
