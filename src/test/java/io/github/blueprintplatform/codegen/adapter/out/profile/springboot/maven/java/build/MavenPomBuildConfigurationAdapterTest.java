package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.build;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependency;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.TemplateSpec;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.factory.ProjectBlueprintFactory;
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
class MavenPomBuildConfigurationAdapterTest {

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
            SampleCodeOptions.none());

    Dependency dep =
        new Dependency(
            new DependencyCoordinates(new GroupId("org.acme"), new ArtifactId("custom-dep")),
            new DependencyVersion("1.0.0"),
            DependencyScope.RUNTIME);

    Dependencies dependencies = Dependencies.of(List.of(dep));

    return ProjectBlueprintFactory.of(metadata, platform, architecture, dependencies);
  }

  @Test
  @DisplayName("artifactKey() should return BUILD_CONFIG")
  void artifactKey_shouldReturnPom() {
    MavenPomBuildConfigurationAdapter adapter =
        new MavenPomBuildConfigurationAdapter(
            new NoopTemplateRenderer(),
            new ArtifactSpec(BASE_PATH, List.of(new TemplateSpec("pom.ftl", "pom.xml"))),
            new RecordingPomDependencyMapper(List.of()));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.BUILD_CONFIG);
  }

  @Test
  @DisplayName("buildModel via generate() should populate all POM fields and dependencies")
  void generate_shouldBuildCorrectModelForPom() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();
    RecordingPomDependencyMapper mapper =
        new RecordingPomDependencyMapper(
            List.of(PomDependency.of("org.acme", "custom-dep", "1.0.0", "runtime")));

    TemplateSpec templateSpec = new TemplateSpec("pom.ftl", "pom.xml");
    ArtifactSpec artifactSpec = new ArtifactSpec(BASE_PATH, List.of(templateSpec));

    MavenPomBuildConfigurationAdapter adapter =
        new MavenPomBuildConfigurationAdapter(renderer, artifactSpec, mapper);

    ProjectBlueprint blueprint = blueprintWithDependencies();

    Path relativePath = Path.of("pom.xml");
    GeneratedTextResource dummyFile =
        new GeneratedTextResource(relativePath, "<project/>", StandardCharsets.UTF_8);
    renderer.nextFile = dummyFile;

    Iterable<? extends GeneratedResource> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(dummyFile);

    assertThat(renderer.capturedOutPath).isEqualTo(relativePath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "pom.ftl");
    assertThat(renderer.capturedModel).isNotNull();

    Map<String, Object> model = renderer.capturedModel;

    assertThat(model)
        .containsEntry("groupId", "com.acme")
        .containsEntry("artifactId", "demo-app")
        .containsEntry("javaVersion", "21")
        .containsEntry("springBootVersion", "3.5.8")
        .containsEntry("projectName", "Demo App")
        .containsEntry("projectDescription", "Sample Project");

    assertThat(mapper.capturedDependencies).isSameAs(blueprint.getDependencies());

    @SuppressWarnings("unchecked")
    List<PomDependency> deps = (List<PomDependency>) model.get("dependencies");
    assertThat(deps).hasSize(3);

    PomDependency core = deps.getFirst();
    assertThat(core.groupId()).isEqualTo("org.springframework.boot");
    assertThat(core.artifactId()).isEqualTo("spring-boot-starter");
    assertThat(core.version()).isNull();
    assertThat(core.scope()).isNull();

    PomDependency mapped = deps.get(1);
    assertThat(mapped.groupId()).isEqualTo("org.acme");
    assertThat(mapped.artifactId()).isEqualTo("custom-dep");
    assertThat(mapped.version()).isEqualTo("1.0.0");
    assertThat(mapped.scope()).isEqualTo("runtime");

    PomDependency testStarter = deps.get(2);
    assertThat(testStarter.groupId()).isEqualTo("org.springframework.boot");
    assertThat(testStarter.artifactId()).isEqualTo("spring-boot-starter-test");
    assertThat(testStarter.scope()).isEqualTo("test");
  }
}
