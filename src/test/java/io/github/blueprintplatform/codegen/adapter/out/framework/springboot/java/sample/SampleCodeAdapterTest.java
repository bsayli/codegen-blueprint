package io.github.blueprintplatform.codegen.adapter.out.framework.springboot.java.sample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.blueprintplatform.codegen.adapter.error.exception.sample.SampleCodeTemplatesNotFoundException;
import io.github.blueprintplatform.codegen.adapter.error.exception.templating.SampleCodeTemplatesScanException;
import io.github.blueprintplatform.codegen.adapter.error.exception.templating.TemplateScanException;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.templating.ClasspathTemplateScanner;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class SampleCodeAdapterTest {

  private static final String BASE_PATH = "springboot/java/";
  private static final String BASE_PACKAGE = "com.acme.demo";
  private static final String BASE_PACKAGE_PATH = "com/acme/demo";

  private static final ArtifactSpec DUMMY_ARTIFACT_SPEC = new ArtifactSpec(BASE_PATH, List.of());

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

    return ProjectBlueprint.of(metadata, platform, architecture, Dependencies.of(List.of()));
  }

  private static List<Path> resourcesToPaths(Iterable<? extends GeneratedResource> resources) {
    List<Path> out = new ArrayList<>();
    for (var r : resources) {
      out.add(r.relativePath());
    }
    return out;
  }

  @Test
  @DisplayName("artifactKey() should return SAMPLE_CODE")
  void artifactKey_shouldReturnSampleCode() {
    SampleCodeAdapter adapter =
        new SampleCodeAdapter(
            new RecordingTemplateRenderer(),
            DUMMY_ARTIFACT_SPEC,
            new StubClasspathTemplateScanner(List.of()));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.SAMPLE_CODE);
  }

  @Test
  @DisplayName("generate() should return empty when sample code level is not BASIC")
  void generate_nonBasicLevel_shouldReturnEmpty() {
    SampleCodeAdapter adapter =
        new SampleCodeAdapter(
            new RecordingTemplateRenderer(),
            DUMMY_ARTIFACT_SPEC,
            new StubClasspathTemplateScanner(
                List.of("springboot/java/sample/hexagonal/basic/main/x.java.ftl")));

    ProjectBlueprint bp = blueprint(ProjectLayout.HEXAGONAL, SampleCodeLevel.NONE);

    assertThat(adapter.generate(bp)).isEmpty();
  }

  @Test
  @DisplayName(
      "generate() should throw SampleCodeTemplatesNotFoundException when scan returns empty")
  void generate_hexagonalBasic_shouldThrowWhenNoTemplatesFound() {
    String templateRoot = "springboot/java/sample/hexagonal/basic";

    StubClasspathTemplateScanner scanner = new StubClasspathTemplateScanner(List.of());

    SampleCodeAdapter adapter =
        new SampleCodeAdapter(new RecordingTemplateRenderer(), DUMMY_ARTIFACT_SPEC, scanner);

    ProjectBlueprint bp = blueprint(ProjectLayout.HEXAGONAL, SampleCodeLevel.BASIC);

    assertThatThrownBy(() -> adapter.generate(bp))
        .isInstanceOf(SampleCodeTemplatesNotFoundException.class);

    assertThat(scanner.lastRoot).isEqualTo(templateRoot);
  }

  @Test
  @DisplayName("generate() should wrap TemplateScanException as SampleCodeTemplatesScanException")
  void generate_hexagonalBasic_shouldWrapTemplateScanException() {
    SampleCodeAdapter adapter =
        new SampleCodeAdapter(
            new RecordingTemplateRenderer(),
            DUMMY_ARTIFACT_SPEC,
            new ThrowingClasspathTemplateScanner());

    ProjectBlueprint bp = blueprint(ProjectLayout.HEXAGONAL, SampleCodeLevel.BASIC);

    assertThatThrownBy(() -> adapter.generate(bp))
        .isInstanceOf(SampleCodeTemplatesScanException.class);
  }

  @Test
  @DisplayName(
      "generate() should render main/test java templates into src/main/java and src/test/java")
  void generate_hexagonalBasic_shouldRenderMainAndTestSources() {
    ArtifactSpec artifactSpec = new ArtifactSpec("springboot/java/", List.of());
    String templateRoot = "springboot/java/sample/hexagonal/basic";

    String mainTemplate = templateRoot + "/main/adapter/sample/in/rest/GreetingController.java.ftl";
    String testTemplate =
        templateRoot + "/test/adapter/sample/in/rest/GreetingControllerTest.java.ftl";
    String ignoredTemplate = templateRoot + "/main/adapter/sample/in/rest/ignored.txt.ftl";

    RecordingTemplateRenderer renderer = new RecordingTemplateRenderer();
    StubClasspathTemplateScanner scanner =
        new StubClasspathTemplateScanner(List.of(mainTemplate, testTemplate, ignoredTemplate));

    SampleCodeAdapter adapter = new SampleCodeAdapter(renderer, artifactSpec, scanner);

    ProjectBlueprint bp = blueprint(ProjectLayout.HEXAGONAL, SampleCodeLevel.BASIC);

    var resources = adapter.generate(bp);

    Path expectedMain =
        Path.of("src/main/java")
            .resolve(BASE_PACKAGE_PATH)
            .resolve("adapter/sample/in/rest/GreetingController.java");

    Path expectedTest =
        Path.of("src/test/java")
            .resolve(BASE_PACKAGE_PATH)
            .resolve("adapter/sample/in/rest/GreetingControllerTest.java");

    List<Path> outPaths = resourcesToPaths(resources);

    assertThat(outPaths).containsExactlyInAnyOrder(expectedMain, expectedTest);

    assertThat(renderer.capturedTemplateNames)
        .containsExactlyInAnyOrder(mainTemplate, testTemplate);
    assertThat(renderer.capturedModels)
        .containsExactlyInAnyOrder(
            Map.of("projectPackageName", BASE_PACKAGE), Map.of("projectPackageName", BASE_PACKAGE));

    assertThat(scanner.lastRoot).isEqualTo(templateRoot);
  }

  private static final class StubClasspathTemplateScanner extends ClasspathTemplateScanner {
    private final List<String> templates;
    private String lastRoot;

    private StubClasspathTemplateScanner(List<String> templates) {
      this.templates = List.copyOf(templates);
    }

    @Override
    public List<String> scan(String templateRoot) {
      this.lastRoot = templateRoot;
      return templates.stream().filter(t -> t.startsWith(templateRoot + "/")).toList();
    }
  }

  private static final class ThrowingClasspathTemplateScanner extends ClasspathTemplateScanner {
    @Override
    public List<String> scan(String templateRoot) {
      throw new TemplateScanException(templateRoot, new IllegalStateException("boom"));
    }
  }

  private static final class RecordingTemplateRenderer implements TemplateRenderer {
    private final List<String> capturedTemplateNames = new ArrayList<>();
    private final List<Map<String, Object>> capturedModels = new ArrayList<>();

    @Override
    public GeneratedResource renderUtf8(
        Path outPath, String templateResourcePath, Map<String, Object> model) {
      capturedTemplateNames.add(templateResourcePath);
      capturedModels.add(model);
      return new GeneratedTextResource(outPath, "", StandardCharsets.UTF_8);
    }
  }
}
