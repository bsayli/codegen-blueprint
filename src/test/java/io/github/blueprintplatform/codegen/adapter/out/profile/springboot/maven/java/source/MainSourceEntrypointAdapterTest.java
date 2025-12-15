package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.source;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.TemplateSpec;
import io.github.blueprintplatform.codegen.adapter.shared.naming.StringCaseFormatter;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ArtifactId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.GroupId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.metadata.ProjectMetadata;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectDescription;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectName;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;
import io.github.blueprintplatform.codegen.domain.model.value.tech.PlatformSpec;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
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
class MainSourceEntrypointAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  private static ProjectBlueprint blueprint() {
    ProjectMetadata metadata =
            new ProjectMetadata(
                    new ProjectIdentity(new GroupId("com.acme"), new ArtifactId("demo-app")),
                    new ProjectName("Demo App"),
                    new ProjectDescription("Sample Project"),
                    new PackageName("com.acme.demo"));

    PlatformSpec platform =
            new PlatformSpec(
                    new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA),
                    new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5));

    ArchitectureSpec architecture =
            new ArchitectureSpec(
                    ProjectLayout.STANDARD,
                    ArchitectureGovernance.none(),
                    SampleCodeOptions.none());

    Dependencies dependencies = Dependencies.of(List.of());

    return new ProjectBlueprint(metadata, platform, architecture, dependencies);
  }

  @Test
  @DisplayName("artifactKey() should return MAIN_SOURCE_ENTRY_POINT")
  void artifactKey_shouldReturnMainSourceEntrypoint() {
    MainSourceEntrypointAdapter adapter =
        new MainSourceEntrypointAdapter(
            new NoopTemplateRenderer(),
            new ArtifactSpec(BASE_PATH, List.of(new TemplateSpec("source.ftl", "src/main/java"))),
            new StringCaseFormatter());

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.MAIN_SOURCE_ENTRY_POINT);
  }

  @Test
  @DisplayName(
      "generate() should build class name from artifactId (PascalCase + Application) and render file under package path")
  void generate_shouldBuildClassNameFromArtifactIdAndRenderFile() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateSpec templateSpec = new TemplateSpec("source.ftl", "src/main/java");
    ArtifactSpec artifactSpec = new ArtifactSpec(BASE_PATH, List.of(templateSpec));

    MainSourceEntrypointAdapter adapter =
        new MainSourceEntrypointAdapter(renderer, artifactSpec, new StringCaseFormatter());

    ProjectBlueprint blueprint = blueprint();

    Path expectedPath = Path.of("src/main/java/com/acme/demo/DemoAppApplication.java");

    GeneratedTextResource expectedFile =
        new GeneratedTextResource(
            expectedPath, "class DemoAppApplication {}", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedResource> result = adapter.generate(blueprint);

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
