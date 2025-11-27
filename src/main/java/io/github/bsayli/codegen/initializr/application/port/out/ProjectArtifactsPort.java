package io.github.bsayli.codegen.initializr.application.port.out;

import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;

public interface ProjectArtifactsPort {

  Iterable<? extends GeneratedFile> generate(ProjectBlueprint blueprint);
}
