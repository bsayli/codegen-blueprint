package io.github.blueprintplatform.codegen.application.error.exception;

import java.io.Serial;

public abstract class ApplicationException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1L;

  private final String messageKey;
  private final transient Object[] args;

  protected ApplicationException(String messageKey, Object... args) {
    super(messageKey);
    this.messageKey = messageKey;
    this.args = args;
  }

  protected ApplicationException(String messageKey, Throwable cause, Object... args) {
    super(messageKey, cause);
    this.messageKey = messageKey;
    this.args = args;
  }

  protected static Object[] prepend(Object first, Object... rest) {
    int extra = rest == null ? 0 : rest.length;
    Object[] merged = new Object[1 + extra];
    merged[0] = first;
    if (extra > 0) {
      System.arraycopy(rest, 0, merged, 1, extra);
    }
    return merged;
  }

  public String getMessageKey() {
    return messageKey;
  }

  public Object[] getArgs() {
    return args;
  }
}
