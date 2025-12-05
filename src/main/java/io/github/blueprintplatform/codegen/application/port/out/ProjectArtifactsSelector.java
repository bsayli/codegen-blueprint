package io.github.blueprintplatform.codegen.application.port.out;

import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;

public interface ProjectArtifactsSelector {
  ProjectArtifactsPort select(TechStack options);
}
