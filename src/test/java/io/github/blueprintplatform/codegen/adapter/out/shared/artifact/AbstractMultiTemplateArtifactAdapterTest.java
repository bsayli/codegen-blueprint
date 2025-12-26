package io.github.blueprintplatform.codegen.adapter.out.shared.artifact;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
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
import io.github.blueprintplatform.codegen.testsupport.templating.RecordingTemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class AbstractMultiTemplateArtifactAdapterTest {

  private static final String BASE_PATH = "springboot/java/";

  private static ProjectBlueprint projectBlueprint() {
    ProjectMetadata metadata =
        new ProjectMetadata(
            new ProjectIdentity(
                new GroupId("io.github.blueprintplatform"), new ArtifactId("greeting")),
            new ProjectName("Greeting"),
            new ProjectDescription("Greeting sample service"),
            new PackageName("io.github.blueprintplatform.greeting"));

    PlatformSpec platform =
        new PlatformSpec(
            new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA),
            new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5));

    ArchitectureSpec architecture =
        new ArchitectureSpec(
            ProjectLayout.STANDARD, ArchitectureGovernance.none(), SampleCodeOptions.none());

    return ProjectBlueprint.of(metadata, platform, architecture, Dependencies.of(List.of()));
  }

  @Test
  @DisplayName("generate() should render all templates with same model and return files in order")
  void generate_shouldRenderAllTemplatesAndReturnFilesInOrder() {
    GeneratedResource file1 =
        new GeneratedTextResource(Path.of("out/one.txt"), "1", StandardCharsets.UTF_8);
    GeneratedResource file2 =
        new GeneratedTextResource(Path.of("out/two.txt"), "2", StandardCharsets.UTF_8);
    GeneratedResource file3 =
        new GeneratedTextResource(Path.of("out/three.txt"), "3", StandardCharsets.UTF_8);

    RecordingTemplateRenderer renderer =
        new RecordingTemplateRenderer(List.of(file1, file2, file3));

    ArtifactSpec artifactSpec =
        new ArtifactSpec(
            BASE_PATH,
            List.of(
                new TemplateSpec("one.ftl", "out/one.txt"),
                new TemplateSpec("two.ftl", "out/two.txt"),
                new TemplateSpec("three.ftl", "out/three.txt")));

    TestMultiTemplateAdapter adapter = new TestMultiTemplateAdapter(renderer, artifactSpec);

    Iterable<? extends GeneratedResource> result = adapter.generate(projectBlueprint());

    assertThat(result)
        .extracting(GeneratedResource::relativePath)
        .containsExactly(Path.of("out/one.txt"), Path.of("out/two.txt"), Path.of("out/three.txt"));

    assertThat(renderer.capturedOutPaths)
        .containsExactly(Path.of("out/one.txt"), Path.of("out/two.txt"), Path.of("out/three.txt"));

    assertThat(renderer.capturedTemplateNames)
        .containsExactly(BASE_PATH + "one.ftl", BASE_PATH + "two.ftl", BASE_PATH + "three.ftl");

    assertThat(renderer.capturedModels)
        .allSatisfy(model -> assertThat(model).isEqualTo(Map.of("key", "value")));
  }

  @Test
  @DisplayName("default buildModel() should return empty model and require non-null blueprint")
  void defaultBuildModel_shouldReturnEmptyModelAndRequireNonNull() {
    GeneratedResource file =
        new GeneratedTextResource(Path.of("out/a.txt"), "a", StandardCharsets.UTF_8);

    RecordingTemplateRenderer renderer = new RecordingTemplateRenderer(List.of(file));

    ArtifactSpec artifactSpec =
        new ArtifactSpec(BASE_PATH, List.of(new TemplateSpec("a.ftl", "out/a.txt")));

    AbstractMultiTemplateArtifactAdapter adapter = new DefaultModelAdapter(renderer, artifactSpec);

    adapter.generate(projectBlueprint());

    assertThat(renderer.capturedModels)
        .singleElement()
        .satisfies(model -> assertThat(model).isEmpty());
  }

  private static final class TestMultiTemplateAdapter extends AbstractMultiTemplateArtifactAdapter {

    TestMultiTemplateAdapter(TemplateRenderer renderer, ArtifactSpec artifactSpec) {
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

  private static final class DefaultModelAdapter extends AbstractMultiTemplateArtifactAdapter {

    DefaultModelAdapter(TemplateRenderer renderer, ArtifactSpec artifactSpec) {
      super(renderer, artifactSpec);
    }

    @Override
    public ArtifactKey artifactKey() {
      return null;
    }
  }
}
