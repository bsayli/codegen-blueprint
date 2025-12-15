package io.github.blueprintplatform.codegen.domain.model.value.architecture;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.shared.KeyEnumParser;
import io.github.blueprintplatform.codegen.domain.shared.KeyedEnum;

public enum EnforcementMode implements KeyedEnum {
  NONE("none"),
  BASIC("basic"),
  STRICT("strict");

  private static final ErrorCode UNKNOWN = () -> "project.architecture.enforcement.unknown";

  private final String key;

  EnforcementMode(String key) {
    this.key = key;
  }

  public static EnforcementMode fromKey(String rawKey) {
    return KeyEnumParser.parse(EnforcementMode.class, rawKey, UNKNOWN);
  }

  public boolean isEnabled() {
    return this != NONE;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public String toString() {
    return key;
  }
}
