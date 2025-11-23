package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.docs;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bsayli.codegen.initializr.adapter.out.build.maven.shared.PomDependency;
import io.github.bsayli.codegen.initializr.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.bootstrap.config.TemplateDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependencies;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependency;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.DependencyCoordinates;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.DependencyScope;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.DependencyVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ArtifactId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.GroupId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectDescription;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectName;
import io.github.bsayli.codegen.initializr.domain.model.value.pkg.PackageName;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import io.github.bsayli.codegen.initializr.testsupport.build.RecordingPomDependencyMapper;
import io.github.bsayli.codegen.initializr.testsupport.templating.CapturingTemplateRenderer;
import io.github.bsayli.codegen.initializr.testsupport.templating.NoopTemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
@DisplayName("Unit Test: ReadmeAdapter")
class ReadmeAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  private static ProjectBlueprint blueprintWithDependencies() {
    ProjectIdentity identity =
        new ProjectIdentity(new GroupId("com.acme"), new ArtifactId("demo-app"));

    ProjectName name = new ProjectName("Demo App");
    ProjectDescription description = new ProjectDescription("Sample Project");
    PackageName pkg = new PackageName("com.acme.demo");

    BuildOptions buildOptions =
        new BuildOptions(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);

    PlatformTarget target = new PlatformTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5_6);

    Dependency dep =
        new Dependency(
            new DependencyCoordinates(new GroupId("org.acme"), new ArtifactId("custom-dep")),
            new DependencyVersion("1.0.0"),
            DependencyScope.RUNTIME);

    Dependencies dependencies = Dependencies.of(List.of(dep));

    return new ProjectBlueprint(
        identity, name, description, pkg, buildOptions, target, dependencies);
  }

  @Test
  @DisplayName("artifactKey() should return README")
  void artifactKey_shouldReturnReadme() {
    ReadmeAdapter adapter =
        new ReadmeAdapter(
            new NoopTemplateRenderer(),
            new ArtifactDefinition(
                BASE_PATH, List.of(new TemplateDefinition("README.ftl", "README.md"))),
            new PomDependencyMapper());

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.README);
  }

  @Test
  @DisplayName("generate() should build correct README model and delegate dependencies mapping")
  void generate_shouldBuildCorrectModelForReadme() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    List<PomDependency> mappedDeps =
        List.of(PomDependency.of("org.acme", "custom-dep", "1.0.0", "runtime"));
    RecordingPomDependencyMapper mapper = new RecordingPomDependencyMapper(mappedDeps);

    TemplateDefinition templateDefinition = new TemplateDefinition("README.ftl", "README.md");
    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(BASE_PATH, List.of(templateDefinition));

    ReadmeAdapter adapter = new ReadmeAdapter(renderer, artifactDefinition, mapper);

    ProjectBlueprint blueprint = blueprintWithDependencies();

    Path relativePath = Path.of("README.md");
    GeneratedFile.Text dummyFile =
        new GeneratedFile.Text(relativePath, "# Readme", StandardCharsets.UTF_8);
    renderer.nextFile = dummyFile;

    Iterable<? extends GeneratedFile> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(dummyFile);

    assertThat(renderer.capturedOutPath).isEqualTo(relativePath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "README.ftl");
    assertThat(renderer.capturedModel).isNotNull();

    Map<String, Object> model = renderer.capturedModel;

    assertThat(model)
        .containsEntry("projectName", "demo-app")
        .containsEntry("projectDescription", "Sample Project")
        .containsEntry("groupId", "com.acme")
        .containsEntry("artifactId", "demo-app")
        .containsEntry("packageName", "com.acme.demo")
        .containsEntry("buildTool", "MAVEN")
        .containsEntry("language", "JAVA")
        .containsEntry("framework", "SPRING_BOOT")
        .containsEntry("javaVersion", "21")
        .containsEntry("springBootVersion", "3.5.6");

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
