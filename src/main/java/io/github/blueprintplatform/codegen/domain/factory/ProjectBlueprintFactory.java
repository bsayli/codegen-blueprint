package io.github.blueprintplatform.codegen.domain.factory;

import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.metadata.ProjectMetadata;
import io.github.blueprintplatform.codegen.domain.model.value.tech.PlatformSpec;
import io.github.blueprintplatform.codegen.domain.policy.tech.CompatibilityPolicy;

public final class ProjectBlueprintFactory {

  private ProjectBlueprintFactory() {}

  public static ProjectBlueprint of(
      ProjectMetadata metadata,
      PlatformSpec platform,
      ArchitectureSpec architecture,
      Dependencies dependencies) {

    CompatibilityPolicy.ensureCompatible(platform.techStack(), platform.platformTarget());

    return new ProjectBlueprint(metadata, platform, architecture, dependencies);
  }
}
