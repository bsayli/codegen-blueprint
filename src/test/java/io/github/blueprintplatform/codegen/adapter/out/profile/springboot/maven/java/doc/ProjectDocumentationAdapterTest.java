package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.doc;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependency;
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
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyVersion;
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
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedTextResource;
import io.github.blueprintplatform.codegen.testsupport.build.RecordingPomDependencyMapper;
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
class ProjectDocumentationAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  private static ProjectBlueprint blueprintWithDependencies() {
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
        new ArchitectureSpec(
            ProjectLayout.STANDARD,
            new ArchitectureGovernance(EnforcementMode.NONE),
            new SampleCodeOptions(SampleCodeLevel.NONE));

    Dependency dep =
        new Dependency(
            new DependencyCoordinates(new GroupId("org.acme"), new ArtifactId("custom-dep")),
            new DependencyVersion("1.0.0"),
            DependencyScope.RUNTIME);

    Dependencies dependencies = Dependencies.of(List.of(dep));

    return ProjectBlueprint.of(metadata, platform, architecture, dependencies);
  }

  @Test
  @DisplayName("artifactKey() should return PROJECT_DOCUMENTATION")
  void artifactKey_shouldReturnProjectDocumentation() {
    ProjectDocumentationAdapter adapter =
        new ProjectDocumentationAdapter(
            new NoopTemplateRenderer(),
            new ArtifactSpec(BASE_PATH, List.of(new TemplateSpec("README.ftl", "README.md"))),
            new RecordingPomDependencyMapper(List.of()));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.PROJECT_DOCUMENTATION);
  }

  @Test
  @DisplayName(
      "generate() should build correct project documentation model and delegate dependencies mapping")
  void generate_shouldBuildCorrectModelForProjectDocumentation() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    List<PomDependency> mappedDeps =
        List.of(PomDependency.of("org.acme", "custom-dep", "1.0.0", "runtime"));
    RecordingPomDependencyMapper mapper = new RecordingPomDependencyMapper(mappedDeps);

    TemplateSpec templateSpec = new TemplateSpec("README.ftl", "README.md");
    ArtifactSpec artifactSpec = new ArtifactSpec(BASE_PATH, List.of(templateSpec));

    ProjectDocumentationAdapter adapter =
        new ProjectDocumentationAdapter(renderer, artifactSpec, mapper);

    ProjectBlueprint blueprint = blueprintWithDependencies();

    Path relativePath = Path.of("README.md");
    GeneratedTextResource dummyFile =
        new GeneratedTextResource(relativePath, "# Readme", StandardCharsets.UTF_8);
    renderer.nextFile = dummyFile;

    Iterable<? extends GeneratedResource> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(dummyFile);

    assertThat(renderer.capturedOutPath).isEqualTo(relativePath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "README.ftl");
    assertThat(renderer.capturedModel).isNotNull();

    Map<String, Object> model = renderer.capturedModel;

    assertThat(model)
        .containsEntry(ProjectDocumentationModel.PROJECT_NAME, "Demo App")
        .containsEntry(ProjectDocumentationModel.PROJECT_DESCRIPTION, "Sample Project")
        .containsEntry(ProjectDocumentationModel.GROUP_ID, "com.acme")
        .containsEntry(ProjectDocumentationModel.ARTIFACT_ID, "demo-app")
        .containsEntry(ProjectDocumentationModel.PACKAGE_NAME, "com.acme.demo")
        .containsEntry(ProjectDocumentationModel.BUILD_TOOL, "maven")
        .containsEntry(ProjectDocumentationModel.LANGUAGE, "java")
        .containsEntry(ProjectDocumentationModel.FRAMEWORK, "spring-boot")
        .containsEntry(ProjectDocumentationModel.JAVA_VERSION, "21")
        .containsEntry(ProjectDocumentationModel.SPRING_BOOT_VERSION, "3.5.9")
        .containsEntry(ProjectDocumentationModel.LAYOUT, "standard")
        .containsEntry(ProjectDocumentationModel.ENFORCEMENT, "none")
        .containsEntry(ProjectDocumentationModel.SAMPLE_CODE, "none");

    assertThat(mapper.capturedDependencies).isSameAs(blueprint.getDependencies());

    @SuppressWarnings("unchecked")
    List<PomDependency> deps =
        (List<PomDependency>) model.get(ProjectDocumentationModel.DEPENDENCIES);
    assertThat(deps).isSameAs(mappedDeps).hasSize(1);

    @SuppressWarnings("unchecked")
    Map<String, Boolean> features =
        (Map<String, Boolean>) model.get(ProjectDocumentationModel.FEATURES);

    assertThat(features)
        .containsEntry("h2", false)
        .containsEntry("actuator", false)
        .containsEntry("security", false);

    PomDependency d = deps.getFirst();
    assertThat(d.groupId()).isEqualTo("org.acme");
    assertThat(d.artifactId()).isEqualTo("custom-dep");
    assertThat(d.version()).isEqualTo("1.0.0");
    assertThat(d.scope()).isEqualTo("runtime");
  }
}
