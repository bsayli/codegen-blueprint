package io.github.bsayli.codegen.initializr.adapter.error.exception;

import java.nio.file.Path;

@SuppressWarnings("java:S110")
public final class ProjectArchiveException extends AdapterException {

  private static final String KEY = "adapter.project.archive.failed";

  public ProjectArchiveException(Path projectRoot, Throwable cause) {
    super(KEY, cause, projectRoot);
  }
}
