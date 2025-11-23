package io.github.bsayli.codegen.initializr.adapter.out.shared.artifact;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.bootstrap.config.TemplateDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import io.github.bsayli.codegen.initializr.testsupport.templating.CapturingTemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
@DisplayName("Unit Test: AbstractSingleTemplateArtifactAdapter")
class AbstractSingleTemplateArtifactAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  @Test
  @DisplayName("generate() should use first template, render with model, and return single file")
  void generate_shouldRenderSingleTemplateAndReturnFile() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateDefinition templateDefinition =
        new TemplateDefinition("test-template.ftl", "output/test.txt");

    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    TestSingleTemplateAdapter adapter = new TestSingleTemplateAdapter(renderer, artifactDefinition);

    ProjectBlueprint blueprint = new ProjectBlueprint(null, null, null, null, null, null, null);

    Path relativePath = Path.of("output/test.txt");
    GeneratedFile.Text expectedFile =
        new GeneratedFile.Text(relativePath, "rendered-content", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedFile> result = adapter.generate(blueprint);

    assertThat(renderer.capturedOutPath).isEqualTo(relativePath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "test-template.ftl");
    assertThat(renderer.capturedModel).isEqualTo(Map.of("key", "value"));

    assertThat(result).singleElement().isSameAs(expectedFile);
  }

  private static final class TestSingleTemplateAdapter
      extends AbstractSingleTemplateArtifactAdapter {

    TestSingleTemplateAdapter(TemplateRenderer renderer, ArtifactDefinition artifactDefinition) {
      super(renderer, artifactDefinition);
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
