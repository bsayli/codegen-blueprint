package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.shared;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.adapter.shared.naming.StringCaseFormatter;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.bootstrap.config.ArtifactDefinition;
import io.github.blueprintplatform.codegen.bootstrap.config.TemplateDefinition;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedFile;
import io.github.blueprintplatform.codegen.testsupport.templating.CapturingTemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class AbstractJavaSourceFileAdapterTest {

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

    TestJavaSourceFileAdapter adapter =
        new TestJavaSourceFileAdapter(renderer, artifactDefinition, formatter);

    ProjectBlueprint blueprint =
        new ProjectBlueprint(
            null, null, null, new PackageName("com.acme.demo"), null, null, null, null);

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

  private static final class TestJavaSourceFileAdapter extends AbstractJavaSourceFileAdapter {

    TestJavaSourceFileAdapter(
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
      return ArtifactKey.MAIN_SOURCE_ENTRY_POINT;
    }
  }
}
