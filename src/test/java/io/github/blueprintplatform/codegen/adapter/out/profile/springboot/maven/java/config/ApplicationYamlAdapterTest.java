package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.config;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.bootstrap.config.ArtifactDefinition;
import io.github.blueprintplatform.codegen.bootstrap.config.TemplateDefinition;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectDescription;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectName;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedFile;
import io.github.blueprintplatform.codegen.testsupport.templating.CapturingTemplateRenderer;
import io.github.blueprintplatform.codegen.testsupport.templating.NoopTemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class ApplicationYamlAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  @Test
  @DisplayName("artifactKey() should return APPLICATION_YAML")
  void artifactKey_shouldReturnApplicationYaml() {
    ApplicationYamlAdapter adapter =
        new ApplicationYamlAdapter(
            new NoopTemplateRenderer(),
            new ArtifactDefinition(
                BASE_PATH,
                List.of(new TemplateDefinition("application-yaml.ftl", "application.yml"))));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.APP_CONFIG);
  }

  @Test
  @DisplayName("generate() should build model with normalized projectName and render single file")
  void generate_shouldBuildModelAndRenderFile() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateDefinition templateDefinition =
        new TemplateDefinition("application-yaml.ftl", "src/main/resources/application.yml");
    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    ApplicationYamlAdapter adapter = new ApplicationYamlAdapter(renderer, artifactDefinition);

    ProjectBlueprint blueprint =
        new ProjectBlueprint(
            null,
            new ProjectName("Demo App"),
            new ProjectDescription("Sample Project"),
            null,
            null,
            null,
            null,
            null);

    Path relativePath = Path.of("src/main/resources/application.yml");
    GeneratedFile.Text expectedFile =
        new GeneratedFile.Text(
            relativePath, "spring.application.name=demo-app", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedFile> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(expectedFile);

    assertThat(renderer.capturedOutPath).isEqualTo(relativePath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "application-yaml.ftl");
    assertThat(renderer.capturedModel).isNotNull().containsEntry("projectName", "demo-app");
  }
}
