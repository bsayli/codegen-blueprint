package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.wrapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.TemplateSpec;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedTextResource;
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
            new ArtifactSpec(
                BASE_PATH,
                List.of(
                    new TemplateSpec(
                        "maven-wrapper.ftl", ".mvn/wrapper/maven-wrapper.properties"))));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.BUILD_TOOL_METADATA);
  }

  @Test
  @DisplayName("generate() should build model with default wrapper and maven versions")
  void generate_shouldBuildModelWithDefaultVersions() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateSpec templateSpec =
        new TemplateSpec("maven-wrapper.ftl", ".mvn/wrapper/maven-wrapper.properties");
    ArtifactSpec artifactSpec = new ArtifactSpec(BASE_PATH, List.of(templateSpec));

    MavenWrapperBuildToolFilesAdapter adapter =
        new MavenWrapperBuildToolFilesAdapter(renderer, artifactSpec);

    ProjectBlueprint blueprint =
        new ProjectBlueprint(
            null,
            null,
            new ArchitectureSpec(null, ArchitectureGovernance.none(), SampleCodeOptions.none()),
            Dependencies.of(List.of()));

    Path relativePath = Path.of(".mvn/wrapper/maven-wrapper.properties");
    GeneratedTextResource expectedFile =
        new GeneratedTextResource(relativePath, "distributionUrl=...", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedResource> result = adapter.generate(blueprint);

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
