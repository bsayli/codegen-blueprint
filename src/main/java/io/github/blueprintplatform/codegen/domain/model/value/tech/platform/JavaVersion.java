package io.github.blueprintplatform.codegen.domain.model.value.tech.platform;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.shared.KeyEnumParser;
import io.github.blueprintplatform.codegen.domain.shared.KeyedEnum;

public enum JavaVersion implements KeyedEnum {
  JAVA_21("21", 21),
  JAVA_25("25", 25);

  private static final ErrorCode UNKNOWN = () -> "platform.java-version.unknown";

  private final String key;
  private final int major;

  JavaVersion(String key, int major) {
    this.key = key;
    this.major = major;
  }

  public static JavaVersion fromKey(String raw) {
    return KeyEnumParser.parse(JavaVersion.class, raw, UNKNOWN);
  }

  @Override
  public String key() {
    return key;
  }

  public int major() {
    return major;
  }

  public String asString() {
    return key;
  }

  @Override
  public String toString() {
    return key;
  }
}
