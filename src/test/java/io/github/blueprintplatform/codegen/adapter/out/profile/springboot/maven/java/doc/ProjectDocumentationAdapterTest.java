package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.doc;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependency;
import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.bootstrap.config.ArtifactDefinition;
import io.github.blueprintplatform.codegen.bootstrap.config.TemplateDefinition;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyCoordinates;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyScope;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyVersion;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ArtifactId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.GroupId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectDescription;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectName;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;
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
    ProjectIdentity identity =
        new ProjectIdentity(new GroupId("com.acme"), new ArtifactId("demo-app"));

    ProjectName name = new ProjectName("Demo App");
    ProjectDescription description = new ProjectDescription("Sample Project");
    PackageName pkg = new PackageName("com.acme.demo");

    TechStack techStack = new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);

    ProjectLayout layout = ProjectLayout.STANDARD;
    PlatformTarget target = new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5);

    Dependency dep =
        new Dependency(
            new DependencyCoordinates(new GroupId("org.acme"), new ArtifactId("custom-dep")),
            new DependencyVersion("1.0.0"),
            DependencyScope.RUNTIME);

    Dependencies dependencies = Dependencies.of(List.of(dep));

    SampleCodeOptions sampleCodeOptions = SampleCodeOptions.none();

    return new ProjectBlueprint(
        identity,
        name,
        description,
        pkg,
        techStack,
        layout,
        target,
        dependencies,
        sampleCodeOptions);
  }

  @Test
  @DisplayName("artifactKey() should return PROJECT_DOCUMENTATION")
  void artifactKey_shouldReturnProjectDocumentation() {
    ProjectDocumentationAdapter adapter =
        new ProjectDocumentationAdapter(
            new NoopTemplateRenderer(),
            new ArtifactDefinition(
                BASE_PATH, List.of(new TemplateDefinition("README.ftl", "README.md"))),
            new PomDependencyMapper());

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

    TemplateDefinition templateDefinition = new TemplateDefinition("README.ftl", "README.md");
    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    ProjectDocumentationAdapter adapter =
        new ProjectDocumentationAdapter(renderer, artifactDefinition, mapper);

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
        .containsEntry("projectName", "Demo App")
        .containsEntry("projectDescription", "Sample Project")
        .containsEntry("groupId", "com.acme")
        .containsEntry("artifactId", "demo-app")
        .containsEntry("packageName", "com.acme.demo")
        .containsEntry("buildTool", "maven")
        .containsEntry("language", "java")
        .containsEntry("framework", "spring-boot")
        .containsEntry("javaVersion", "21")
        .containsEntry("springBootVersion", "3.5.8")
        .containsEntry("hasHexSample", false);

    assertThat(mapper.capturedDependencies).isSameAs(blueprint.getDependencies());

    @SuppressWarnings("unchecked")
    List<PomDependency> deps = (List<PomDependency>) model.get("dependencies");
    assertThat(deps).isSameAs(mappedDeps).hasSize(1);

    PomDependency d = deps.getFirst();
    assertThat(d.groupId()).isEqualTo("org.acme");
    assertThat(d.artifactId()).isEqualTo("custom-dep");
    assertThat(d.version()).isEqualTo("1.0.0");
    assertThat(d.scope()).isEqualTo("runtime");
  }
}
