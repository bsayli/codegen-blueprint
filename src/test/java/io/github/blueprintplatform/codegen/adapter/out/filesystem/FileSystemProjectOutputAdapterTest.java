package io.github.blueprintplatform.codegen.adapter.out.filesystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import io.github.blueprintplatform.codegen.adapter.error.exception.filesystem.ProjectOutputDiscoveryException;
import io.github.blueprintplatform.codegen.application.port.out.output.ProjectOutputItem;
import io.github.blueprintplatform.codegen.application.port.out.output.ProjectOutputPort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@Tag("unit")
@Tag("adapter")
class FileSystemProjectOutputAdapterTest {

  private final ProjectOutputPort outputPort = new FileSystemProjectOutputAdapter();

  @TempDir Path tempDir;

  @Test
  @DisplayName("list(null) should return empty list")
  void list_nullRoot_shouldReturnEmpty() {
    assertThat(outputPort.list(null)).isEmpty();
  }

  @Test
  @DisplayName("list() should return empty list when root does not exist")
  void list_rootDoesNotExist_shouldReturnEmpty() {
    Path missing = tempDir.resolve("missing-root");
    assertThat(outputPort.list(missing)).isEmpty();
  }

  @Test
  @DisplayName("list() should return empty list when root is not a directory")
  void list_rootIsFile_shouldReturnEmpty() throws IOException {
    Path file = tempDir.resolve("root.txt");
    Files.writeString(file, "x");

    assertThat(outputPort.list(file)).isEmpty();
  }

  @Test
  @DisplayName("list() should return relative paths and detect binary/executable by name/extension")
  void list_shouldReturnRelativeItems_withFlags() throws IOException {
    Files.createDirectories(tempDir.resolve("a/b"));

    Path text = tempDir.resolve("a/b/readme.txt");
    Files.writeString(text, "hello");

    Path binary = tempDir.resolve("a/logo.png");
    Files.write(binary, new byte[] {1, 2, 3});

    Path mvnw = tempDir.resolve("mvnw");
    Files.writeString(mvnw, "#!/bin/sh\necho ok");

    Path script = tempDir.resolve("scripts/run.sh");
    Files.createDirectories(script.getParent());
    Files.writeString(script, "echo hi");

    List<ProjectOutputItem> items = outputPort.list(tempDir);

    assertThat(items)
        .extracting(ProjectOutputItem::relativePath)
        .containsExactlyInAnyOrder(
            Path.of("a/b/readme.txt"),
            Path.of("a/logo.png"),
            Path.of("mvnw"),
            Path.of("scripts/run.sh"));

    ProjectOutputItem readme =
        items.stream()
            .filter(i -> i.relativePath().equals(Path.of("a/b/readme.txt")))
            .findFirst()
            .orElseThrow();
    assertThat(readme.binary()).isFalse();
    assertThat(readme.executable()).isFalse();

    ProjectOutputItem logo =
        items.stream()
            .filter(i -> i.relativePath().equals(Path.of("a/logo.png")))
            .findFirst()
            .orElseThrow();
    assertThat(logo.binary()).isTrue();
    assertThat(logo.executable()).isFalse();

    ProjectOutputItem mvnwItem =
        items.stream()
            .filter(i -> i.relativePath().equals(Path.of("mvnw")))
            .findFirst()
            .orElseThrow();
    assertThat(mvnwItem.binary()).isFalse();
    assertThat(mvnwItem.executable()).isTrue();

    ProjectOutputItem shItem =
        items.stream()
            .filter(i -> i.relativePath().equals(Path.of("scripts/run.sh")))
            .findFirst()
            .orElseThrow();
    assertThat(shItem.binary()).isFalse();
    assertThat(shItem.executable()).isTrue();
  }

  @Test
  @DisplayName(
      "list() should NOT mark executable only because POSIX executable bit is set (convention-only)")
  void list_posixExecutableBit_shouldNotMatter_whenConventionOnly() throws IOException {
    Path bin = tempDir.resolve("bin/tool");
    Files.createDirectories(bin.getParent());
    Files.writeString(bin, "echo ok");

    boolean posixSupported = Files.getFileStore(bin).supportsFileAttributeView("posix");
    assumeTrue(posixSupported, "POSIX permissions not supported on this filesystem");

    Files.setPosixFilePermissions(
        bin,
        Set.of(
            PosixFilePermission.OWNER_READ,
            PosixFilePermission.OWNER_WRITE,
            PosixFilePermission.OWNER_EXECUTE));

    List<ProjectOutputItem> items = outputPort.list(tempDir);

    ProjectOutputItem tool =
        items.stream()
            .filter(i -> i.relativePath().equals(Path.of("bin/tool")))
            .findFirst()
            .orElseThrow();

    assertThat(tool.executable()).isFalse();
  }

  @Test
  @DisplayName("list() should wrap IOExceptions in ProjectOutputDiscoveryException")
  void list_shouldWrapIOException() throws IOException {
    Path lockedDir = tempDir.resolve("locked");
    Files.createDirectories(lockedDir);

    boolean posixSupported = Files.getFileStore(lockedDir).supportsFileAttributeView("posix");
    assumeTrue(posixSupported, "POSIX permissions not supported on this filesystem");

    Set<PosixFilePermission> originalPerms = Files.getPosixFilePermissions(lockedDir);

    try {
      Files.setPosixFilePermissions(lockedDir, Set.of());

      assertThatThrownBy(() -> outputPort.list(lockedDir))
          .isInstanceOf(ProjectOutputDiscoveryException.class);
    } finally {
      Files.setPosixFilePermissions(lockedDir, originalPerms);
    }
  }
}
