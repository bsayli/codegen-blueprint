package io.github.blueprintplatform.codegen.adapter.error.exception;

import io.github.blueprintplatform.codegen.bootstrap.error.exception.InfrastructureException;

public abstract class AdapterException extends InfrastructureException {
  protected AdapterException(String messageKey, Object... args) {
    super(messageKey, args);
  }

  protected AdapterException(String messageKey, Throwable cause, Object... args) {
    super(messageKey, cause, args);
  }
}
