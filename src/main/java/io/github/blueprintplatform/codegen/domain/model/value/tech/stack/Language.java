package io.github.blueprintplatform.codegen.domain.model.value.tech.stack;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.shared.KeyEnumParser;
import io.github.blueprintplatform.codegen.domain.shared.KeyedEnum;

public enum Language implements KeyedEnum {
  JAVA("java");

  private static final ErrorCode UNKNOWN = () -> "project.tech-stack.language.unknown";

  private final String key;

  Language(String key) {
    this.key = key;
  }

  public static Language fromKey(String raw) {
    return KeyEnumParser.parse(Language.class, raw, UNKNOWN);
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
