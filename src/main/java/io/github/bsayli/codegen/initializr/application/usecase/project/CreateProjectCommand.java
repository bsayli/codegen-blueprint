package io.github.bsayli.codegen.initializr.application.usecase.project;

import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;
import java.nio.file.Path;
import java.util.List;

public record CreateProjectCommand(
    String groupId,
    String artifactId,
    String projectName,
    String projectDescription,
    String packageName,
    TechStack techStack,
    PlatformTarget platformTarget,
    List<DependencyInput> dependencies,
    Path targetDirectory) {}
