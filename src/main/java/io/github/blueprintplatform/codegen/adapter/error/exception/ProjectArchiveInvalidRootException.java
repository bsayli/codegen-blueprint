package io.github.blueprintplatform.codegen.adapter.error.exception;

import java.nio.file.Path;

@SuppressWarnings("java:S110")
public final class ProjectArchiveInvalidRootException extends AdapterException {

  private static final String KEY = "adapter.project.archive.invalid.root";

  public ProjectArchiveInvalidRootException(Path root) {
    super(KEY, root != null ? root : "<null>");
  }
}
