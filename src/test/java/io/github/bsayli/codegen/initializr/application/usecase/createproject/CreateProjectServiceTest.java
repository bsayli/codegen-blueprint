package io.github.bsayli.codegen.initializr.application.usecase.createproject;

import static io.github.bsayli.codegen.initializr.domain.port.out.ProjectRootExistencePolicy.FAIL_IF_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import io.github.bsayli.codegen.initializr.domain.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.domain.port.out.ProjectRootExistencePolicy;
import io.github.bsayli.codegen.initializr.domain.port.out.ProjectRootPort;
import io.github.bsayli.codegen.initializr.domain.port.out.ProjectWriterPort;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@Tag("unit")
@Tag("application")
@DisplayName("CreateProjectService")
class CreateProjectServiceTest {

  @TempDir Path tempDir;

  @Test
  @DisplayName("execute() prepares project root and writes pom.xml and .gitignore")
  void creates_project_root_and_writes_artifacts() {
    var mapper = new ProjectBlueprintMapper();
    var fakeRootPort = new FakeRootPort();
    var fakeArtifacts = new FakeArtifactsPort();
    var fakeWriter = new FakeWriterPort();

    var service = new CreateProjectService(mapper, fakeRootPort, fakeArtifacts, fakeWriter);

    var cmd =
            new CreateProjectCommand(
                    "com.acme",
                    "demo-app",
                    "Demo App",
                    "desc",
                    "com.acme.demo",
                    new BuildOptions(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA),
                    JavaVersion.JAVA_21,
                    SpringBootVersion.V3_5_6,
                    List.of(),
                    tempDir);

    var result = service.execute(cmd);

    ProjectBlueprint bp = result.blueprint();
    assertThat(bp.getIdentity().groupId().value()).isEqualTo("com.acme");
    assertThat(bp.getIdentity().artifactId().value()).isEqualTo("demo-app");
    assertThat(result.projectRoot().getFileName()).hasToString("demo-app");

    assertThat(fakeRootPort.lastPreparedRoot).isEqualTo(result.projectRoot());
    assertThat(fakeRootPort.lastPolicy).isEqualTo(FAIL_IF_EXISTS);

    assertThat(fakeWriter.writtenFiles).containsExactlyInAnyOrder("pom.xml", ".gitignore");
  }

  static class FakeRootPort implements ProjectRootPort {
    Path lastPreparedRoot;
    ProjectRootExistencePolicy lastPolicy;

    @Override
    public Path prepareRoot(Path targetDir, String artifactId, ProjectRootExistencePolicy policy) {
      this.lastPolicy = policy;
      this.lastPreparedRoot = targetDir.resolve(artifactId);
      return lastPreparedRoot;
    }
  }

  static class FakeArtifactsPort implements ProjectArtifactsPort {
    @Override
    public Iterable<? extends GeneratedFile> generate(ProjectBlueprint blueprint) {
      return List.of(
              new GeneratedFile.Text(Path.of("pom.xml"), "<project/>", StandardCharsets.UTF_8),
              new GeneratedFile.Text(Path.of(".gitignore"), "*.class", StandardCharsets.UTF_8));
    }
  }

  static class FakeWriterPort implements ProjectWriterPort {
    final List<String> writtenFiles = new java.util.ArrayList<>();

    @Override
    public void writeBytes(Path projectRoot, Path relativePath, byte[] content) {
      throw new UnsupportedOperationException("bytes not expected");
    }

    @Override
    public void writeText(
            Path projectRoot, Path relativePath, String content, java.nio.charset.Charset charset) {
      writtenFiles.add(relativePath.toString());
    }
  }
}