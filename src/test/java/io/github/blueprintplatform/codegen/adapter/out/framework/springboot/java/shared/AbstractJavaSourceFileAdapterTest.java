package io.github.blueprintplatform.codegen.adapter.out.framework.springboot.java.shared;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.TemplateSpec;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.adapter.shared.naming.StringCaseFormatter;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class AbstractJavaSourceFileAdapterTest {

  private static final String BASE_PATH = "springboot/java/";

  @Test
  @DisplayName("generate() should build correct path, model and return single file")
  void generate_shouldBuildOutPathAndModelAndReturnFile() {
    CapturingTemplateRenderer renderer = new CapturingTemplateRenderer();

    TemplateSpec templateSpec = new TemplateSpec("java-class.ftl", "src/main/java");

    ArtifactSpec artifactSpec = new ArtifactSpec(BASE_PATH, List.of(templateSpec));

    StringCaseFormatter formatter = new StringCaseFormatter();

    TestJavaSourceFileAdapter adapter =
        new TestJavaSourceFileAdapter(renderer, artifactSpec, formatter);

    ProjectBlueprint blueprint =
        ProjectBlueprint.of(
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

    Path expectedPath = Path.of("src/main/java/com/acme/demo/DemoApplication.java");

    GeneratedTextResource expectedFile =
        new GeneratedTextResource(expectedPath, "class DemoApplication {}", StandardCharsets.UTF_8);
    renderer.nextFile = expectedFile;

    Iterable<? extends GeneratedResource> result = adapter.generate(blueprint);

    assertThat(result).singleElement().isSameAs(expectedFile);

    assertThat(renderer.capturedOutPath).isEqualTo(expectedPath);
    assertThat(renderer.capturedTemplateName).isEqualTo(BASE_PATH + "java-class.ftl");

    assertThat(renderer.capturedModel)
        .isNotNull()
        .containsEntry("projectPackageName", "com.acme.demo")
        .containsEntry("className", "DemoApplication");
  }

  private static final class TestJavaSourceFileAdapter extends AbstractJavaSourceFileAdapter {

    TestJavaSourceFileAdapter(
        TemplateRenderer renderer,
        ArtifactSpec artifactSpec,
        StringCaseFormatter stringCaseFormatter) {
      super(renderer, artifactSpec, stringCaseFormatter);
    }

    @Override
    protected String buildClassName(ProjectBlueprint blueprint) {
      return "DemoApplication";
    }

    @Override
    public ArtifactKey artifactKey() {
      return ArtifactKey.MAIN_SOURCE_ENTRY_POINT;
    }
  }
}
