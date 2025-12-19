package io.github.blueprintplatform.codegen.adapter.out.shared.artifact;

import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractMultiTemplateArtifactAdapter implements ArtifactPort {

  private final TemplateRenderer renderer;
  private final ArtifactSpec artifactSpec;

  protected AbstractMultiTemplateArtifactAdapter(
      TemplateRenderer renderer, ArtifactSpec artifactSpec) {
    this.renderer = renderer;
    this.artifactSpec = artifactSpec;
  }

  @Override
  public final Iterable<? extends GeneratedResource> generate(ProjectBlueprint blueprint) {
    List<TemplateSpec> templates = artifactSpec.templates();

    Map<String, Object> model = buildModel(blueprint);

    List<GeneratedResource> files = new ArrayList<>(templates.size());
    for (TemplateSpec templateSpec : templates) {
      Path outPath = Path.of(templateSpec.outputPath());
      String templateResourcePath = artifactSpec.basePath() + templateSpec.template();
      files.add(renderer.renderUtf8(outPath, templateResourcePath, model));
    }

    return List.copyOf(files);
  }

  protected Map<String, Object> buildModel(ProjectBlueprint blueprint) {
    Objects.requireNonNull(blueprint, "blueprint");
    return Map.of();
  }
}
