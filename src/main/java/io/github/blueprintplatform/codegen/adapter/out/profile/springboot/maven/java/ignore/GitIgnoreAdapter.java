package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.ignore;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.AbstractSingleTemplateArtifactAdapter;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.IgnoreRulesPort;
import io.github.blueprintplatform.codegen.bootstrap.config.ArtifactDefinition;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import java.util.List;
import java.util.Map;

public class GitIgnoreAdapter extends AbstractSingleTemplateArtifactAdapter
    implements IgnoreRulesPort {

  private static final String KEY_IGNORE_LIST = "ignoreList";

  public GitIgnoreAdapter(TemplateRenderer renderer, ArtifactDefinition artifactDefinition) {
    super(renderer, artifactDefinition);
  }

  @Override
  protected Map<String, Object> buildModel(ProjectBlueprint blueprint) {
    return Map.of(KEY_IGNORE_LIST, List.of());
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.IGNORE_RULES;
  }
}
