package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.test;

import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.shared.AbstractJavaClassScaffolderAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.adapter.shared.naming.StringCaseFormatter;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.ArtifactKey;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.TestScaffolderPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;

public class TestScaffolderAdapter extends AbstractJavaClassScaffolderAdapter
    implements TestScaffolderPort {

  public static final String POSTFIX_APPLICATION_TESTS = "ApplicationTests";

  public TestScaffolderAdapter(
      TemplateRenderer renderer,
      ArtifactDefinition artifactDefinition,
      StringCaseFormatter stringCaseFormatter) {
    super(renderer, artifactDefinition, stringCaseFormatter);
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.TEST_SCAFFOLDER;
  }

  @Override
  protected String buildClassName(ProjectBlueprint blueprint) {
    ProjectIdentity id = blueprint.getIdentity();
    return pascal(id.artifactId().value()) + POSTFIX_APPLICATION_TESTS;
  }
}
