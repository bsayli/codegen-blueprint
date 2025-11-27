package io.github.bsayli.codegen.initializr.adapter.out.filesystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.adapter.error.exception.ProjectWriteException;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectWriterPort;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class FileSystemProjectWriterAdapterTest {

  private final ProjectWriterPort writer = new FileSystemProjectWriterAdapter();

  @Test
  @DisplayName("writeBytes() should create parent directories and write file")
  void writeBytes_shouldCreateDirsAndWrite() throws IOException {
    Path temp = Files.createTempDirectory("writer-test");
    Path relative = Path.of("a/b/c.txt");

    byte[] content = "hello-bytes".getBytes(StandardCharsets.UTF_8);

    writer.writeBytes(temp, relative, content);

    Path target = temp.resolve(relative);
    assertThat(Files.exists(target)).isTrue();
    assertThat(Files.readString(target)).isEqualTo("hello-bytes");
  }

  @Test
  @DisplayName("writeBytes() should overwrite existing file")
  void writeBytes_shouldOverwriteExistingFile() throws IOException {
    Path temp = Files.createTempDirectory("writer-test2");
    Path relative = Path.of("file.txt");

    Files.writeString(temp.resolve(relative), "old");

    writer.writeBytes(temp, relative, "new".getBytes(StandardCharsets.UTF_8));

    assertThat(Files.readString(temp.resolve(relative))).isEqualTo("new");
  }

  @Test
  @DisplayName("writeText() should write file with provided charset")
  void writeText_shouldWriteWithCharset() throws IOException {
    Path temp = Files.createTempDirectory("writer-test3");
    Path relative = Path.of("utf16.txt");

    writer.writeText(temp, relative, "Merhaba Dünya", StandardCharsets.UTF_16);

    assertThat(Files.readString(temp.resolve(relative), StandardCharsets.UTF_16))
        .isEqualTo("Merhaba Dünya");
  }

  @Test
  @DisplayName("writeBytes() should wrap IOExceptions in ProjectWriteException")
  void writeBytes_shouldWrapIOException() throws IOException {
    Path temp = Files.createTempDirectory("writer-test4");

    // Make directory read-only to force IOException
    File root = temp.toFile();
    assertThat(root.setWritable(false)).isTrue();

    Path relative = Path.of("fail/here.txt");
    byte[] content = "boom".getBytes(StandardCharsets.UTF_8);

    assertThatThrownBy(() -> writer.writeBytes(temp, relative, content))
        .isInstanceOf(ProjectWriteException.class);

    // Restore permission for cleanup
    assertThat(root.setWritable(true)).isTrue();
  }
}
