package io.github.bsayli.codegen.initializr.projectgeneration.generator;

import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import java.io.IOException;
import java.nio.file.Path;

public interface ProjectGenerator {

  /**
   * Generates a project based on the provided project metadata. This method delegates the specific
   * generation tasks to appropriate collaborators (ports) based on the project type.
   *
   * @param projectMetadata The project metadata object containing information about the desired
   *     project type.
   * @throws IOException If an error occurs during project generation.
   */
  Path generateProject(ProjectMetadata projectMetadata) throws IOException;
}
