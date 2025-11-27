package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java;

import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactPort;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
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
