package io.github.bsayli.codegen.initializr.adapter.out.profile;

import io.github.bsayli.codegen.initializr.adapter.error.exception.ArtifactsPortNotFoundException;
import io.github.bsayli.codegen.initializr.adapter.error.exception.UnsupportedProfileTypeException;
import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsSelector;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;
import java.util.Map;

public class ProfileBasedArtifactsSelector implements ProjectArtifactsSelector {

  private final Map<ProfileType, ProjectArtifactsPort> registry;

  public ProfileBasedArtifactsSelector(Map<ProfileType, ProjectArtifactsPort> registry) {
    this.registry = registry;
  }

  @Override
  public ProjectArtifactsPort select(TechStack options) {
    ProfileType type = ProfileType.from(options);
    if (type == null) {
      throw new UnsupportedProfileTypeException(options);
    }

    ProjectArtifactsPort port = registry.get(type);
    if (port == null) {
      throw new ArtifactsPortNotFoundException(type);
    }

    return port;
  }
}
