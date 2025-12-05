package io.github.blueprintplatform.codegen.domain.error.exception;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;

public abstract class DomainException extends RuntimeException {
  private final transient ErrorCode code;
  private final transient Object[] args;

  protected DomainException(ErrorCode code, Object... args) {
    super(code.key());
    this.code = code;
    this.args = args;
  }

  public ErrorCode getCode() {
    return code;
  }

  public String getMessageKey() {
    return code.key();
  }

  public Object[] getArgs() {
    return args;
  }
}
