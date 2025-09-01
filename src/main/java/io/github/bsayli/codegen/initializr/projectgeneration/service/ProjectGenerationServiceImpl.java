package io.github.bsayli.codegen.initializr.projectgeneration.service;

import io.github.bsayli.codegen.initializr.projectgeneration.generator.ProjectGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectType;
import io.github.bsayli.codegen.initializr.projectgeneration.registry.ProjectGeneratorRegistry;
import io.github.bsayli.codegen.initializr.projectgeneration.service.exception.ProjectGenerationException;
import java.io.IOException;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class ProjectGenerationServiceImpl implements ProjectGenerationService {

  private final ProjectGeneratorRegistry registry;

  public ProjectGenerationServiceImpl(ProjectGeneratorRegistry registry) {
    this.registry = registry;
  }

  @Override
  public Path generateProject(ProjectType projectType, ProjectMetadata projectMetadata) {
    ProjectGenerator projectGenerator =
        registry
            .getProjectGenerator(projectType)
            .orElseThrow(
                () -> new IllegalArgumentException("Unsupported project type: " + projectType));
    try {
      return projectGenerator.generateProject(projectMetadata);
    } catch (IOException e) {
      throw new ProjectGenerationException("Error generating project: " + e.getMessage(), e);
    }
  }
}
