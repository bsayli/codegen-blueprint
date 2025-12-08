package io.github.blueprintplatform.codegen.domain.factory;

import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectDescription;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectName;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import io.github.blueprintplatform.codegen.domain.policy.tech.CompatibilityPolicy;
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
      ProjectLayout layout,
      PlatformTarget platformTarget,
      Dependencies dependencies) {

    CompatibilityPolicy.ensureCompatible(techStack, platformTarget);

    return new ProjectBlueprint(
        identity, name, description, packageName, techStack, layout, platformTarget, dependencies);
  }

  public static ProjectBlueprint of(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      TechStack techStack,
      ProjectLayout layout,
      PlatformTarget platformTarget,
      List<Dependency> dependencies) {

    return of(
        identity,
        name,
        description,
        packageName,
        techStack,
        layout,
        platformTarget,
        Dependencies.of(dependencies));
  }

  public static ProjectBlueprint of(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      TechStack techStack,
      ProjectLayout layout,
      PlatformTarget platformTarget,
      Dependency... deps) {

    return of(
        identity,
        name,
        description,
        packageName,
        techStack,
        layout,
        platformTarget,
        Dependencies.of(Arrays.asList(deps)));
  }
}
