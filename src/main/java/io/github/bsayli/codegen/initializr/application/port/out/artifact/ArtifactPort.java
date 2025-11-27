package io.github.bsayli.codegen.initializr.application.port.out.artifact;

import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;

public interface ArtifactPort {
  ArtifactKey artifactKey();

  Iterable<? extends GeneratedFile> generate(ProjectBlueprint blueprint);
}
