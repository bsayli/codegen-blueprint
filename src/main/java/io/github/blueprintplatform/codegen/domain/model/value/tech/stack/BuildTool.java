package io.github.blueprintplatform.codegen.domain.model.value.tech.stack;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.shared.KeyEnumParser;
import io.github.blueprintplatform.codegen.domain.shared.KeyedEnum;

public enum BuildTool implements KeyedEnum {
  MAVEN("maven");

  private static final ErrorCode UNKNOWN = () -> "project.tech-stack.build-tool.unknown";

  private final String key;

  BuildTool(String key) {
    this.key = key;
  }

  public static BuildTool fromKey(String raw) {
    return KeyEnumParser.parse(BuildTool.class, raw, UNKNOWN);
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
