package io.github.bsayli.codegen.initializr.adapter.out.generator.build;

import io.github.bsayli.codegen.initializr.adapter.out.generator.ArtifactGenerator;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.MavenPomPort;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.util.List;

public final class MavenPomAdapter implements MavenPomPort, ArtifactGenerator {

  private static final int ORDER = 10;
  private static final String GENERATOR_NAME = "maven-pom";

  @Override
  public GeneratedFile generate(ProjectBlueprint blueprint) {
    throw new UnsupportedOperationException("MavenPomAdapter.generate not implemented yet");
  }

  @Override
  public Iterable<? extends GeneratedFile> generateFiles(ProjectBlueprint blueprint) {
    return List.of(generate(blueprint));
  }

  @Override
  public int order() {
    return ORDER;
  }

  @Override
  public String name() {
    return GENERATOR_NAME;
  }
}
