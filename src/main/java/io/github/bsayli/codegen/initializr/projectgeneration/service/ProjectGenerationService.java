package io.github.bsayli.codegen.initializr.projectgeneration.service;

import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectType;
import java.io.IOException;
import java.nio.file.Path;

public interface ProjectGenerationService {

  Path generateProject(ProjectType projectType, ProjectMetadata projectMetadata) throws IOException;
}
