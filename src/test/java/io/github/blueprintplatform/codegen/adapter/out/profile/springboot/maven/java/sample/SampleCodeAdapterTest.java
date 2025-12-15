package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.sample;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.shared.SampleCodeLayoutSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.factory.ProjectBlueprintFactory;
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
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeLevel;
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
import io.github.blueprintplatform.codegen.testsupport.templating.NoopTemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class SampleCodeAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";
  private static final String STANDARD_SAMPLES_ROOT = "sample/standard";
  private static final String HEXAGONAL_SAMPLES_ROOT = "sample/hexagonal";
  private static final String BASIC_DIR_NAME = "basic";
  private static final String RICH_DIR_NAME = "rich";

  private static final String BASE_PACKAGE = "com.acme.demo";
  private static final String BASE_PACKAGE_PATH = "com/acme/demo";

  private static final ArtifactSpec DUMMY_ARTIFACT_SPEC = new ArtifactSpec(BASE_PATH, List.of());

  private static final SampleCodeLayoutSpec SAMPLES_CODE_SPEC =
      new SampleCodeLayoutSpec(
          new SampleCodeLayoutSpec.Roots(STANDARD_SAMPLES_ROOT, HEXAGONAL_SAMPLES_ROOT),
          new SampleCodeLayoutSpec.Levels(BASIC_DIR_NAME, RICH_DIR_NAME));

  private static ProjectBlueprint blueprint(ProjectLayout layout, SampleCodeLevel level) {

    ProjectMetadata metadata =
        new ProjectMetadata(
            new ProjectIdentity(new GroupId("com.acme"), new ArtifactId("demo-app")),
            new ProjectName("Demo App"),
            new ProjectDescription("Sample Project"),
            new PackageName(BASE_PACKAGE));

    PlatformSpec platform =
        new PlatformSpec(
            new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA),
            new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5));

    ArchitectureSpec architecture =
        new ArchitectureSpec(layout, ArchitectureGovernance.none(), new SampleCodeOptions(level));

    Dependencies dependencies = Dependencies.of(List.of());

    return ProjectBlueprintFactory.of(metadata, platform, architecture, dependencies);
  }

  private static List<Path> toRelativePaths(Iterable<? extends GeneratedResource> resources) {
    List<Path> result = new ArrayList<>();
    StreamSupport.stream(resources.spliterator(), false).forEach(r -> result.add(r.relativePath()));
    return result;
  }

  @Test
  @DisplayName("artifactKey() should return SAMPLE_CODE")
  void artifactKey_shouldReturnSampleCode() {
    SampleCodeAdapter adapter =
        new SampleCodeAdapter(new NoopTemplateRenderer(), DUMMY_ARTIFACT_SPEC, SAMPLES_CODE_SPEC);

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.SAMPLE_CODE);
  }

  @Test
  @DisplayName("generate() should return empty when sample level is NONE")
  void generate_noneLevel_shouldReturnEmpty() {
    SampleCodeAdapter adapter =
        new SampleCodeAdapter(new NoopTemplateRenderer(), DUMMY_ARTIFACT_SPEC, SAMPLES_CODE_SPEC);

    ProjectBlueprint blueprint = blueprint(ProjectLayout.HEXAGONAL, SampleCodeLevel.NONE);

    Iterable<? extends GeneratedResource> resources = adapter.generate(blueprint);

    assertThat(resources).isEmpty();
  }

  @Test
  @DisplayName("generate() should return empty when layout is STANDARD even if level is BASIC")
  void generate_standardLayoutBasicLevel_shouldReturnEmpty() {
    SampleCodeAdapter adapter =
        new SampleCodeAdapter(new NoopTemplateRenderer(), DUMMY_ARTIFACT_SPEC, SAMPLES_CODE_SPEC);

    ProjectBlueprint blueprint = blueprint(ProjectLayout.STANDARD, SampleCodeLevel.BASIC);

    Iterable<? extends GeneratedResource> resources = adapter.generate(blueprint);

    assertThat(resources).isEmpty();
  }

  @Test
  @DisplayName("generate() should resolve hexagonal BASIC templates and map them to Java sources")
  void generate_hexagonalBasic_shouldGenerateSampleSources() {
    RecordingTemplateRenderer renderer = new RecordingTemplateRenderer();

    SampleCodeAdapter adapter =
        new SampleCodeAdapter(renderer, DUMMY_ARTIFACT_SPEC, SAMPLES_CODE_SPEC);

    ProjectBlueprint blueprint = blueprint(ProjectLayout.HEXAGONAL, SampleCodeLevel.BASIC);

    Iterable<? extends GeneratedResource> resources = adapter.generate(blueprint);
    List<Path> paths = toRelativePaths(resources);

    assertThat(paths).isNotEmpty();

    Path expectedGreetingControllerPath =
        Path.of("src/main/java")
            .resolve(BASE_PACKAGE_PATH)
            .resolve("adapter/sample/greeting/in/rest/GreetingController.java");

    assertThat(paths).contains(expectedGreetingControllerPath);

    String expectedTemplateName =
        "springboot/maven/java/sample/hexagonal/basic/adapter/sample/greeting/in/rest/GreetingController.java.ftl";

    assertThat(renderer.capturedTemplateNames).contains(expectedTemplateName);

    assertThat(renderer.capturedModels)
        .allMatch(model -> BASE_PACKAGE.equals(model.get("projectPackageName")));
  }

  private static final class RecordingTemplateRenderer implements TemplateRenderer {

    private final List<String> capturedTemplateNames = new ArrayList<>();
    private final List<Map<String, Object>> capturedModels = new ArrayList<>();

    @Override
    public GeneratedResource renderUtf8(
        Path outPath, String templateName, Map<String, Object> model) {
      capturedTemplateNames.add(templateName);
      capturedModels.add(model);
      return new GeneratedTextResource(outPath, "", StandardCharsets.UTF_8);
    }
  }
}
