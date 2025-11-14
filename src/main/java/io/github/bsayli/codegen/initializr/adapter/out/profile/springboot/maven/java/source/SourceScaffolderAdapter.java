package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.source;

import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.shared.AbstractJavaClassScaffolderAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.adapter.shared.naming.StringCaseFormatter;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.ArtifactKey;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.SourceScaffolderPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;

public class SourceScaffolderAdapter extends AbstractJavaClassScaffolderAdapter
    implements SourceScaffolderPort {

  public static final String POSTFIX_APPLICATION = "Application";

  public SourceScaffolderAdapter(
      TemplateRenderer renderer,
      ArtifactDefinition artifactDefinition,
      StringCaseFormatter stringCaseFormatter) {
    super(renderer, artifactDefinition, stringCaseFormatter);
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.SOURCE_SCAFFOLDER;
  }

  @Override
  protected String buildClassName(ProjectBlueprint blueprint) {
    ProjectIdentity id = blueprint.getIdentity();
    return pascal(id.artifactId().value()) + POSTFIX_APPLICATION;
  }
}
