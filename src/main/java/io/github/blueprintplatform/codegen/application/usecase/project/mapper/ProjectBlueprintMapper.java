package io.github.blueprintplatform.codegen.application.usecase.project.mapper;

import io.github.blueprintplatform.codegen.application.port.in.project.dto.CreateProjectRequest;
import io.github.blueprintplatform.codegen.application.port.in.project.dto.DependencyInput;
import io.github.blueprintplatform.codegen.domain.factory.ProjectBlueprintFactory;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyCoordinates;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyScope;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyVersion;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ArtifactId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.GroupId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.metadata.ProjectMetadata;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectDescription;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectName;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.tech.PlatformSpec;
import java.util.ArrayList;
import java.util.List;

public class ProjectBlueprintMapper {

  public ProjectBlueprint from(CreateProjectRequest c) {
    ProjectMetadata metadata =
        new ProjectMetadata(
            new ProjectIdentity(new GroupId(c.groupId()), new ArtifactId(c.artifactId())),
            new ProjectName(c.projectName()),
            new ProjectDescription(c.projectDescription()),
            new PackageName(c.packageName()));

    PlatformSpec platform = new PlatformSpec(c.techStack(), c.platformTarget());

    ArchitectureGovernance architectureGovernance = new ArchitectureGovernance(c.enforcementMode());
    ArchitectureSpec architecture =
        new ArchitectureSpec(c.layout(), architectureGovernance, c.sampleCodeOptions());

    Dependencies dependencies = mapDependencies(c.dependencies());

    return ProjectBlueprintFactory.of(metadata, platform, architecture, dependencies);
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
