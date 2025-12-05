package io.github.blueprintplatform.codegen.adapter.error.exception;

import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;

@SuppressWarnings("java:S110")
public final class ArtifactKeyMismatchException extends AdapterException {
  private static final String KEY = "adapter.generator.key.mismatch";

  public ArtifactKeyMismatchException(ArtifactKey expected, ArtifactKey actual) {
    super(KEY, expected.key(), actual.key());
  }
}
