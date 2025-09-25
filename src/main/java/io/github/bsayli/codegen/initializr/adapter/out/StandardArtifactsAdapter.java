package io.github.bsayli.codegen.initializr.adapter.out;

import io.github.bsayli.codegen.initializr.adapter.out.generator.ArtifactGenerator;
import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StandardArtifactsAdapter implements ProjectArtifactsPort {

  private final List<ArtifactGenerator> artifactGenerators;

  public StandardArtifactsAdapter(List<ArtifactGenerator> artifactGenerators) {
    this.artifactGenerators = artifactGenerators;
  }

  @Override
  public Iterable<? extends GeneratedFile> generate(ProjectBlueprint blueprint) {
    List<GeneratedFile> generatedFiles = new ArrayList<>();
    artifactGenerators.stream()
        .filter(g -> g.supports(blueprint))
        .sorted(Comparator.comparingInt(ArtifactGenerator::order))
        .forEach(g -> g.generateFiles(blueprint).forEach(generatedFiles::add));
    return generatedFiles;
  }
}
