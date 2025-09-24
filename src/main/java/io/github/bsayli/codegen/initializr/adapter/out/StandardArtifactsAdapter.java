package io.github.bsayli.codegen.initializr.adapter.out;

import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.port.out.GitIgnorePort;
import io.github.bsayli.codegen.initializr.domain.port.out.MavenPomPort;
import io.github.bsayli.codegen.initializr.domain.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.util.List;

public class StandardArtifactsAdapter implements ProjectArtifactsPort {

  private final MavenPomPort mavenPomPort;
  private final GitIgnorePort gitIgnorePort;

  public StandardArtifactsAdapter(MavenPomPort mavenPomPort, GitIgnorePort gitIgnorePort) {
    this.mavenPomPort = mavenPomPort;
    this.gitIgnorePort = gitIgnorePort;
  }

  @Override
  public Iterable<? extends GeneratedFile> generate(ProjectBlueprint blueprint) {
    BuildOptions options = blueprint.getBuildOptions();

    return List.of(
            mavenPomPort.generate(blueprint),
            gitIgnorePort.generate(options)
    );
  }
}