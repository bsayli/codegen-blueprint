package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.build;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.build.shared.BuildDependency;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.TemplateSpec;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyCoordinates;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyScope;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyVersion;
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
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedTextResource;
import io.github.blueprintplatform.codegen.testsupport.build.RecordingBuildDependencyMapper;
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
class MavenPomBuildConfigurationAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  private static ProjectBlueprint blueprint(
      ArchitectureGovernance governance, Dependencies dependencies) {
    ProjectMetadata metadata =
        new ProjectMetadata(
            new ProjectIdentity(new GroupId("com.acme"), new ArtifactId("demo-app")),
            new ProjectName("Demo App"),
            new ProjectDescription("Sample Project"),
            new PackageName("com.acme.demo"));

    TechStack techStack = new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);
    PlatformTarget target = new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5);
    PlatformSpec platform = new PlatformSpec(techStack, target);

    ArchitectureSpec architecture =
        new ArchitectureSpec(ProjectLayout.STANDARD, governance, SampleCodeOptions.none());

    return ProjectBlueprint.of(metadata, platform, architecture, dependencies);
  }

  private static Dependency dep(String groupId, String artifactId) {
    return new Dependency(
        new DependencyCoordinates(new GroupId(groupId), new ArtifactId(artifactId)),
        new DependencyVersion("1.0.0"),
        DependencyScope.RUNTIME);
  }

  @Test
  @DisplayName("artifactKey() should return BUILD_CONFIG")
  void artifactKey_shouldReturnPom() {
    MavenPomBuildConfigurationAdapter adapter =
        new MavenPomBuildConfigurationAdapter(
            new NoopTemplateRenderer(),
            new ArtifactSpec(BASE_PATH, List.of(new TemplateSpec("pom.ftl", "pom.xml"))),
            new RecordingBuildDependencyMapper(List.of()));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.BUILD_CONFIG);
  }

  @Test
  @DisplayName(
      "generate() should build model with base deps only when JPA not selected and governance disabled")
  void generate_shouldBuildModel_withoutJpa_withoutGovernance() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    List<BuildDependency> mapped =
        List.of(BuildDependency.of("org.acme", "custom-dep", "1.0.0", "runtime"));
    RecordingBuildDependencyMapper mapper = new RecordingBuildDependencyMapper(mapped);

    ArtifactSpec artifactSpec =
        new ArtifactSpec(BASE_PATH, List.of(new TemplateSpec("pom.ftl", "pom.xml")));
    MavenPomBuildConfigurationAdapter adapter =
        new MavenPomBuildConfigurationAdapter(renderer, artifactSpec, mapper);

    ProjectBlueprint bp =
        blueprint(
            ArchitectureGovernance.none(), Dependencies.of(List.of(dep("org.acme", "custom-dep"))));

    renderer.nextFile =
        new GeneratedTextResource(Path.of("pom.xml"), "<project/>", StandardCharsets.UTF_8);

    Iterable<? extends GeneratedResource> result = adapter.generate(bp);

    assertThat(result).singleElement().isSameAs(renderer.nextFile);

    Map<String, Object> model = renderer.capturedModel;
    assertThat(model)
        .containsEntry(MavenPomBuildModel.KEY_GROUP_ID, "com.acme")
        .containsEntry(MavenPomBuildModel.KEY_ARTIFACT_ID, "demo-app")
        .containsEntry(MavenPomBuildModel.KEY_JAVA_VERSION, "21")
        .containsEntry(MavenPomBuildModel.KEY_SPRING_BOOT_VER, "3.5.9")
        .containsEntry(MavenPomBuildModel.KEY_PROJECT_NAME, "Demo App")
        .containsEntry(MavenPomBuildModel.KEY_PROJECT_DESCRIPTION, "Sample Project");

    @SuppressWarnings("unchecked")
    Map<String, String> props =
        (Map<String, String>) model.get(MavenPomBuildModel.KEY_POM_PROPERTIES);
    assertThat(props).isNotNull().isEmpty();

    @SuppressWarnings("unchecked")
    List<BuildDependency> deps =
        (List<BuildDependency>) model.get(MavenPomBuildModel.KEY_DEPENDENCIES);
    assertThat(deps).hasSize(3);

    assertThat(deps.get(0)).isEqualTo(MavenPomBuildModel.CORE_STARTER);
    assertThat(deps.get(1)).isSameAs(mapped.getFirst());
    assertThat(deps.get(2)).isEqualTo(MavenPomBuildModel.TEST_STARTER);
  }

  @Test
  @DisplayName("generate() should add H2 dependency when JPA starter is selected")
  void generate_shouldAddH2_whenJpaSelected() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    List<BuildDependency> mapped =
        List.of(BuildDependency.of("org.acme", "custom-dep", "1.0.0", "runtime"));
    RecordingBuildDependencyMapper mapper = new RecordingBuildDependencyMapper(mapped);

    ArtifactSpec artifactSpec =
        new ArtifactSpec(BASE_PATH, List.of(new TemplateSpec("pom.ftl", "pom.xml")));
    MavenPomBuildConfigurationAdapter adapter =
        new MavenPomBuildConfigurationAdapter(renderer, artifactSpec, mapper);

    Dependency jpaStarter =
        new Dependency(
            new DependencyCoordinates(
                new GroupId("org.springframework.boot"),
                new ArtifactId("spring-boot-starter-data-jpa")),
            null,
            DependencyScope.RUNTIME);

    ProjectBlueprint bp =
        blueprint(
            ArchitectureGovernance.none(),
            Dependencies.of(List.of(dep("org.acme", "custom-dep"), jpaStarter)));

    renderer.nextFile =
        new GeneratedTextResource(Path.of("pom.xml"), "<project/>", StandardCharsets.UTF_8);

    adapter.generate(bp);

    @SuppressWarnings("unchecked")
    List<BuildDependency> deps =
        (List<BuildDependency>) renderer.capturedModel.get(MavenPomBuildModel.KEY_DEPENDENCIES);

    assertThat(deps).hasSize(4);
    assertThat(deps.get(0)).isEqualTo(MavenPomBuildModel.CORE_STARTER);
    assertThat(deps.get(1)).isSameAs(mapped.getFirst());
    assertThat(deps.get(2)).isEqualTo(MavenPomBuildModel.H2_DB);
    assertThat(deps.get(3)).isEqualTo(MavenPomBuildModel.TEST_STARTER);

    @SuppressWarnings("unchecked")
    Map<String, String> props =
        (Map<String, String>) renderer.capturedModel.get(MavenPomBuildModel.KEY_POM_PROPERTIES);
    assertThat(props).isNotNull().isEmpty();
  }

  @Test
  @DisplayName(
      "generate() should add ArchUnit test dependency and pomProperties when governance is enabled")
  void generate_shouldAddArchUnit_whenGovernanceEnabled() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    List<BuildDependency> mapped =
        List.of(BuildDependency.of("org.acme", "custom-dep", "1.0.0", "runtime"));
    RecordingBuildDependencyMapper mapper = new RecordingBuildDependencyMapper(mapped);

    ArtifactSpec artifactSpec =
        new ArtifactSpec(BASE_PATH, List.of(new TemplateSpec("pom.ftl", "pom.xml")));
    MavenPomBuildConfigurationAdapter adapter =
        new MavenPomBuildConfigurationAdapter(renderer, artifactSpec, mapper);

    Dependency jpaStarter =
        new Dependency(
            new DependencyCoordinates(
                new GroupId("org.springframework.boot"),
                new ArtifactId("spring-boot-starter-data-jpa")),
            null,
            DependencyScope.RUNTIME);

    ProjectBlueprint bp =
        blueprint(
            ArchitectureGovernance.basic(),
            Dependencies.of(List.of(dep("org.acme", "custom-dep"), jpaStarter)));

    renderer.nextFile =
        new GeneratedTextResource(Path.of("pom.xml"), "<project/>", StandardCharsets.UTF_8);

    adapter.generate(bp);

    @SuppressWarnings("unchecked")
    Map<String, String> props =
        (Map<String, String>) renderer.capturedModel.get(MavenPomBuildModel.KEY_POM_PROPERTIES);
    assertThat(props)
        .isNotNull()
        .containsEntry(
            MavenPomBuildModel.ARCH_UNIT_VERSION_KEY, MavenPomBuildModel.ARCH_UNIT_VERSION);

    @SuppressWarnings("unchecked")
    List<BuildDependency> deps =
        (List<BuildDependency>) renderer.capturedModel.get(MavenPomBuildModel.KEY_DEPENDENCIES);

    assertThat(deps).hasSize(5);
    assertThat(deps.get(0)).isEqualTo(MavenPomBuildModel.CORE_STARTER);
    assertThat(deps.get(1)).isSameAs(mapped.getFirst());
    assertThat(deps.get(2)).isEqualTo(MavenPomBuildModel.H2_DB);
    assertThat(deps.get(3)).isEqualTo(MavenPomBuildModel.ARCH_UNIT_TEST);
    assertThat(deps.get(4)).isEqualTo(MavenPomBuildModel.TEST_STARTER);
  }
}
