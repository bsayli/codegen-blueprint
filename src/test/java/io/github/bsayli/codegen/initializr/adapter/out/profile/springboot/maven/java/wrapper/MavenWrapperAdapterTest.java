package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.wrapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.bootstrap.config.TemplateDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import io.github.bsayli.codegen.initializr.testsupport.templating.CapturingTemplateRenderer;
import io.github.bsayli.codegen.initializr.testsupport.templating.NoopTemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
@DisplayName("Unit Test: MavenWrapperAdapter")
class MavenWrapperAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  @Test
  @DisplayName("artifactKey() should return MAVEN_WRAPPER")
  void artifactKey_shouldReturnMavenWrapper() {
    MavenWrapperAdapter adapter =
        new MavenWrapperAdapter(
            new NoopTemplateRenderer(),
            new ArtifactDefinition(
                BASE_PATH,
                List.of(
                    new TemplateDefinition(
                        "maven-wrapper.ftl", ".mvn/wrapper/maven-wrapper.properties"))));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.MAVEN_WRAPPER);
  }

  @Test
  @DisplayName("generate() should build model with default wrapper and maven versions")
  void generate_shouldBuildModelWithDefaultVersions() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateDefinition templateDefinition =
        new TemplateDefinition("maven-wrapper.ftl", ".mvn/wrapper/maven-wrapper.properties");
    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    MavenWrapperAdapter adapter = new MavenWrapperAdapter(renderer, artifactDefinition);

    ProjectBlueprint blueprint = new ProjectBlueprint(null, null, null, null, null, null, null);

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
        .containsEntry("wrapperVersion", "3.3.3")
        .containsEntry("mavenVersion", "3.9.11");
  }
}
