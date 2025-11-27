package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.source;

import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.shared.AbstractJavaSourceFileAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.adapter.shared.naming.StringCaseFormatter;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.MainSourceEntrypointPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;

public class MainSourceEntrypointAdapter extends AbstractJavaSourceFileAdapter
    implements MainSourceEntrypointPort {

  public static final String POSTFIX_APPLICATION = "Application";

  public MainSourceEntrypointAdapter(
      TemplateRenderer renderer,
      ArtifactDefinition artifactDefinition,
      StringCaseFormatter stringCaseFormatter) {
    super(renderer, artifactDefinition, stringCaseFormatter);
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.MAIN_SOURCE_ENTRY_POINT;
  }

  @Override
  protected String buildClassName(ProjectBlueprint blueprint) {
    ProjectIdentity id = blueprint.getIdentity();
    return pascal(id.artifactId().value()) + POSTFIX_APPLICATION;
  }
}
