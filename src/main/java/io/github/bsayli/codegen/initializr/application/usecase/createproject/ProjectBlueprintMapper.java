package io.github.bsayli.codegen.initializr.application.usecase.createproject;

import io.github.bsayli.codegen.initializr.domain.factory.ProjectBlueprintFactory;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependencies;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependency;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.DependencyCoordinates;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.DependencyScope;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.DependencyVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ArtifactId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.GroupId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectDescription;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectName;
import io.github.bsayli.codegen.initializr.domain.model.value.pkg.PackageName;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.policy.tech.PlatformTargetSelector;
import java.util.ArrayList;
import java.util.List;

public class ProjectBlueprintMapper {

  public ProjectBlueprint toDomain(CreateProjectCommand c) {
    ProjectIdentity identity =
        new ProjectIdentity(new GroupId(c.groupId()), new ArtifactId(c.artifactId()));

    ProjectName name = new ProjectName(c.projectName());
    ProjectDescription description = new ProjectDescription(c.projectDescription());
    PackageName pkg = new PackageName(c.packageName());

    PlatformTarget target =
        PlatformTargetSelector.select(c.buildOptions(), c.preferredJava(), c.preferredBoot());

    Dependencies deps = mapDependencies(c.dependencies());

    return ProjectBlueprintFactory.of(
        identity, name, description, pkg, c.buildOptions(), target, deps);
  }

  private Dependencies mapDependencies(List<DependencyInput> raw) {
    if (raw == null || raw.isEmpty()) {
      return Dependencies.of(List.of());
    }

    List<Dependency> items = new ArrayList<>(raw.size());
    for (DependencyInput d : raw) {
      DependencyVersion version =
          (d.version() == null || d.version().isBlank())
              ? null
              : new DependencyVersion(d.version());

      DependencyScope scope =
          (d.scope() == null || d.scope().isBlank())
              ? null
              : DependencyScope.valueOf(d.scope().trim().toUpperCase());

      items.add(
          new Dependency(
              new DependencyCoordinates(new GroupId(d.groupId()), new ArtifactId(d.artifactId())),
              version,
              scope));
    }
    return Dependencies.of(items);
  }
}
