package io.github.bsayli.codegen.initializr.domain.error.code;

public final class ErrorKeys {
  private ErrorKeys() {}

  public static ErrorCode compose(Field field, Violation v) {
    return () -> field.key() + v.suffix;
  }
}
