package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.wrapper;

import io.github.bsayli.codegen.initializr.adapter.out.spi.ArtifactGenerator;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.MavenWrapperPort;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;

public final class MavenWrapperAdapter implements MavenWrapperPort, ArtifactGenerator {

  private static final int ORDER = 60;
  private static final String NAME = "maven-wrapper";

  @Override
  public Iterable<? extends GeneratedFile> generate(ProjectBlueprint blueprint) {
    throw new UnsupportedOperationException("MavenWrapperAdapter.generate not implemented yet");
  }

  @Override
  public Iterable<? extends GeneratedFile> generateFiles(ProjectBlueprint blueprint) {
    return generate(blueprint);
  }

  @Override
  public int order() {
    return ORDER;
  }

  @Override
  public String name() {
    return NAME;
  }
}
