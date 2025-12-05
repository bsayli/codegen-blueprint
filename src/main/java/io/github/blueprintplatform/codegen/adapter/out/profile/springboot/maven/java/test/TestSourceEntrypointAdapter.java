package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.test;

import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.shared.AbstractJavaSourceFileAdapter;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.adapter.shared.naming.StringCaseFormatter;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.TestSourceEntrypointPort;
import io.github.blueprintplatform.codegen.bootstrap.config.ArtifactDefinition;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;

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
