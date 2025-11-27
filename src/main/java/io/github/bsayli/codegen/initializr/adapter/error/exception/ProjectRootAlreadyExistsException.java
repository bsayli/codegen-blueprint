package io.github.bsayli.codegen.initializr.adapter.error.exception;

import java.nio.file.Path;

@SuppressWarnings("java:S110")
public final class ProjectRootAlreadyExistsException extends AdapterException {

  private static final String KEY = "adapter.project-root.already-exists";

  public ProjectRootAlreadyExistsException(Path path) {
    super(KEY, path);
  }
}
