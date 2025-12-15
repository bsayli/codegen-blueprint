package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.config;

import static java.util.Map.entry;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.AbstractSingleTemplateArtifactAdapter;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ApplicationConfigurationPort;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import java.util.Map;

public class ApplicationYamlAdapter extends AbstractSingleTemplateArtifactAdapter
    implements ApplicationConfigurationPort {

  private static final String KEY_APP_NAME = "applicationName";

  public ApplicationYamlAdapter(TemplateRenderer renderer, ArtifactSpec artifactSpec) {
    super(renderer, artifactSpec);
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.APP_CONFIG;
  }

  @Override
  protected Map<String, Object> buildModel(ProjectBlueprint blueprint) {
    ProjectIdentity id = blueprint.getMetadata().identity();
      return Map.ofEntries(entry(KEY_APP_NAME, id.artifactId().value()));
  }
}
