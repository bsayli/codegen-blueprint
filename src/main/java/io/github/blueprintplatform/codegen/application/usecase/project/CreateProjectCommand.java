package io.github.blueprintplatform.codegen.application.usecase.project;

import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import java.nio.file.Path;
import java.util.List;

public record CreateProjectCommand(
    String groupId,
    String artifactId,
    String projectName,
    String projectDescription,
    String packageName,
    TechStack techStack,
    ProjectLayout layout,
    PlatformTarget platformTarget,
    List<DependencyInput> dependencies,
    Path targetDirectory) {}
