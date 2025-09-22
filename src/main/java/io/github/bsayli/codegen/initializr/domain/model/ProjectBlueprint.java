package io.github.bsayli.codegen.initializr.domain.model;

import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectDescription;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectName;
import io.github.bsayli.codegen.initializr.domain.model.value.pkg.PackageName;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.options.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;

public class ProjectBlueprint {

  private final ProjectIdentity identity;
  private final ProjectName name;
  private final ProjectDescription description;
  private final PackageName packageName;
  private final BuildOptions buildOptions;
  private final PlatformTarget platformTarget;

  public ProjectBlueprint(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      BuildOptions buildOptions,
      PlatformTarget platformTarget) {
    this.identity = identity;
    this.name = name;
    this.description = description;
    this.packageName = packageName;
    this.buildOptions = buildOptions;
    this.platformTarget = platformTarget;
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

  public BuildOptions getBuildOptions() {
    return buildOptions;
  }

  public PlatformTarget getPlatformTarget() {
    return platformTarget;
  }
}
