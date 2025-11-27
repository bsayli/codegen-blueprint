package io.github.bsayli.codegen.initializr.domain.error.exception;

import io.github.bsayli.codegen.initializr.domain.error.code.ErrorCode;

public class DomainViolationException extends DomainException {
  public DomainViolationException(ErrorCode code, Object... args) {
    super(code, args);
  }
}
