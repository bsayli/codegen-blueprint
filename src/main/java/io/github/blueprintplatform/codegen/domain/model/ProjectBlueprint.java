package io.github.blueprintplatform.codegen.domain.model;

import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectDescription;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectName;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;

public class ProjectBlueprint {

  private final ProjectIdentity identity;
  private final ProjectName name;
  private final ProjectDescription description;
  private final PackageName packageName;
  private final TechStack techStack;
  private final ProjectLayout layout;
  private final PlatformTarget platformTarget;
  private final Dependencies dependencies;

  public ProjectBlueprint(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      TechStack techStack,
      ProjectLayout layout,
      PlatformTarget platformTarget,
      Dependencies dependencies) {
    this.identity = identity;
    this.name = name;
    this.description = description;
    this.packageName = packageName;
    this.techStack = techStack;
    this.layout = layout;
    this.platformTarget = platformTarget;
    this.dependencies = dependencies;
  }

  public ProjectIdentity getIdentity() {
    return identity;
  }

  public ProjectName getName() {
    return name;
  }

  public ProjectDescription getDescription() {
    return description;
  }

  public PackageName getPackageName() {
    return packageName;
  }

  public TechStack getTechStack() {
    return techStack;
  }

  public ProjectLayout getLayout() {
    return layout;
  }

  public PlatformTarget getPlatformTarget() {
    return platformTarget;
  }

  public Dependencies getDependencies() {
    return dependencies;
  }
}
