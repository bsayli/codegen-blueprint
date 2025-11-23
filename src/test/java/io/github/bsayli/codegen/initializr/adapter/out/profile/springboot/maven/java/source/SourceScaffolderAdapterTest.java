package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.source;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bsayli.codegen.initializr.adapter.shared.naming.StringCaseFormatter;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.bootstrap.config.TemplateDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ArtifactId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.GroupId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectDescription;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectName;
import io.github.bsayli.codegen.initializr.domain.model.value.pkg.PackageName;
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
@DisplayName("Unit Test: SourceScaffolderAdapter")
class SourceScaffolderAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  private static ProjectBlueprint blueprint() {
    ProjectIdentity identity =
        new ProjectIdentity(new GroupId("com.acme"), new ArtifactId("demo-app"));

    ProjectName name = new ProjectName("Demo App");
    ProjectDescription description = new ProjectDescription("Sample Project");
    PackageName pkg = new PackageName("com.acme.demo");

    return new ProjectBlueprint(identity, name, description, pkg, null, null, null);
  }

  @Test
  @DisplayName("artifactKey() should return SOURCE_SCAFFOLDER")
  void artifactKey_shouldReturnSourceScaffolder() {
    SourceScaffolderAdapter adapter =
        new SourceScaffolderAdapter(
            new NoopTemplateRenderer(),
            new ArtifactDefinition(
                BASE_PATH, List.of(new TemplateDefinition("source.ftl", "src/main/java"))),
            new StringCaseFormatter());

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.SOURCE_SCAFFOLDER);
  }

  @Test
  @DisplayName(
      "generate() should build class name from artifactId (PascalCase + Application) and render file under package path")
  void generate_shouldBuildClassNameFromArtifactIdAndRenderFile() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateDefinition templateDefinition = new TemplateDefinition("source.ftl", "src/main/java");
    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    SourceScaffolderAdapter adapter =
        new SourceScaffolderAdapter(renderer, artifactDefinition, new StringCaseFormatter());

    ProjectBlueprint blueprint = blueprint();

    Path expectedPath = Path.of("src/main/java/com/acme/demo/DemoAppApplication.java");

    GeneratedFile.Text expectedFile =
        new GeneratedFile.Text(expectedPath, "class DemoAppApplication {}", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedFile> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(expectedFile);

    assertThat(renderer.capturedOutPath).isEqualTo(expectedPath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "source.ftl");

    Map<String, Object> model = renderer.capturedModel;
    assertThat(model)
        .isNotNull()
        .containsEntry("projectPackageName", "com.acme.demo")
        .containsEntry("className", "DemoAppApplication");
  }
}
