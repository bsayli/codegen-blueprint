package io.github.blueprintplatform.codegen.domain.error.exception;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;

public class DomainViolationException extends DomainException {
  public DomainViolationException(ErrorCode code, Object... args) {
    super(code, args);
  }
}
