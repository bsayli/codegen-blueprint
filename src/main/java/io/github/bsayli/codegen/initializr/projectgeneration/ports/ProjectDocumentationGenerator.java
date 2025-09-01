package io.github.bsayli.codegen.initializr.projectgeneration.ports;

import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import java.io.File;
import java.io.IOException;

public interface ProjectDocumentationGenerator {

  void generateProjectDocument(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException;
}
