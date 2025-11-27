package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.test;

import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.shared.AbstractJavaSourceFileAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.adapter.shared.naming.StringCaseFormatter;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.TestSourceEntrypointPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;

public class TestSourceEntrypointAdapter extends AbstractJavaSourceFileAdapter
    implements TestSourceEntrypointPort {

  public static final String POSTFIX_APPLICATION_TESTS = "ApplicationTests";

  public TestSourceEntrypointAdapter(
      TemplateRenderer renderer,
      ArtifactDefinition artifactDefinition,
      StringCaseFormatter stringCaseFormatter) {
    super(renderer, artifactDefinition, stringCaseFormatter);
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.TEST_SOURCE_ENTRY_POINT;
  }

  @Override
  protected String buildClassName(ProjectBlueprint blueprint) {
    ProjectIdentity id = blueprint.getIdentity();
    return pascal(id.artifactId().value()) + POSTFIX_APPLICATION_TESTS;
  }
}
