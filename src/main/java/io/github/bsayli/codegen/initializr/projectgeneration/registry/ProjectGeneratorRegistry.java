package io.github.bsayli.codegen.initializr.projectgeneration.registry;

import io.github.bsayli.codegen.initializr.projectgeneration.generator.ProjectGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectType;
import java.util.Optional;

public interface ProjectGeneratorRegistry {

  Optional<ProjectGenerator> getProjectGenerator(ProjectType projectType);
}
