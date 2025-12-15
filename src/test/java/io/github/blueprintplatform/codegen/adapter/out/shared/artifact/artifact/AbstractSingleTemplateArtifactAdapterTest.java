package io.github.blueprintplatform.codegen.adapter.out.shared.artifact.artifact;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.AbstractSingleTemplateArtifactAdapter;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.TemplateSpec;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedTextResource;
import io.github.blueprintplatform.codegen.testsupport.templating.CapturingTemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class AbstractSingleTemplateArtifactAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  @Test
  @DisplayName("generate() should use first template, render with model, and return single file")
  void generate_shouldRenderSingleTemplateAndReturnFile() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateSpec templateSpec = new TemplateSpec("test-template.ftl", "output/test.txt");

    ArtifactSpec artifactSpec = new ArtifactSpec(BASE_PATH, List.of(templateSpec));

    TestSingleTemplateAdapter adapter = new TestSingleTemplateAdapter(renderer, artifactSpec);

    ProjectBlueprint blueprint =
        new ProjectBlueprint(
            null,
            null,
            new ArchitectureSpec(null, ArchitectureGovernance.none(), SampleCodeOptions.none()),
            Dependencies.of(List.of()));

    Path relativePath = Path.of("output/test.txt");
    GeneratedTextResource expectedFile =
        new GeneratedTextResource(relativePath, "rendered-content", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedResource> result = adapter.generate(blueprint);

    assertThat(renderer.capturedOutPath).isEqualTo(relativePath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "test-template.ftl");
    assertThat(renderer.capturedModel).isEqualTo(Map.of("key", "value"));

    assertThat(result).singleElement().isSameAs(expectedFile);
  }

  private static final class TestSingleTemplateAdapter
      extends AbstractSingleTemplateArtifactAdapter {

    TestSingleTemplateAdapter(TemplateRenderer renderer, ArtifactSpec artifactSpec) {
      super(renderer, artifactSpec);
    }

    @Override
    protected Map<String, Object> buildModel(ProjectBlueprint blueprint) {
      return Map.of("key", "value");
    }

    @Override
    public ArtifactKey artifactKey() {
      return null;
    }
  }
}
