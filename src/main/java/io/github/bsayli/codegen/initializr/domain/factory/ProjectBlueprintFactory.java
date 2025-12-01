package io.github.bsayli.codegen.initializr.domain.factory;

import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependencies;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependency;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectDescription;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectName;
import io.github.bsayli.codegen.initializr.domain.model.value.pkg.PackageName;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;
import io.github.bsayli.codegen.initializr.domain.policy.tech.CompatibilityPolicy;
import java.util.Arrays;
import java.util.List;

public final class ProjectBlueprintFactory {

  private ProjectBlueprintFactory() {}

  public static ProjectBlueprint of(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      TechStack techStack,
      PlatformTarget platformTarget,
      Dependencies dependencies) {

    CompatibilityPolicy.ensureCompatible(techStack, platformTarget);

    return new ProjectBlueprint(
        identity, name, description, packageName, techStack, platformTarget, dependencies);
  }

  public static ProjectBlueprint of(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      TechStack techStack,
      PlatformTarget platformTarget,
      List<Dependency> dependencies) {
    return of(
        identity,
        name,
        description,
        packageName,
        techStack,
        platformTarget,
        Dependencies.of(dependencies));
  }

  public static ProjectBlueprint of(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      TechStack techStack,
      PlatformTarget platformTarget,
      Dependency... deps) {
    return of(
        identity,
        name,
        description,
        packageName,
        techStack,
        platformTarget,
        Dependencies.of(Arrays.asList(deps)));
  }
}
