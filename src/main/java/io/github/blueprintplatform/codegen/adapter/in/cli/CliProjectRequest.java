package io.github.blueprintplatform.codegen.adapter.in.cli;

import java.nio.file.Path;
import java.util.List;

public record CliProjectRequest(
    String groupId,
    String artifactId,
    String name,
    String description,
    String packageName,
    String profile,
    String layoutKey,
    String enforcementModeKey,
    List<String> dependencies,
    String sampleCodeLevelKey,
    Path targetDirectory) {}
