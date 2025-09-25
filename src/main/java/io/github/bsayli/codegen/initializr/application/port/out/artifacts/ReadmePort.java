package io.github.bsayli.codegen.initializr.application.port.out.artifacts;

import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;

public interface ReadmePort {
  GeneratedFile generate(ProjectBlueprint blueprint);
}
