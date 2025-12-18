package io.github.blueprintplatform.codegen.domain.model.value.tech.platform;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.shared.KeyEnumParser;
import io.github.blueprintplatform.codegen.domain.shared.KeyedEnum;

public enum SpringBootVersion implements KeyedEnum {
  V3_5("3.5", "3.5.9"),
  V3_4("3.4", "3.4.13");

  private static final ErrorCode UNKNOWN = () -> "platform.springboot-version.unknown";

  private final String key;
  private final String defaultPatch;

  SpringBootVersion(String key, String defaultPatch) {
    this.key = key;
    this.defaultPatch = defaultPatch;
  }

  public static SpringBootVersion fromKey(String raw) {
    return KeyEnumParser.parse(SpringBootVersion.class, raw, UNKNOWN);
  }

  @Override
  public String key() {
    return key;
  }

  public String majorMinor() {
    return key;
  }

  public String defaultVersion() {
    return defaultPatch;
  }

  @Override
  public String toString() {
    return key;
  }
}
