package io.github.blueprintplatform.codegen.domain.model.value.layout;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.shared.KeyEnumParser;
import io.github.blueprintplatform.codegen.domain.shared.KeyedEnum;

public enum ProjectLayout implements KeyedEnum {
  STANDARD("standard"),
  HEXAGONAL("hexagonal");

  private static final ErrorCode UNKNOWN = () -> "project.layout.unknown";

  private final String key;

  ProjectLayout(String key) {
    this.key = key;
  }

  public static ProjectLayout fromKey(String rawKey) {
    return KeyEnumParser.parse(ProjectLayout.class, rawKey, UNKNOWN);
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
