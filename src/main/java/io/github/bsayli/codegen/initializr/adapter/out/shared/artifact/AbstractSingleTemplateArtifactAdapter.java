package io.github.bsayli.codegen.initializr.adapter.out.shared.artifact;

import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.bootstrap.config.TemplateDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class AbstractSingleTemplateArtifactAdapter implements ArtifactPort {

  private final TemplateRenderer renderer;
  private final ArtifactDefinition artifactDefinition;

  protected AbstractSingleTemplateArtifactAdapter(
          TemplateRenderer renderer, ArtifactDefinition artifactDefinition) {
    this.renderer = renderer;
    this.artifactDefinition = artifactDefinition;
  }

  @Override
  public final Iterable<? extends GeneratedFile> generate(ProjectBlueprint blueprint) {
    TemplateDefinition templateDefinition = artifactDefinition.templates().getFirst();

    Path outPath = Path.of(templateDefinition.outputPath());
    String templateName = artifactDefinition.basePath() + templateDefinition.template();

    Map<String, Object> model = buildModel(blueprint);
    GeneratedFile file = renderer.renderUtf8(outPath, templateName, model);

    return List.of(file);
  }

  protected abstract Map<String, Object> buildModel(ProjectBlueprint blueprint);
}