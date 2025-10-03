package io.github.bsayli.codegen.initializr.adapter.error;

import io.github.bsayli.codegen.initializr.bootstrap.error.InfrastructureException;

public abstract class AdapterException extends InfrastructureException {
  protected AdapterException(String messageKey, Object... args) {
    super(messageKey, args);
  }

  protected AdapterException(String messageKey, Throwable cause, Object... args) {
    super(messageKey, cause, args);
  }
}
