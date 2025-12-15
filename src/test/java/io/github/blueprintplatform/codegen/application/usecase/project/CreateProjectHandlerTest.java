package io.github.blueprintplatform.codegen.application.usecase.project;

import static io.github.blueprintplatform.codegen.domain.port.out.filesystem.ProjectRootExistencePolicy.FAIL_IF_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.application.port.in.project.dto.CreateProjectRequest;
import io.github.blueprintplatform.codegen.application.port.in.project.dto.GeneratedFileSummary;
import io.github.blueprintplatform.codegen.application.port.out.ProjectArtifactsPort;
import io.github.blueprintplatform.codegen.application.port.out.ProjectArtifactsSelector;
import io.github.blueprintplatform.codegen.application.port.out.archive.ProjectArchiverPort;
import io.github.blueprintplatform.codegen.application.usecase.project.context.CreateProjectExecutionContext;
import io.github.blueprintplatform.codegen.application.usecase.project.mapper.CreateProjectResponseMapper;
import io.github.blueprintplatform.codegen.application.usecase.project.mapper.ProjectBlueprintMapper;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.EnforcementMode;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedTextResource;
import io.github.blueprintplatform.codegen.domain.port.out.filesystem.ProjectFileListingPort;
import io.github.blueprintplatform.codegen.domain.port.out.filesystem.ProjectRootExistencePolicy;
import io.github.blueprintplatform.codegen.domain.port.out.filesystem.ProjectRootPort;
import io.github.blueprintplatform.codegen.domain.port.out.filesystem.ProjectWriterPort;
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
class CreateProjectHandlerTest {

  @TempDir Path tempDir;

  @Test
  @DisplayName(
      "handle() prepares project root, writes artifacts, lists files, and returns archive + summary")
  void handle_prepares_root_writes_artifacts_lists_files_and_archives() {
    var blueprintMapper = new ProjectBlueprintMapper();
    var responseMapper = new CreateProjectResponseMapper();

    var fakeRootPort = new FakeRootPort();
    var fakeArtifacts = new FakeArtifactsPort();
    var fakeSelector = new FakeSelector(fakeArtifacts);
    var fakeWriter = new FakeWriterPort();
    var fakeFileListing = new FakeFileListingPort(fakeArtifacts);
    var fakeArchiver = new FakeArchiverPort();

    var executionContext =
        new CreateProjectExecutionContext(
            fakeRootPort, fakeSelector, fakeWriter, fakeFileListing, fakeArchiver);

    var handler = new CreateProjectHandler(blueprintMapper, responseMapper, executionContext);

    var createProjectRequest = getCreateProjectRequest();

    var result = handler.handle(createProjectRequest);

    assertThat(result.archivePath()).hasFileName("demo-app.zip");
    assertThat(result.projectRoot()).isEqualTo(tempDir.resolve("demo-app"));

    assertThat(fakeArchiver.lastProjectRoot).isEqualTo(fakeRootPort.lastPreparedRoot);
    assertThat(fakeArchiver.lastArtifactId).isEqualTo("demo-app");

    assertThat(fakeRootPort.lastPreparedRoot).isEqualTo(tempDir.resolve("demo-app"));
    assertThat(fakeRootPort.lastPolicy).isEqualTo(FAIL_IF_EXISTS);

    assertThat(fakeFileListing.lastProjectRoot).isEqualTo(fakeRootPort.lastPreparedRoot);

    assertThat(fakeWriter.writtenFiles)
        .containsExactlyInAnyOrderElementsOf(fakeArtifacts.lastEmittedRelativePaths)
        .hasSize(fakeArtifacts.lastEmittedRelativePaths.size());

    assertThat(result.project().groupId()).isEqualTo("com.acme");
    assertThat(result.project().artifactId()).isEqualTo("demo-app");
    assertThat(result.project().projectName()).isEqualTo("Demo App");
    assertThat(result.project().projectDescription()).isEqualTo("Demo project");
    assertThat(result.project().packageName()).isEqualTo("com.acme.demo");
    assertThat(result.project().layout()).isEqualTo(ProjectLayout.STANDARD);
    assertThat(result.project().enforcementMode()).isEqualTo(EnforcementMode.NONE);
    assertThat(result.project().sampleCode()).isEqualTo(SampleCodeOptions.none());

    assertThat(result.files())
        .extracting(GeneratedFileSummary::relativePath)
        .containsExactlyInAnyOrderElementsOf(fakeArtifacts.lastEmittedRelativePaths);

    assertThat(result.files()).allSatisfy(f -> assertThat(f.executable()).isFalse());
    assertThat(result.files()).allSatisfy(f -> assertThat(f.binary()).isFalse());
  }

  private CreateProjectRequest getCreateProjectRequest() {
    var techStack = new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);
    var platformTarget = new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5);

    return new CreateProjectRequest(
        "com.acme",
        "demo-app",
        "Demo App",
        "Demo project",
        "com.acme.demo",
        techStack,
        ProjectLayout.STANDARD,
        EnforcementMode.NONE,
        platformTarget,
        List.of(),
        SampleCodeOptions.none(),
        tempDir);
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
    public ProjectArtifactsPort select(TechStack options) {
      return delegate;
    }
  }

  static class FakeArtifactsPort implements ProjectArtifactsPort {
    final List<Path> lastEmittedRelativePaths = new ArrayList<>();

    @Override
    public Iterable<? extends GeneratedResource> generate(ProjectBlueprint bp) {
      var files =
          List.of(
              new GeneratedTextResource(Path.of("pom.xml"), "<project/>", StandardCharsets.UTF_8),
              new GeneratedTextResource(Path.of(".gitignore"), "*.class", StandardCharsets.UTF_8),
              new GeneratedTextResource(
                  Path.of("src/main/java/com/acme/demo/DemoApplication.java"),
                  "class DemoApplication {}",
                  StandardCharsets.UTF_8),
              new GeneratedTextResource(
                  Path.of("src/test/java/com/acme/demo/DemoApplicationTests.java"),
                  "class DemoApplicationTests {}",
                  StandardCharsets.UTF_8),
              new GeneratedTextResource(
                  Path.of("src/main/resources/application.yml"),
                  "spring:\n  application:\n    name: demo-app",
                  StandardCharsets.UTF_8),
              new GeneratedTextResource(
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
      writtenFiles.add(relativePath);
    }

    @Override
    public void writeText(Path projectRoot, Path relativePath, String content, Charset charset) {
      writtenFiles.add(relativePath);
    }

    @Override
    public void createDirectories(Path projectRoot, Path relativeDir) {
      // noop
    }
  }

  static class FakeFileListingPort implements ProjectFileListingPort {
    private final FakeArtifactsPort artifactsPort;
    Path lastProjectRoot;

    FakeFileListingPort(FakeArtifactsPort artifactsPort) {
      this.artifactsPort = artifactsPort;
    }

    @Override
    public List<Path> listFiles(Path projectRoot) {
      this.lastProjectRoot = projectRoot;
      return List.copyOf(artifactsPort.lastEmittedRelativePaths);
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
