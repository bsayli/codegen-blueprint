package io.github.blueprintplatform.codegen.adapter.in.cli.springboot;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.in.cli.mapper.CreateProjectCommandMapper;
import io.github.blueprintplatform.codegen.adapter.in.cli.request.CliProjectRequest;
import io.github.blueprintplatform.codegen.adapter.in.cli.request.model.CliRuntimeTargetKeys;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option.SpringBootArchitectureEnforcementOption;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option.SpringBootBuildToolOption;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option.SpringBootDependencyOption;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option.SpringBootJavaVersionOption;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option.SpringBootLanguageOption;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option.SpringBootLayoutOption;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option.SpringBootSampleCodeOption;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option.SpringBootVersionOption;
import io.github.blueprintplatform.codegen.application.port.in.project.CreateProjectPort;
import io.github.blueprintplatform.codegen.application.port.in.project.model.CreateProjectCommand;
import io.github.blueprintplatform.codegen.application.port.in.project.model.CreateProjectResult;
import io.github.blueprintplatform.codegen.application.port.in.project.model.ProjectSummary;
import io.github.blueprintplatform.codegen.application.port.in.project.model.summary.ArchitectureSpecSummary;
import io.github.blueprintplatform.codegen.application.port.in.project.model.summary.ProjectMetadataSummary;
import io.github.blueprintplatform.codegen.application.port.in.project.model.summary.RuntimeTargetSummary;
import io.github.blueprintplatform.codegen.application.port.in.project.model.summary.TechStackSummary;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class SpringBootGenerateCommandTest {

  @Test
  @DisplayName("call() should build CliProjectRequest and invoke use case with mapped command")
  void call_shouldBuildRequestAndInvokeUseCase() {
    var mapper = new RecordingMapper();
    var useCase = new StubCreateProjectPort();

    var cmd = new SpringBootGenerateCommand(mapper, useCase);

    cmd.groupId = "com.acme";
    cmd.artifactId = "demo-app";
    cmd.name = "Demo App";
    cmd.description = "Demo application for Acme";
    cmd.packageName = "com.acme.demo";

    cmd.buildTool = SpringBootBuildToolOption.MAVEN;
    cmd.language = SpringBootLanguageOption.JAVA;
    cmd.javaVersion = SpringBootJavaVersionOption.JAVA_21;
    cmd.bootVersion = SpringBootVersionOption.V3_5;

    cmd.layout = SpringBootLayoutOption.STANDARD;
    cmd.sampleCode = SpringBootSampleCodeOption.NONE;
    cmd.enforcementMode = SpringBootArchitectureEnforcementOption.NONE;

    cmd.dependencies = List.of(SpringBootDependencyOption.WEB);

    Path expectedTargetDir = Path.of(".");
    cmd.targetDirectory = expectedTargetDir;

    Integer exitCode = cmd.call();

    assertThat(exitCode).isZero();

    assertThat(mapper.lastRequest).isNotNull();

    CliProjectRequest r = mapper.lastRequest;

    assertThat(r.metadata().groupId()).isEqualTo("com.acme");
    assertThat(r.metadata().artifactId()).isEqualTo("demo-app");
    assertThat(r.metadata().projectName()).isEqualTo("Demo App");
    assertThat(r.metadata().projectDescription()).isEqualTo("Demo application for Acme");
    assertThat(r.metadata().packageName()).isEqualTo("com.acme.demo");

    assertThat(r.targetDirectory()).isEqualTo(expectedTargetDir);

    assertThat(r.techStack().framework()).isEqualTo("spring-boot");
    assertThat(r.techStack().buildTool()).isEqualTo("maven");
    assertThat(r.techStack().language()).isEqualTo("java");

    assertThat(r.runtimeTarget().type()).isEqualTo(CliRuntimeTargetKeys.TYPE_SPRING_BOOT_JVM);
    assertThat(r.runtimeTarget().params())
        .containsEntry(CliRuntimeTargetKeys.PARAM_JAVA_VERSION, "21")
        .containsEntry(CliRuntimeTargetKeys.PARAM_SPRING_BOOT_VERSION, "3.5");

    assertThat(r.architecture().layout()).isEqualTo("standard");
    assertThat(r.architecture().enforcementMode()).isEqualTo("none");
    assertThat(r.architecture().sampleCodeLevel()).isEqualTo("none");

    assertThat(r.dependencies()).hasSize(1);
    assertThat(r.dependencies().getFirst().groupId()).isEqualTo("org.springframework.boot");
    assertThat(r.dependencies().getFirst().artifactId()).isEqualTo("spring-boot-starter-web");
    assertThat(r.dependencies().getFirst().version()).isNull();
    assertThat(r.dependencies().getFirst().scope()).isNull();

    assertThat(useCase.lastCommand).isSameAs(mapper.returnedCommand);
  }

  static class RecordingMapper extends CreateProjectCommandMapper {

    final CreateProjectCommand returnedCommand = null;
    CliProjectRequest lastRequest;

    @Override
    public CreateProjectCommand from(CliProjectRequest request) {
      this.lastRequest = request;
      return returnedCommand;
    }
  }

  static class StubCreateProjectPort implements CreateProjectPort {

    CreateProjectCommand lastCommand;

    @Override
    public CreateProjectResult handle(CreateProjectCommand command) {
      this.lastCommand = command;

      var metadata =
          new ProjectMetadataSummary(
              "com.acme", "demo-app", "Demo App", "Demo application for Acme", "com.acme.demo");

      var techStack = new TechStackSummary("spring-boot", "3.5.9", "maven", null, "java", "21");

      var runtimeTarget =
          new RuntimeTargetSummary(
              "spring-boot-jvm",
              Map.of(
                  "javaVersion", "21",
                  "springBootVersion", "3.5.9"));

      var architecture = new ArchitectureSpecSummary("standard", "none", "none");

      var project =
          new ProjectSummary(
              metadata, techStack, runtimeTarget, architecture, List.of(), List.of());

      return new CreateProjectResult(project, Path.of("."), Path.of("demo-app.zip"));
    }
  }
}
