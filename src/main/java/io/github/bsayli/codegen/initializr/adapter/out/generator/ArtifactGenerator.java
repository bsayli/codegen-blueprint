package io.github.bsayli.codegen.initializr.adapter.out.generator;

import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;

public interface ArtifactGenerator {
  default boolean supports(ProjectBlueprint blueprint) {
    return true;
  }

  default int order() {
    return 100;
  }

  Iterable<? extends GeneratedFile> generateFiles(ProjectBlueprint blueprint);

  default String name() {
    return getClass().getSimpleName();
  }
}
