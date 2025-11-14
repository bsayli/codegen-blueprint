package io.github.bsayli.codegen.initializr.adapter.out.filesystem;

import io.github.bsayli.codegen.initializr.adapter.error.exception.ProjectRootAlreadyExistsException;
import io.github.bsayli.codegen.initializr.adapter.error.exception.ProjectRootIOException;
import io.github.bsayli.codegen.initializr.adapter.error.exception.ProjectRootNotDirectoryException;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootExistencePolicy;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootPort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemProjectRootAdapter implements ProjectRootPort {

  @Override
  public Path prepareRoot(Path targetDir, String artifactId, ProjectRootExistencePolicy policy) {
    Path projectRoot = targetDir.resolve(artifactId);

    try {
      if (Files.exists(projectRoot)) {

        if (!Files.isDirectory(projectRoot)) {
          throw new ProjectRootNotDirectoryException(projectRoot);
        }

        if (policy == ProjectRootExistencePolicy.FAIL_IF_EXISTS) {
          throw new ProjectRootAlreadyExistsException(projectRoot);
        }

        return projectRoot;
      }

      Files.createDirectories(projectRoot);
      return projectRoot;

    } catch (IOException e) {
      throw new ProjectRootIOException(projectRoot, e);
    }
  }
}
