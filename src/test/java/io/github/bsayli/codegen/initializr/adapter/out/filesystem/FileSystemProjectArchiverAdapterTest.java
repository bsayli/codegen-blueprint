package io.github.bsayli.codegen.initializr.adapter.out.filesystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.adapter.error.exception.ProjectArchiveInvalidRootException;
import io.github.bsayli.codegen.initializr.application.port.out.archive.ProjectArchiverPort;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@Tag("unit")
@Tag("adapter")
class FileSystemProjectArchiverAdapterTest {

  private final ProjectArchiverPort archiver = new FileSystemProjectArchiverAdapter();

  private static List<String> zipEntries(ZipFile zipFile) {
    Enumeration<? extends ZipEntry> entries = zipFile.entries();
    List<String> names = new java.util.ArrayList<>();
    while (entries.hasMoreElements()) {
      names.add(entries.nextElement().getName());
    }
    return names;
  }

  @Test
  @DisplayName("archive() should create zip with artifactId as root directory and include files")
  void archive_shouldCreateZipWithArtifactIdRoot(@TempDir Path tempDir) throws IOException {
    Path projectRoot = Files.createDirectory(tempDir.resolve("demo-app"));

    Path mainJava = projectRoot.resolve("src/main/java");
    Files.createDirectories(mainJava);
    Files.writeString(mainJava.resolve("App.java"), "class App {}", StandardCharsets.UTF_8);

    Path testJava = projectRoot.resolve("src/test/java");
    Files.createDirectories(testJava);
    Files.writeString(testJava.resolve("AppTest.java"), "class AppTest {}", StandardCharsets.UTF_8);

    Path archivePath = archiver.archive(projectRoot, "my-artifact");

    assertThat(archivePath).isEqualTo(tempDir.resolve("my-artifact.zip"));
    assertThat(Files.exists(archivePath)).isTrue();

    try (ZipFile zipFile = new ZipFile(archivePath.toFile())) {
      List<String> entryNames = zipEntries(zipFile);
      assertThat(entryNames)
          .contains("my-artifact/")
          .anySatisfy(name -> assertThat(name).startsWith("my-artifact/src/"))
          .contains("my-artifact/src/main/java/App.java")
          .contains("my-artifact/src/test/java/AppTest.java");
    }
  }

  @Test
  @DisplayName("archive() should fall back to directory name when artifactId is null")
  void archive_shouldUseDirectoryNameWhenArtifactIdNull(@TempDir Path tempDir) throws IOException {
    Path projectRoot = Files.createDirectory(tempDir.resolve("demo-app"));

    Files.writeString(projectRoot.resolve("README.md"), "# Demo", StandardCharsets.UTF_8);

    Path archivePath = archiver.archive(projectRoot, null);

    assertThat(archivePath).isEqualTo(tempDir.resolve("demo-app.zip"));
    assertThat(Files.exists(archivePath)).isTrue();

    try (ZipFile zipFile = new ZipFile(archivePath.toFile())) {
      List<String> entryNames = zipEntries(zipFile);
      assertThat(entryNames).contains("demo-app/").contains("demo-app/README.md");
    }
  }

  @Test
  @DisplayName("archive() should throw ProjectArchiveInvalidRootException when root is null")
  void archive_shouldThrowWhenRootIsNull() {
    assertThatThrownBy(() -> archiver.archive(null, "anything"))
        .isInstanceOf(ProjectArchiveInvalidRootException.class);
  }

  @Test
  @DisplayName("archive() should throw ProjectArchiveInvalidRootException when root has no parent")
  void archive_shouldThrowWhenRootHasNoParent(@TempDir Path tempDir) {
    Path rootWithoutParent = tempDir.getRoot();

    assertThatThrownBy(() -> archiver.archive(rootWithoutParent, "artifact"))
        .isInstanceOf(ProjectArchiveInvalidRootException.class);
  }

  @Test
  @DisplayName(
      "archive() should throw ProjectArchiveInvalidRootException when path is not a directory")
  void archive_shouldThrowWhenNotDirectory(@TempDir Path tempDir) throws IOException {
    Path fileAsRoot = Files.createFile(tempDir.resolve("not-a-directory.txt"));

    assertThatThrownBy(() -> archiver.archive(fileAsRoot, "artifact"))
        .isInstanceOf(ProjectArchiveInvalidRootException.class);
  }
}
