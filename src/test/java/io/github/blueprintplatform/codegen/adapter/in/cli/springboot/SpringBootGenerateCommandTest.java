package io.github.blueprintplatform.codegen.adapter.in.cli.springboot;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.in.cli.CliProjectRequest;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.dependency.SpringBootDependencyAlias;
import io.github.blueprintplatform.codegen.application.port.in.project.CreateProjectPort;
import io.github.blueprintplatform.codegen.application.port.in.project.dto.CreateProjectRequest;
import io.github.blueprintplatform.codegen.application.port.in.project.dto.CreateProjectResponse;
import io.github.blueprintplatform.codegen.application.port.in.project.dto.ProjectGenerationSummary;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.EnforcementMode;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeLevel;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class SpringBootGenerateCommandTest {

  @Test
  @DisplayName("call() should build profile, map layout & dependencies and invoke use case")
  void call_shouldBuildProfileAndInvokeUseCase() {
    var mapper = new RecordingMapper();
    var useCase = new StubCreateProjectPort();

    var cmd = new SpringBootGenerateCommand(mapper, useCase);

    cmd.groupId = "com.acme";
    cmd.artifactId = "demo-app";
    cmd.name = "Demo App";
    cmd.description = "Demo application for Acme";
    cmd.packageName = "com.acme.demo";
    cmd.buildTool = BuildTool.MAVEN;
    cmd.language = Language.JAVA;
    cmd.javaVersion = JavaVersion.JAVA_21;
    cmd.bootVersion = SpringBootVersion.V3_5;

    cmd.layout = ProjectLayout.STANDARD;
    cmd.sampleCode = SampleCodeLevel.NONE;
    cmd.enforcementMode = EnforcementMode.NONE;
    cmd.dependencies = List.of(SpringBootDependencyAlias.WEB);
    Path expected = Path.of(".");
    cmd.targetDirectory = expected;

    Integer exitCode = cmd.call();

    assertThat(exitCode).isZero();

    assertThat(mapper.lastRequest).isNotNull();
    assertThat(mapper.lastBuildTool).isEqualTo(BuildTool.MAVEN);
    assertThat(mapper.lastLanguage).isEqualTo(Language.JAVA);
    assertThat(mapper.lastJavaVersion).isEqualTo(JavaVersion.JAVA_21);
    assertThat(mapper.lastBootVersion).isEqualTo(SpringBootVersion.V3_5);

    assertThat(mapper.lastRequest.groupId()).isEqualTo("com.acme");
    assertThat(mapper.lastRequest.artifactId()).isEqualTo("demo-app");
    assertThat(mapper.lastRequest.name()).isEqualTo("Demo App");
    assertThat(mapper.lastRequest.description()).isEqualTo("Demo application for Acme");
    assertThat(mapper.lastRequest.packageName()).isEqualTo("com.acme.demo");
    assertThat(mapper.lastRequest.targetDirectory()).isEqualTo(expected);

    assertThat(mapper.lastRequest.profile()).isEqualTo("spring-boot-maven-java");

    assertThat(mapper.lastRequest.layoutKey()).isEqualTo(ProjectLayout.STANDARD.key());
    assertThat(mapper.lastRequest.enforcementModeKey()).isEqualTo(EnforcementMode.NONE.key());
    assertThat(mapper.lastRequest.sampleCodeLevelKey()).isEqualTo(SampleCodeLevel.NONE.key());

    assertThat(mapper.lastRequest.dependencies())
        .containsExactly(SpringBootDependencyAlias.WEB.name());

    assertThat(useCase.lastCommand).isSameAs(mapper.returnedCommand);
  }

  static class RecordingMapper extends CreateProjectRequestMapper {

    final CreateProjectRequest returnedCommand = null;
    CliProjectRequest lastRequest;
    BuildTool lastBuildTool;
    Language lastLanguage;
    JavaVersion lastJavaVersion;
    SpringBootVersion lastBootVersion;

    @Override
    public CreateProjectRequest from(
        CliProjectRequest request,
        BuildTool buildTool,
        Language language,
        JavaVersion javaVersion,
        SpringBootVersion bootVersion) {

      this.lastRequest = request;
      this.lastBuildTool = buildTool;
      this.lastLanguage = language;
      this.lastJavaVersion = javaVersion;
      this.lastBootVersion = bootVersion;

      return returnedCommand;
    }
  }

  static class StubCreateProjectPort implements CreateProjectPort {

    CreateProjectRequest lastCommand;

    @Override
    public CreateProjectResponse handle(CreateProjectRequest command) {
      this.lastCommand = command;

      var project =
          new ProjectGenerationSummary(
              "com.acme",
              "demo-app",
              "Demo App",
              "Demo application for Acme",
              "com.acme.demo",
              new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA),
              ProjectLayout.STANDARD,
              EnforcementMode.NONE,
              new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5),
              SampleCodeOptions.none(),
              List.of());

      return new CreateProjectResponse(project, Path.of("."), Path.of("demo-app.zip"), List.of());
    }
  }
}
