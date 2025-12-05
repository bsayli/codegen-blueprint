package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java;

import io.github.blueprintplatform.codegen.application.port.out.ProjectArtifactsPort;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedFile;
import java.util.List;
import java.util.stream.StreamSupport;

public class SpringBootMavenJavaArtifactsAdapter implements ProjectArtifactsPort {

  private final List<ArtifactPort> artifacts;

  public SpringBootMavenJavaArtifactsAdapter(List<ArtifactPort> artifacts) {
    this.artifacts = artifacts;
  }

  @Override
  public Iterable<? extends GeneratedFile> generate(ProjectBlueprint blueprint) {
    return artifacts.stream()
        .flatMap(p -> StreamSupport.stream(p.generate(blueprint).spliterator(), false))
        .toList();
  }
}
