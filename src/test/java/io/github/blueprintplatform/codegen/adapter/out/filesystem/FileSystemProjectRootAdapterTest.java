package io.github.blueprintplatform.codegen.adapter.out.filesystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.blueprintplatform.codegen.adapter.error.exception.ProjectRootAlreadyExistsException;
import io.github.blueprintplatform.codegen.adapter.error.exception.ProjectRootIOException;
import io.github.blueprintplatform.codegen.adapter.error.exception.ProjectRootNotDirectoryException;
import io.github.blueprintplatform.codegen.domain.port.out.filesystem.ProjectRootExistencePolicy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@Tag("unit")
@Tag("adapter")
class FileSystemProjectRootAdapterTest {

  private final FileSystemProjectRootAdapter adapter = new FileSystemProjectRootAdapter();
  @TempDir Path tempDir;

  @Test
  @DisplayName("Should create directory when project root does not exist")
  void shouldCreateDirectoryWhenNotExists() {
    Path result =
        adapter.prepareRoot(tempDir, "demo-app", ProjectRootExistencePolicy.FAIL_IF_EXISTS);

    assertThat(result).exists().isDirectory();
    assertThat(result.getFileName().toString()).hasToString("demo-app");
  }

  @Test
  @DisplayName(
      "Should throw ProjectRootAlreadyExistsException when directory exists and policy=FAIL_IF_EXISTS")
  void shouldFailIfExists() throws IOException {
    Path existing = tempDir.resolve("demo-app");
    Files.createDirectories(existing);

    assertThatThrownBy(
            () ->
                adapter.prepareRoot(tempDir, "demo-app", ProjectRootExistencePolicy.FAIL_IF_EXISTS))
        .isInstanceOf(ProjectRootAlreadyExistsException.class);
  }

  @Test
  @DisplayName("Should return directory when exists and policy=OVERWRITE")
  void shouldReturnExistingDirWhenOverwrite() throws IOException {
    Path existing = tempDir.resolve("demo-app");
    Files.createDirectories(existing);

    Path result = adapter.prepareRoot(tempDir, "demo-app", ProjectRootExistencePolicy.OVERWRITE);

    assertThat(result).isEqualTo(existing);
  }

  @Test
  @DisplayName("Should throw ProjectRootNotDirectoryException when exists and is a file")
  void shouldThrowIfExistsButNotDirectory() throws IOException {
    Path file = tempDir.resolve("demo-app");
    Files.writeString(file, "not a directory");

    assertThatThrownBy(
            () ->
                adapter.prepareRoot(tempDir, "demo-app", ProjectRootExistencePolicy.FAIL_IF_EXISTS))
        .isInstanceOf(ProjectRootNotDirectoryException.class);
  }

  @Test
  @DisplayName("Should wrap IO errors in ProjectRootIOException")
  void shouldWrapIOException() throws IOException {
    Path targetDir = Files.createTempDirectory("locked");
    File dir = targetDir.toFile();

    assertThat(dir.setReadable(true)).isTrue();
    assertThat(dir.setWritable(false)).isTrue();
    assertThat(dir.setExecutable(true)).isTrue();

    assertThatThrownBy(
            () -> adapter.prepareRoot(targetDir, "demo-app", ProjectRootExistencePolicy.OVERWRITE))
        .isInstanceOf(ProjectRootIOException.class);
  }
}
