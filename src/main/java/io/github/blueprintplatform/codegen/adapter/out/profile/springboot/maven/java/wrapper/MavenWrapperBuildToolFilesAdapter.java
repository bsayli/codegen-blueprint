package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.wrapper;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.AbstractMultiTemplateArtifactAdapter;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.BuildToolFilesPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import java.util.Map;

public class MavenWrapperBuildToolFilesAdapter extends AbstractMultiTemplateArtifactAdapter
    implements BuildToolFilesPort {

  public MavenWrapperBuildToolFilesAdapter(TemplateRenderer renderer, ArtifactSpec artifactSpec) {
    super(renderer, artifactSpec);
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.BUILD_TOOL_FILES;
  }

  @Override
  protected Map<String, Object> buildModel(@SuppressWarnings("unused") ProjectBlueprint blueprint) {
    return Map.of();
  }
}
