package io.github.bsayli.codegen.initializr.domain.factory;

import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ArtifactId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.GroupId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectDescription;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectName;
import io.github.bsayli.codegen.initializr.domain.model.value.pkg.PackageName;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.options.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
import io.github.bsayli.codegen.initializr.domain.policy.tech.CompatibilityPolicy;
import io.github.bsayli.codegen.initializr.domain.policy.tech.PlatformTargetSelector;
import java.util.Objects;

public final class ProjectBlueprintFactory {

  private ProjectBlueprintFactory() {}

  public static ProjectBlueprint create(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      BuildOptions buildOptions,
      PlatformTarget platformTarget) {
    Objects.requireNonNull(identity);
    Objects.requireNonNull(name);
    Objects.requireNonNull(description);
    Objects.requireNonNull(packageName);
    Objects.requireNonNull(buildOptions);
    Objects.requireNonNull(platformTarget);

    CompatibilityPolicy.ensureCompatible(buildOptions, platformTarget);

    return new ProjectBlueprint(
        identity, name, description, packageName, buildOptions, platformTarget);
  }

  public static ProjectBlueprint createWithAutoTarget(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      BuildOptions buildOptions) {
    Objects.requireNonNull(buildOptions);
    PlatformTarget target = PlatformTargetSelector.selectDefaultFor(buildOptions);
    return create(identity, name, description, packageName, buildOptions, target);
  }

  public static ProjectBlueprint createFromRaw(
      String groupId,
      String artifactId,
      String projectName,
      String projectDescription,
      String packageName,
      BuildOptions buildOptions,
      JavaVersion preferredJava,
      SpringBootVersion preferredBoot) {
    ProjectIdentity identity =
        new ProjectIdentity(new GroupId(groupId), new ArtifactId(artifactId));
    ProjectName name = new ProjectName(projectName);
    ProjectDescription description = new ProjectDescription(projectDescription);
    PackageName pkg = new PackageName(packageName);

    PlatformTarget target =
        PlatformTargetSelector.selectOrDefault(buildOptions, preferredJava, preferredBoot);

    return create(identity, name, description, pkg, buildOptions, target);
  }

  public static ProjectBlueprint createFromRawWithAutoTarget(
      String groupId,
      String artifactId,
      String projectName,
      String projectDescription,
      String packageName,
      BuildOptions buildOptions) {
    ProjectIdentity identity =
        new ProjectIdentity(new GroupId(groupId), new ArtifactId(artifactId));
    ProjectName name = new ProjectName(projectName);
    ProjectDescription description = new ProjectDescription(projectDescription);
    PackageName pkg = new PackageName(packageName);

    return createWithAutoTarget(identity, name, description, pkg, buildOptions);
  }
}
