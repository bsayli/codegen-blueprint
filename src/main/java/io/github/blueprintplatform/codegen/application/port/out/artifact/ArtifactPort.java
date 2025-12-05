package io.github.blueprintplatform.codegen.application.port.out.artifact;

import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedFile;

public interface ArtifactPort {
  ArtifactKey artifactKey();

  Iterable<? extends GeneratedFile> generate(ProjectBlueprint blueprint);
}
