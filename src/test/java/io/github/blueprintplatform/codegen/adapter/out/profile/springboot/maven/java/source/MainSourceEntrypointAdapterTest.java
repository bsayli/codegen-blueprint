package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.source;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.shared.naming.StringCaseFormatter;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.bootstrap.config.ArtifactDefinition;
import io.github.blueprintplatform.codegen.bootstrap.config.TemplateDefinition;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ArtifactId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.GroupId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectDescription;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectName;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
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
class MainSourceEntrypointAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  private static ProjectBlueprint blueprint() {
    ProjectIdentity identity =
        new ProjectIdentity(new GroupId("com.acme"), new ArtifactId("demo-app"));

    ProjectName name = new ProjectName("Demo App");
    ProjectDescription description = new ProjectDescription("Sample Project");
    PackageName pkg = new PackageName("com.acme.demo");

    TechStack techStack = new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);
    ProjectLayout layout = ProjectLayout.STANDARD;
    PlatformTarget platformTarget =
        new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5);
    Dependencies dependencies = Dependencies.of(List.of());

    return new ProjectBlueprint(
        identity, name, description, pkg, techStack, layout, platformTarget, dependencies);
  }

  @Test
  @DisplayName("artifactKey() should return MAIN_SOURCE_ENTRY_POINT")
  void artifactKey_shouldReturnMainSourceEntrypoint() {
    MainSourceEntrypointAdapter adapter =
        new MainSourceEntrypointAdapter(
            new NoopTemplateRenderer(),
            new ArtifactDefinition(
                BASE_PATH, List.of(new TemplateDefinition("source.ftl", "src/main/java"))),
            new StringCaseFormatter());

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.MAIN_SOURCE_ENTRY_POINT);
  }

  @Test
  @DisplayName(
      "generate() should build class name from artifactId (PascalCase + Application) and render file under package path")
  void generate_shouldBuildClassNameFromArtifactIdAndRenderFile() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateDefinition templateDefinition = new TemplateDefinition("source.ftl", "src/main/java");
    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    MainSourceEntrypointAdapter adapter =
        new MainSourceEntrypointAdapter(renderer, artifactDefinition, new StringCaseFormatter());

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
