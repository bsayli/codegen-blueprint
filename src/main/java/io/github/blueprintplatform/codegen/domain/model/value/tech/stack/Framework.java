package io.github.blueprintplatform.codegen.domain.model.value.tech.stack;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.shared.KeyEnumParser;
import io.github.blueprintplatform.codegen.domain.shared.KeyedEnum;

public enum Framework implements KeyedEnum {
  SPRING_BOOT("spring-boot");

  private static final ErrorCode UNKNOWN = () -> "project.tech-stack.framework.unknown";

  private final String key;

  Framework(String key) {
    this.key = key;
  }

  public static Framework fromKey(String raw) {
    return KeyEnumParser.parse(Framework.class, raw, UNKNOWN);
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
