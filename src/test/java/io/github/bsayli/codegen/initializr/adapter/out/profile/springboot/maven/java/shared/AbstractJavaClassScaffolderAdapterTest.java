package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.shared;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.adapter.shared.naming.StringCaseFormatter;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.bootstrap.config.TemplateDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.pkg.PackageName;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import io.github.bsayli.codegen.initializr.testsupport.templating.CapturingTemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
@DisplayName("Unit Test: AbstractJavaClassScaffolderAdapter")
class AbstractJavaClassScaffolderAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  @Test
  @DisplayName("generate() should build correct path, model and return single file")
  void generate_shouldBuildOutPathAndModelAndReturnFile() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateDefinition templateDefinition =
        new TemplateDefinition("java-class.ftl", "src/main/java");

    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    StringCaseFormatter formatter = new StringCaseFormatter();

    TestJavaClassScaffolderAdapter adapter =
        new TestJavaClassScaffolderAdapter(renderer, artifactDefinition, formatter);

    ProjectBlueprint blueprint =
        new ProjectBlueprint(null, null, null, new PackageName("com.acme.demo"), null, null, null);

    Path expectedPath = Path.of("src/main/java/com/acme/demo/DemoApplication.java");

    GeneratedFile.Text expectedFile =
        new GeneratedFile.Text(expectedPath, "class DemoApplication {}", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedFile> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(expectedFile);

    assertThat(renderer.capturedOutPath).isEqualTo(expectedPath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "java-class.ftl");

    assertThat(renderer.capturedModel)
        .isNotNull()
        .containsEntry("projectPackageName", "com.acme.demo")
        .containsEntry("className", "DemoApplication");
  }

  private static final class TestJavaClassScaffolderAdapter
      extends AbstractJavaClassScaffolderAdapter {

    TestJavaClassScaffolderAdapter(
        TemplateRenderer renderer,
        ArtifactDefinition artifactDefinition,
        StringCaseFormatter stringCaseFormatter) {
      super(renderer, artifactDefinition, stringCaseFormatter);
    }

    @Override
    protected String buildClassName(ProjectBlueprint blueprint) {
      return "DemoApplication";
    }

    @Override
    public ArtifactKey artifactKey() {
      return ArtifactKey.SOURCE_SCAFFOLDER;
    }
  }
}
