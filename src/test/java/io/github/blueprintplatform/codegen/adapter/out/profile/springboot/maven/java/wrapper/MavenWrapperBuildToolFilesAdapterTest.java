package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.wrapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.bootstrap.config.ArtifactDefinition;
import io.github.blueprintplatform.codegen.bootstrap.config.TemplateDefinition;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedFile;
import io.github.blueprintplatform.codegen.testsupport.templating.CapturingTemplateRenderer;
import io.github.blueprintplatform.codegen.testsupport.templating.NoopTemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class MavenWrapperBuildToolFilesAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  @Test
  @DisplayName("artifactKey() should return BUILD_TOOL_METADATA")
  void artifactKey_shouldReturnBuildToolMetadata() {
    MavenWrapperBuildToolFilesAdapter adapter =
        new MavenWrapperBuildToolFilesAdapter(
            new NoopTemplateRenderer(),
            new ArtifactDefinition(
                BASE_PATH,
                List.of(
                    new TemplateDefinition(
                        "maven-wrapper.ftl", ".mvn/wrapper/maven-wrapper.properties"))));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.BUILD_TOOL_METADATA);
  }

  @Test
  @DisplayName("generate() should build model with default wrapper and maven versions")
  void generate_shouldBuildModelWithDefaultVersions() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateDefinition templateDefinition =
        new TemplateDefinition("maven-wrapper.ftl", ".mvn/wrapper/maven-wrapper.properties");
    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    MavenWrapperBuildToolFilesAdapter adapter =
        new MavenWrapperBuildToolFilesAdapter(renderer, artifactDefinition);

    ProjectBlueprint blueprint =
        new ProjectBlueprint(null, null, null, null, null, null, null, null);

    Path relativePath = Path.of(".mvn/wrapper/maven-wrapper.properties");
    GeneratedFile.Text expectedFile =
        new GeneratedFile.Text(relativePath, "distributionUrl=...", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedFile> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(expectedFile);

    assertThat(renderer.capturedOutPath).isEqualTo(relativePath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "maven-wrapper.ftl");

    Map<String, Object> model = renderer.capturedModel;
    assertThat(model)
        .isNotNull()
        .containsEntry("wrapperVersion", "3.3.4")
        .containsEntry("mavenVersion", "3.9.11");
  }
}
