package io.github.blueprintplatform.codegen.adapter.in.cli.springboot;

import io.github.blueprintplatform.codegen.adapter.in.cli.CliProjectRequest;
import io.github.blueprintplatform.codegen.application.usecase.project.CreateProjectCommand;
import io.github.blueprintplatform.codegen.application.usecase.project.DependencyInput;
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
    List<DependencyInput> inputs = toDependencyInputs(request.dependencies());

    return new CreateProjectCommand(
        request.groupId(),
        request.artifactId(),
        request.name(),
        request.description(),
        request.packageName(),
        techStack,
        platformTarget,
        inputs,
        request.targetDirectory());
  }

  private List<DependencyInput> toDependencyInputs(List<String> dependencies) {
    if (dependencies == null || dependencies.isEmpty()) {
      return List.of();
    }

    List<DependencyInput> result = new ArrayList<>();

    for (String raw : dependencies) {
      if (raw == null || raw.isBlank()) {
        continue;
      }

      String trimmed = raw.trim();
      String[] parts = trimmed.split(":");

      if (parts.length < 2 || parts.length > 4) {
        throw new IllegalArgumentException(
            "Invalid dependency format: '%s'. Expected 'groupId:artifactId[:version[:scope]]'"
                .formatted(trimmed));
      }

      String groupId = parts[0];
      String artifactId = parts[1];
      String version = parts.length >= 3 ? parts[2] : null;
      String scope = parts.length == 4 ? parts[3] : null;

      result.add(new DependencyInput(groupId, artifactId, version, scope));
    }

    return List.copyOf(result);
  }
}
