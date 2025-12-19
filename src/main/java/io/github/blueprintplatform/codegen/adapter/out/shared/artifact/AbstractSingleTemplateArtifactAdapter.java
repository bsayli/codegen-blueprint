package io.github.blueprintplatform.codegen.adapter.out.shared.artifact;

import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class AbstractSingleTemplateArtifactAdapter implements ArtifactPort {

  private final TemplateRenderer renderer;
  private final ArtifactSpec artifactSpec;

  protected AbstractSingleTemplateArtifactAdapter(
      TemplateRenderer renderer, ArtifactSpec artifactSpec) {
    this.renderer = renderer;
    this.artifactSpec = artifactSpec;
  }

  @Override
  public final Iterable<? extends GeneratedResource> generate(ProjectBlueprint blueprint) {
    TemplateSpec templateSpec = artifactSpec.templates().getFirst();

    Path outPath = Path.of(templateSpec.outputPath());
    String templateResourcePath = artifactSpec.basePath() + templateSpec.template();

    Map<String, Object> model = buildModel(blueprint);
    GeneratedResource file = renderer.renderUtf8(outPath, templateResourcePath, model);

    return List.of(file);
  }

  protected abstract Map<String, Object> buildModel(
      @SuppressWarnings("unused") ProjectBlueprint blueprint);
}
