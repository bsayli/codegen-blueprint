package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.vcs;

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
@DisplayName("Unit Test: GitIgnoreAdapter")
class GitIgnoreAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  @Test
  @DisplayName("artifactKey() should return GITIGNORE")
  void artifactKey_shouldReturnGitignore() {
    GitIgnoreAdapter adapter =
        new GitIgnoreAdapter(
            new NoopTemplateRenderer(),
            new ArtifactDefinition(
                BASE_PATH, List.of(new TemplateDefinition("gitignore.ftl", ".gitignore"))));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.GITIGNORE);
  }

  @Test
  @DisplayName("generate() should render .gitignore with an empty ignoreList model")
  void generate_shouldRenderGitignoreWithEmptyIgnoreList() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateDefinition templateDefinition = new TemplateDefinition("gitignore.ftl", ".gitignore");
    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    GitIgnoreAdapter adapter = new GitIgnoreAdapter(renderer, artifactDefinition);

    ProjectBlueprint blueprint = new ProjectBlueprint(null, null, null, null, null, null, null);

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
