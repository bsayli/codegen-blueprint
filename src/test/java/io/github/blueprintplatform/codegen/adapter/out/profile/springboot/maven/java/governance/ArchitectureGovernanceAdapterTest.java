package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.governance;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.templating.ClasspathTemplateScanner;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.factory.ProjectBlueprintFactory;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.EnforcementMode;
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
class ArchitectureGovernanceAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";
  private static final String BASE_PACKAGE = "com.acme.demo";
  private static final String BASE_PACKAGE_PATH = "com/acme/demo";

  private static final ArtifactSpec DUMMY_ARTIFACT_SPEC = new ArtifactSpec(BASE_PATH, List.of());

  private static ProjectBlueprint blueprint(ProjectLayout layout, EnforcementMode mode) {
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
        new ArchitectureSpec(layout, new ArchitectureGovernance(mode), SampleCodeOptions.none());

    Dependencies dependencies = Dependencies.of(List.of());

    return ProjectBlueprintFactory.of(metadata, platform, architecture, dependencies);
  }

  private static List<Path> toRelativePaths(Iterable<? extends GeneratedResource> resources) {
    List<Path> result = new ArrayList<>();
    StreamSupport.stream(resources.spliterator(), false).forEach(r -> result.add(r.relativePath()));
    return result;
  }

  @Test
  @DisplayName("artifactKey() should return ARCHITECTURE_GOVERNANCE")
  void artifactKey_shouldReturnArchitectureGovernance() {
    ArchitectureGovernanceAdapter adapter =
        new ArchitectureGovernanceAdapter(
            new RecordingTemplateRenderer(),
            DUMMY_ARTIFACT_SPEC,
            new StubClasspathTemplateScanner(List.of()));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.ARCHITECTURE_GOVERNANCE);
  }

  @Test
  @DisplayName("generate() should return empty when enforcement mode is NONE")
  void generate_noneMode_shouldReturnEmpty() {
    StubClasspathTemplateScanner scanner =
        new StubClasspathTemplateScanner(
            List.of("springboot/maven/java/governance/hexagonal/strict/X.java.ftl"));

    ArchitectureGovernanceAdapter adapter =
        new ArchitectureGovernanceAdapter(
            new RecordingTemplateRenderer(), DUMMY_ARTIFACT_SPEC, scanner);

    ProjectBlueprint bp = blueprint(ProjectLayout.HEXAGONAL, EnforcementMode.NONE);

    assertThat(adapter.generate(bp)).isEmpty();
    assertThat(scanner.lastRoot).isNull();
  }

  @Test
  @DisplayName(
      "generate() should scan templates and render governance tests into src/test/java/{base}/architecture")
  void generate_hexagonalStrict_shouldGenerateGovernanceTests() {
    String templateRoot = "springboot/maven/java/governance/hexagonal/strict";
    String domainPurityTemplate = templateRoot + "/DomainPurityTest.java.ftl";
    String nonJavaTemplate = templateRoot + "/ignored.txt.ftl";

    RecordingTemplateRenderer renderer = new RecordingTemplateRenderer();
    StubClasspathTemplateScanner scanner =
        new StubClasspathTemplateScanner(List.of(domainPurityTemplate, nonJavaTemplate));

    ArchitectureGovernanceAdapter adapter =
        new ArchitectureGovernanceAdapter(renderer, DUMMY_ARTIFACT_SPEC, scanner);

    ProjectBlueprint bp = blueprint(ProjectLayout.HEXAGONAL, EnforcementMode.STRICT);

    Iterable<? extends GeneratedResource> resources = adapter.generate(bp);
    List<Path> paths = toRelativePaths(resources);

    Path expectedOut =
        Path.of("src/test/java")
            .resolve(BASE_PACKAGE_PATH)
            .resolve("architecture")
            .resolve("DomainPurityTest.java");

    assertThat(paths).containsExactlyInAnyOrder(expectedOut);

    assertThat(renderer.capturedTemplateNames).containsExactly(domainPurityTemplate);

    assertThat(renderer.capturedModels).containsExactly(Map.of("projectPackageName", BASE_PACKAGE));

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
