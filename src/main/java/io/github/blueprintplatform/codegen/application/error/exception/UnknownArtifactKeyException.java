package io.github.blueprintplatform.codegen.application.error.exception;

public final class UnknownArtifactKeyException extends ApplicationException {

  private static final String KEY = "application.artifact.key.unknown";

  public UnknownArtifactKeyException(String key) {
    super(KEY, key);
  }
}
