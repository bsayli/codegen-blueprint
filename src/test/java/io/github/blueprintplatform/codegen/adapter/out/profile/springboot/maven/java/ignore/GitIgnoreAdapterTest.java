package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.ignore;

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
class GitIgnoreAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  @Test
  @DisplayName("artifactKey() should return IGNORE_RULES")
  void artifactKey_shouldReturnIgnoreRules() {
    GitIgnoreAdapter adapter =
        new GitIgnoreAdapter(
            new NoopTemplateRenderer(),
            new ArtifactDefinition(
                BASE_PATH, List.of(new TemplateDefinition("gitignore.ftl", ".gitignore"))));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.IGNORE_RULES);
  }

  @Test
  @DisplayName("generate() should render ignore rules with an empty ignoreList model")
  void generate_shouldRenderGitignoreWithEmptyIgnoreList() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateDefinition templateDefinition = new TemplateDefinition("gitignore.ftl", ".gitignore");
    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    GitIgnoreAdapter adapter = new GitIgnoreAdapter(renderer, artifactDefinition);

    ProjectBlueprint blueprint =
        new ProjectBlueprint(null, null, null, null, null, null, null, null);

    Path relativePath = Path.of(".gitignore");
    GeneratedFile.Text expectedFile =
        new GeneratedFile.Text(relativePath, "# gitignore", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedFile> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(expectedFile);

    assertThat(renderer.capturedOutPath).isEqualTo(relativePath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "gitignore.ftl");

    Map<String, Object> model = renderer.capturedModel;
    assertThat(model).isNotNull().containsEntry("ignoreList", List.of());
  }
}
