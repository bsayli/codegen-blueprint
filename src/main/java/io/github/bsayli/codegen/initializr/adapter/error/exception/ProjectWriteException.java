package io.github.bsayli.codegen.initializr.adapter.error.exception;

import java.nio.file.Path;

@SuppressWarnings("java:S110")
public final class ProjectWriteException extends AdapterException {

  private static final String KEY = "adapter.project.write.failed";

  public ProjectWriteException(Path path, Throwable cause) {
    super(KEY, cause, path);
  }
}
