package io.github.bsayli.codegen.initializr.adapter.error.exception;

import java.nio.file.Path;

@SuppressWarnings("java:S110")
public final class ProjectRootNotDirectoryException extends AdapterException {

  private static final String KEY = "adapter.project-root.not-directory";

  public ProjectRootNotDirectoryException(Path path) {
    super(KEY, path);
  }
}
