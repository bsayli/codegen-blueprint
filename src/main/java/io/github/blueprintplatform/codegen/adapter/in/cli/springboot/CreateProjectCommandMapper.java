package io.github.blueprintplatform.codegen.adapter.in.cli.springboot;

import io.github.blueprintplatform.codegen.adapter.in.cli.CliProjectRequest;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.dependency.SpringBootDependencyAlias;
import io.github.blueprintplatform.codegen.application.usecase.project.CreateProjectCommand;
import io.github.blueprintplatform.codegen.application.usecase.project.DependencyInput;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import java.util.ArrayList;
import java.util.List;

public class CreateProjectCommandMapper {

  public CreateProjectCommand from(
          CliProjectRequest request,
          BuildTool buildTool,
          Language language,
          JavaVersion javaVersion,
          SpringBootVersion bootVersion) {

    TechStack techStack = new TechStack(Framework.SPRING_BOOT, buildTool, language);
    PlatformTarget platformTarget = new SpringBootJvmTarget(javaVersion, bootVersion);
    ProjectLayout layout = ProjectLayout.fromKey(request.layoutKey());
    List<DependencyInput> inputs = toDependencyInputs(request.dependencies());

    return new CreateProjectCommand(
            request.groupId(),
            request.artifactId(),
            request.name(),
            request.description(),
            request.packageName(),
            techStack,
            layout,
            platformTarget,
            inputs,
            request.targetDirectory());
  }

  private List<DependencyInput> toDependencyInputs(List<String> aliases) {
    if (aliases == null || aliases.isEmpty()) {
      return List.of();
    }

    List<DependencyInput> result = new ArrayList<>();

    for (String raw : aliases) {
      if (raw == null || raw.isBlank()) {
        continue;
      }

      SpringBootDependencyAlias alias = SpringBootDependencyAlias.fromKey(raw);

      result.add(
              new DependencyInput(
                      alias.groupId(),
                      alias.artifactId(),
                      null,
                      null));
    }

    return List.copyOf(result);
  }
}