package io.github.bsayli.codegen.initializr.application.usecase.createproject;

import static io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootExistencePolicy.FAIL_IF_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
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
@DisplayName("CreateProjectService")
class CreateProjectServiceTest {

  private static final Path POM = Path.of("pom.xml");
  private static final Path GITIGNORE = Path.of(".gitignore");
  private static final Path APP_YML = Path.of("src/main/resources/application.yml");
  private static final Path WRAPPER_PROPS = Path.of(".mvn/wrapper/maven-wrapper.properties");
  private static final Path README = Path.of("README.md");

  @TempDir Path tempDir;

  private static Path appClass(ProjectBlueprint bp) {
    String pkg = bp.getPackageName().value().replace('.', '/');
    return Path.of("src/main/java", pkg, "DemoApplication.java");
  }

  private static Path testClass(ProjectBlueprint bp) {
    String pkg = bp.getPackageName().value().replace('.', '/');
    return Path.of("src/test/java", pkg, "DemoApplicationTests.java");
  }

  @Test
  @DisplayName("execute() prepares project root, writes artifacts, and returns archive path")
  void creates_project_root_writes_artifacts_and_archives() {
    var mapper = new ProjectBlueprintMapper();
    var fakeRootPort = new FakeRootPort();
    var fakeArtifacts = new FakeArtifactsPort();
    var fakeWriter = new FakeWriterPort();
    var fakeArchiver = new FakeArchiverPort();

    var service =
        new CreateProjectService(mapper, fakeRootPort, fakeArtifacts, fakeWriter, fakeArchiver);

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

    assertThat(result.archivePath()).hasFileName("demo-app.zip");
    assertThat(fakeArchiver.lastProjectRoot).isEqualTo(fakeRootPort.lastPreparedRoot);
    assertThat(fakeArchiver.lastArtifactId).isEqualTo("demo-app");

    assertThat(fakeRootPort.lastPreparedRoot).isEqualTo(tempDir.resolve("demo-app"));
    assertThat(fakeRootPort.lastPolicy).isEqualTo(FAIL_IF_EXISTS);

    var bpForPaths = mapper.toDomain(cmd);
    var expected =
        List.of(
            POM,
            GITIGNORE,
            appClass(bpForPaths),
            testClass(bpForPaths),
            APP_YML,
            WRAPPER_PROPS,
            README);

    assertThat(fakeWriter.writtenFiles)
        .containsExactlyInAnyOrderElementsOf(expected)
        .hasSize(expected.size());
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
    public Iterable<? extends GeneratedFile> generate(ProjectBlueprint bp) {
      return List.of(
          new GeneratedFile.Text(POM, "<project/>", StandardCharsets.UTF_8),
          new GeneratedFile.Text(GITIGNORE, "*.class", StandardCharsets.UTF_8),
          new GeneratedFile.Text(appClass(bp), "class DemoApplication {}", StandardCharsets.UTF_8),
          new GeneratedFile.Text(
              testClass(bp), "class DemoApplicationTests {}", StandardCharsets.UTF_8),
          new GeneratedFile.Text(
              APP_YML, "spring:\n  application:\n    name: demo-app", StandardCharsets.UTF_8),
          new GeneratedFile.Text(
              WRAPPER_PROPS,
              "distributionUrl=https://repo.maven.apache.org/maven2/...",
              StandardCharsets.UTF_8),
          new GeneratedFile.Text(
              README,
              "# Demo App\n\nThis project was generated by Codegen Initializr.",
              StandardCharsets.UTF_8));
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
