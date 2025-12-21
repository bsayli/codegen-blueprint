package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.governance;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.templating.ClasspathTemplateScanner;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.EnforcementMode;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyCoordinates;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class ArchitectureGovernanceAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java";
  private static final String BASE_PACKAGE = "com.acme.demo";

  private static final String GOV_HEX_STRICT = "springboot/maven/java/governance/hexagonal/strict";

  private static final String DOMAIN_PURITY_TEMPLATE =
      GOV_HEX_STRICT + "/DomainPurityTest.java.ftl";

  private static final String HEX_REST_SIGNATURE_TEMPLATE =
      GOV_HEX_STRICT + "/HexagonalStrictRestBoundarySignatureIsolationTest.java.ftl";

  private static final String STD_REST_SIGNATURE_TEMPLATE =
      GOV_HEX_STRICT + "/StandardStrictRestBoundarySignatureIsolationTest.java.ftl";

  private static final ArtifactSpec DUMMY_ARTIFACT_SPEC = new ArtifactSpec(BASE_PATH, List.of());

  private static final GroupId ACME_GROUP_ID = new GroupId("com.acme");
  private static final ArtifactId DEMO_ARTIFACT_ID = new ArtifactId("demo-app");

  private static final GroupId SPRING_BOOT_GROUP_ID = new GroupId("org.springframework.boot");
  private static final ArtifactId SPRING_BOOT_WEB_ARTIFACT_ID =
      new ArtifactId("spring-boot-starter-web");

  private static StubClasspathTemplateScanner scannerWithTemplates(String... templates) {
    return new StubClasspathTemplateScanner(List.of(templates));
  }

  private static ProjectBlueprint blueprint(EnforcementMode mode) {
    return blueprint(mode, Dependencies.of(List.of()));
  }

  private static ProjectBlueprint blueprint(EnforcementMode mode, Dependencies dependencies) {
    ProjectMetadata metadata =
        new ProjectMetadata(
            new ProjectIdentity(ACME_GROUP_ID, DEMO_ARTIFACT_ID),
            new ProjectName("Demo App"),
            new ProjectDescription("Sample Project"),
            new PackageName(BASE_PACKAGE));

    PlatformSpec platform =
        new PlatformSpec(
            new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA),
            new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5));

    ArchitectureSpec architecture =
        new ArchitectureSpec(
            ProjectLayout.HEXAGONAL, new ArchitectureGovernance(mode), SampleCodeOptions.none());

    return ProjectBlueprint.of(metadata, platform, architecture, dependencies);
  }

  private static Dependencies dependenciesWithSpringWeb() {
    DependencyCoordinates coordinates =
        new DependencyCoordinates(SPRING_BOOT_GROUP_ID, SPRING_BOOT_WEB_ARTIFACT_ID);
    return Dependencies.of(List.of(new Dependency(coordinates, null, null)));
  }

  private static Path expectedArchUnitOutPath(String fileName) {
    String packagePath = BASE_PACKAGE.replace('.', '/');
    return Paths.get("src/test/java")
        .resolve(packagePath)
        .resolve("architecture")
        .resolve("archunit")
        .resolve(fileName);
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
    RecordingTemplateRenderer renderer = new RecordingTemplateRenderer();
    ArchitectureGovernanceAdapter adapter =
        new ArchitectureGovernanceAdapter(
            renderer,
            DUMMY_ARTIFACT_SPEC,
            new StubClasspathTemplateScanner(List.of(DOMAIN_PURITY_TEMPLATE)));

    assertThat(adapter.generate(blueprint(EnforcementMode.NONE))).isEmpty();
    assertThat(renderer.capturedTemplateNames).isEmpty();
    assertThat(renderer.capturedOutPaths).isEmpty();
  }

  @Test
  @DisplayName("STRICT + no starter-web -> REST boundary signature tests must NOT be generated")
  void strict_withoutWeb_shouldSkipRestSignatureTests() {
    RecordingTemplateRenderer renderer = new RecordingTemplateRenderer();
    StubClasspathTemplateScanner scanner =
        scannerWithTemplates(
            DOMAIN_PURITY_TEMPLATE, HEX_REST_SIGNATURE_TEMPLATE, STD_REST_SIGNATURE_TEMPLATE);

    ArchitectureGovernanceAdapter adapter =
        new ArchitectureGovernanceAdapter(renderer, DUMMY_ARTIFACT_SPEC, scanner);

    adapter.generate(blueprint(EnforcementMode.STRICT));

    assertThat(renderer.capturedTemplateNames)
        .contains(DOMAIN_PURITY_TEMPLATE)
        .doesNotContain(HEX_REST_SIGNATURE_TEMPLATE)
        .doesNotContain(STD_REST_SIGNATURE_TEMPLATE);

    assertThat(renderer.capturedOutPaths)
        .contains(expectedArchUnitOutPath("DomainPurityTest.java"))
        .doesNotContain(
            expectedArchUnitOutPath("HexagonalStrictRestBoundarySignatureIsolationTest.java"),
            expectedArchUnitOutPath("StandardStrictRestBoundarySignatureIsolationTest.java"));
  }

  @Test
  @DisplayName("STRICT + starter-web -> REST boundary signature tests must be generated")
  void strict_withWeb_shouldIncludeRestSignatureTests() {
    RecordingTemplateRenderer renderer = new RecordingTemplateRenderer();
    StubClasspathTemplateScanner scanner =
        scannerWithTemplates(
            DOMAIN_PURITY_TEMPLATE, HEX_REST_SIGNATURE_TEMPLATE, STD_REST_SIGNATURE_TEMPLATE);

    ArchitectureGovernanceAdapter adapter =
        new ArchitectureGovernanceAdapter(renderer, DUMMY_ARTIFACT_SPEC, scanner);

    adapter.generate(blueprint(EnforcementMode.STRICT, dependenciesWithSpringWeb()));

    assertThat(renderer.capturedTemplateNames)
        .contains(DOMAIN_PURITY_TEMPLATE, HEX_REST_SIGNATURE_TEMPLATE, STD_REST_SIGNATURE_TEMPLATE);

    assertThat(renderer.capturedOutPaths)
        .contains(
            expectedArchUnitOutPath("DomainPurityTest.java"),
            expectedArchUnitOutPath("HexagonalStrictRestBoundarySignatureIsolationTest.java"),
            expectedArchUnitOutPath("StandardStrictRestBoundarySignatureIsolationTest.java"));
  }

  private static final class StubClasspathTemplateScanner extends ClasspathTemplateScanner {
    private final List<String> templates;

    private StubClasspathTemplateScanner(List<String> templates) {
      this.templates = List.copyOf(templates);
    }

    @Override
    public List<String> scan(String templateRoot) {
      return templates.stream().filter(t -> t.startsWith(templateRoot)).toList();
    }
  }

  private static final class RecordingTemplateRenderer implements TemplateRenderer {
    private final List<String> capturedTemplateNames = new ArrayList<>();
    private final List<Path> capturedOutPaths = new ArrayList<>();

    @Override
    public GeneratedResource renderUtf8(
        Path outPath, String templateResourcePath, Map<String, Object> model) {
      capturedTemplateNames.add(templateResourcePath);
      capturedOutPaths.add(outPath);
      return new GeneratedTextResource(outPath, "", StandardCharsets.UTF_8);
    }
  }
}
