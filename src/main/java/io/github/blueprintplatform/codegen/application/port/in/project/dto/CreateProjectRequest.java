package io.github.blueprintplatform.codegen.application.port.in.project.dto;

import io.github.blueprintplatform.codegen.domain.model.value.architecture.EnforcementMode;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import java.nio.file.Path;
import java.util.List;

public record CreateProjectRequest(
    String groupId,
    String artifactId,
    String projectName,
    String projectDescription,
    String packageName,
    TechStack techStack,
    ProjectLayout layout,
    EnforcementMode enforcementMode,
    PlatformTarget platformTarget,
    List<DependencyInput> dependencies,
    SampleCodeOptions sampleCodeOptions,
    Path targetDirectory) {}
