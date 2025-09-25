package io.github.bsayli.codegen.initializr.adapter.out.generator.config;

import io.github.bsayli.codegen.initializr.adapter.out.generator.ArtifactGenerator;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.ConfigFilesPort;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;

public final class ConfigFilesAdapter implements ConfigFilesPort, ArtifactGenerator {

  private static final int ORDER = 50;
  private static final String NAME = "config-files";

  @Override
  public Iterable<? extends GeneratedFile> generate(BuildOptions options) {
    throw new UnsupportedOperationException("ConfigFilesAdapter.generate not implemented yet");
  }

  @Override
  public Iterable<? extends GeneratedFile> generateFiles(ProjectBlueprint blueprint) {
    return generate(blueprint.getBuildOptions());
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
