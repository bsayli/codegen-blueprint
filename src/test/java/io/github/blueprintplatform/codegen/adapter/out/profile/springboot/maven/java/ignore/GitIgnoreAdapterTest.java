package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.ignore;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.TemplateSpec;
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
class GitIgnoreAdapterTest {

  private static final String BASE_PATH = "springboot/maven/java/";

  @Test
  @DisplayName("artifactKey() should return IGNORE_RULES")
  void artifactKey_shouldReturnIgnoreRules() {
    GitIgnoreAdapter adapter =
        new GitIgnoreAdapter(
            new NoopTemplateRenderer(),
            new ArtifactSpec(BASE_PATH, List.of(new TemplateSpec("gitignore.ftl", ".gitignore"))));

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.IGNORE_RULES);
  }

  @Test
  @DisplayName("generate() should render ignore rules with an empty ignoreList model")
  void generate_shouldRenderGitignoreWithEmptyIgnoreList() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateSpec templateSpec = new TemplateSpec("gitignore.ftl", ".gitignore");
    ArtifactSpec artifactSpec = new ArtifactSpec(BASE_PATH, List.of(templateSpec));

    GitIgnoreAdapter adapter = new GitIgnoreAdapter(renderer, artifactSpec);

    ProjectBlueprint blueprint =
        ProjectBlueprintFactory.of(
            new ProjectMetadata(
                new ProjectIdentity(new GroupId("com.acme"), new ArtifactId("demo-app")),
                new ProjectName("Demo App"),
                new ProjectDescription("Sample Project"),
                new PackageName("com.acme.demo")),
            new PlatformSpec(
                new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA),
                new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5)),
            new ArchitectureSpec(
                ProjectLayout.STANDARD, ArchitectureGovernance.none(), SampleCodeOptions.none()),
            Dependencies.of(List.of()));

    Path relativePath = Path.of(".gitignore");
    GeneratedTextResource expectedFile =
        new GeneratedTextResource(relativePath, "# gitignore", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedResource> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(expectedFile);

    assertThat(renderer.capturedOutPath).isEqualTo(relativePath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "gitignore.ftl");

    Map<String, Object> model = renderer.capturedModel;
    assertThat(model).isNotNull().containsEntry("ignoreList", List.of());
  }
}
