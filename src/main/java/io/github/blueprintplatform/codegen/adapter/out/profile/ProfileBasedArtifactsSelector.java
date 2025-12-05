package io.github.blueprintplatform.codegen.adapter.out.profile;

import io.github.blueprintplatform.codegen.adapter.error.exception.ArtifactsPortNotFoundException;
import io.github.blueprintplatform.codegen.adapter.error.exception.UnsupportedProfileTypeException;
import io.github.blueprintplatform.codegen.application.port.out.ProjectArtifactsPort;
import io.github.blueprintplatform.codegen.application.port.out.ProjectArtifactsSelector;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
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
