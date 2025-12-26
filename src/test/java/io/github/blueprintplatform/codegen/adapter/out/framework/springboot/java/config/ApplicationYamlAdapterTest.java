package io.github.blueprintplatform.codegen.adapter.out.framework.springboot.java.config;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.TemplateSpec;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.EnforcementMode;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyCoordinates;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyScope;
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
class ApplicationYamlAdapterTest {

  private static final String BASE_PATH = "springboot/java/";

  private static ProjectBlueprint blueprintWithDependencies(Dependencies dependencies) {
    ProjectMetadata metadata =
        new ProjectMetadata(
            new ProjectIdentity(new GroupId("com.acme"), new ArtifactId("demo-app")),
            new ProjectName("Demo App"),
            new ProjectDescription("Sample Project"),
            new PackageName("com.acme.demo"));

    TechStack techStack = new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);
    PlatformTarget platformTarget =
        new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5);
    PlatformSpec platform = new PlatformSpec(techStack, platformTarget);

    ArchitectureSpec architecture =
        new ArchitectureSpec(
            ProjectLayout.STANDARD,
            new ArchitectureGovernance(EnforcementMode.NONE),
            SampleCodeOptions.none());

    return ProjectBlueprint.of(metadata, platform, architecture, dependencies);
  }

  @Test
  @DisplayName("artifactKey() should return APP_CONFIG")
  void artifactKey_shouldReturnApplicationYaml() {
    ApplicationYamlAdapter adapter =
        new ApplicationYamlAdapter(
            new NoopTemplateRenderer(),
            new ArtifactSpec(
                BASE_PATH, List.of(new TemplateSpec("application-yaml.ftl", "application.yml"))));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.APP_CONFIG);
  }

  @Test
  @DisplayName(
      "generate() should build model with applicationName and empty features when dependencies are empty")
  void generate_shouldBuildModelWithEmptyFeatures_whenDependenciesEmpty() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateSpec templateSpec =
        new TemplateSpec("application-yaml.ftl", "src/main/resources/application.yml");
    ArtifactSpec artifactSpec = new ArtifactSpec(BASE_PATH, List.of(templateSpec));

    ApplicationYamlAdapter adapter = new ApplicationYamlAdapter(renderer, artifactSpec);

    ProjectBlueprint blueprint = blueprintWithDependencies(Dependencies.of(List.of()));

    Path relativePath = Path.of("src/main/resources/application.yml");
    GeneratedTextResource expectedFile =
        new GeneratedTextResource(relativePath, "dummy", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedResource> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(expectedFile);

    assertThat(renderer.capturedOutPath).isEqualTo(relativePath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "application-yaml.ftl");

    Map<String, Object> model = renderer.capturedModel;
    assertThat(model).isNotNull().containsEntry(ApplicationYamlModel.KEY_APP_NAME, "demo-app");

    @SuppressWarnings("unchecked")
    Map<String, Boolean> features =
        (Map<String, Boolean>) model.get(ApplicationYamlModel.KEY_FEATURES);
    assertThat(features).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("generate() should compute features map based on selected dependencies")
  void generate_shouldComputeFeaturesMap() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateSpec templateSpec =
        new TemplateSpec("application-yaml.ftl", "src/main/resources/application.yml");
    ArtifactSpec artifactSpec = new ArtifactSpec(BASE_PATH, List.of(templateSpec));

    ApplicationYamlAdapter adapter = new ApplicationYamlAdapter(renderer, artifactSpec);

    Dependency actuator =
        new Dependency(
            new DependencyCoordinates(
                new GroupId("org.springframework.boot"),
                new ArtifactId("spring-boot-starter-actuator")),
            null,
            DependencyScope.RUNTIME);

    ProjectBlueprint blueprint = blueprintWithDependencies(Dependencies.of(List.of(actuator)));

    Path relativePath = Path.of("src/main/resources/application.yml");
    renderer.nextFile = new GeneratedTextResource(relativePath, "dummy", StandardCharsets.UTF_8);

    adapter.generate(blueprint);

    @SuppressWarnings("unchecked")
    Map<String, Boolean> features =
        (Map<String, Boolean>) renderer.capturedModel.get(ApplicationYamlModel.KEY_FEATURES);

    assertThat(features)
        .containsEntry("h2", false)
        .containsEntry("actuator", true)
        .containsEntry("security", false);
  }
}
