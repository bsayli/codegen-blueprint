package io.github.bsayli.codegen.initializr.application.port.out;

import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;

public interface ProjectArtifactsSelector {
  ProjectArtifactsPort select(TechStack options);
}
