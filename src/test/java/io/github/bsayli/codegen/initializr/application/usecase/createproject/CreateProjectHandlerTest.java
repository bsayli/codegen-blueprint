package io.github.bsayli.codegen.initializr.application.usecase.createproject;

import static io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootExistencePolicy.FAIL_IF_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsSelector;
import io.github.bsayli.codegen.initializr.application.port.out.archive.ProjectArchiverPort;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootExistencePolicy;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootPort;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectWriterPort;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@Tag("unit")
@Tag("application")
@DisplayName("Unit Test: CreateProjectHandler")
class CreateProjectHandlerTest {

  @TempDir Path tempDir;

  @Test
  @DisplayName("handle() prepares project root, writes artifacts, and returns archive path")
  void handle_prepares_root_writes_artifacts_and_archives() {
    var mapper = new ProjectBlueprintMapper();
    var fakeRootPort = new FakeRootPort();
    var fakeArtifacts = new FakeArtifactsPort();
    var fakeSelector = new FakeSelector(fakeArtifacts);
    var fakeWriter = new FakeWriterPort();
    var fakeArchiver = new FakeArchiverPort();

    var handler =
        new CreateProjectHandler(mapper, fakeRootPort, fakeSelector, fakeWriter, fakeArchiver);

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

    var result = handler.handle(cmd);

    assertThat(result.archivePath()).hasFileName("demo-app.zip");
    assertThat(fakeArchiver.lastProjectRoot).isEqualTo(fakeRootPort.lastPreparedRoot);
    assertThat(fakeArchiver.lastArtifactId).isEqualTo("demo-app");

    assertThat(fakeRootPort.lastPreparedRoot).isEqualTo(tempDir.resolve("demo-app"));
    assertThat(fakeRootPort.lastPolicy).isEqualTo(FAIL_IF_EXISTS);

    assertThat(fakeWriter.writtenFiles)
        .containsExactlyInAnyOrderElementsOf(fakeArtifacts.lastEmittedRelativePaths)
        .hasSize(fakeArtifacts.lastEmittedRelativePaths.size());
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

  static class FakeSelector implements ProjectArtifactsSelector {
    private final ProjectArtifactsPort delegate;

    FakeSelector(ProjectArtifactsPort delegate) {
      this.delegate = delegate;
    }

    @Override
    public ProjectArtifactsPort select(BuildOptions options) {
      return delegate;
    }
  }

  static class FakeArtifactsPort implements ProjectArtifactsPort {
    final List<Path> lastEmittedRelativePaths = new ArrayList<>();

    @Override
    public Iterable<? extends GeneratedFile> generate(ProjectBlueprint bp) {
      var files =
          List.of(
              new GeneratedFile.Text(Path.of("pom.xml"), "<project/>", StandardCharsets.UTF_8),
              new GeneratedFile.Text(Path.of(".gitignore"), "*.class", StandardCharsets.UTF_8),
              new GeneratedFile.Text(
                  Path.of("src/main/java/com/acme/demo/DemoApplication.java"),
                  "class DemoApplication {}",
                  StandardCharsets.UTF_8),
              new GeneratedFile.Text(
                  Path.of("src/test/java/com/acme/demo/DemoApplicationTests.java"),
                  "class DemoApplicationTests {}",
                  StandardCharsets.UTF_8),
              new GeneratedFile.Text(
                  Path.of("src/main/resources/application.yml"),
                  "spring:\n  application:\n    name: demo-app",
                  StandardCharsets.UTF_8),
              new GeneratedFile.Text(
                  Path.of("README.md"),
                  "# Demo App\n\nThis project was generated by Codegen Initializr.",
                  StandardCharsets.UTF_8));

      lastEmittedRelativePaths.clear();
      for (var gf : files) {
        lastEmittedRelativePaths.add(gf.relativePath());
      }
      return files;
    }
  }

  static class FakeWriterPort implements ProjectWriterPort {
    final List<Path> writtenFiles = new ArrayList<>();

    @Override
    public void writeBytes(Path projectRoot, Path relativePath, byte[] content) {
      throw new UnsupportedOperationException("bytes not expected");
    }

    @Override
    public void writeText(Path projectRoot, Path relativePath, String content, Charset charset) {
      writtenFiles.add(relativePath);
    }
  }

  static class FakeArchiverPort implements ProjectArchiverPort {
    Path lastProjectRoot;
    String lastArtifactId;

    @Override
    public Path archive(Path projectRoot, String artifactId) {
      this.lastProjectRoot = projectRoot;
      this.lastArtifactId = artifactId;
      return projectRoot.getParent().resolve(artifactId + ".zip");
    }
  }
}
