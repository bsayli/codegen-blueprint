package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.ignore;

import io.github.bsayli.codegen.initializr.adapter.out.shared.artifact.AbstractSingleTemplateArtifactAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.IgnoreRulesPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
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
