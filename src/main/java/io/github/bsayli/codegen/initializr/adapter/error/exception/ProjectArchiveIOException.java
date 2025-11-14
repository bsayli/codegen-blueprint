package io.github.bsayli.codegen.initializr.adapter.error.exception;

import java.nio.file.Path;

@SuppressWarnings("java:S110")
public final class ProjectArchiveIOException extends AdapterException {

  private static final String KEY = "adapter.project.archive.io";

  public ProjectArchiveIOException(Path root, Throwable cause) {
    super(KEY, cause, root);
  }
}
