package io.github.bsayli.codegen.initializr.domain.port.out.filesystem;

import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.nio.charset.Charset;
import java.nio.file.Path;

public interface ProjectWriterPort {
  void writeBytes(Path projectRoot, Path relativePath, byte[] content);

  void writeText(Path projectRoot, Path relativePath, String content, Charset charset);

  default void writeText(Path root, Path relative, String content) {
    writeText(root, relative, content, java.nio.charset.StandardCharsets.UTF_8);
  }

  default void write(Path projectRoot, GeneratedFile file) {
    switch (file) {
      case GeneratedFile.Text(Path p, String c, Charset cs) -> writeText(projectRoot, p, c, cs);
      case GeneratedFile.Binary(Path p, byte[] b) -> writeBytes(projectRoot, p, b);
    }
  }

  default void write(Path projectRoot, Iterable<? extends GeneratedFile> files) {
    for (GeneratedFile f : files) write(projectRoot, f);
  }

  default void write(Path projectRoot, GeneratedFile... files) {
    for (GeneratedFile f : files) write(projectRoot, f);
  }

  default void write(Path projectRoot, java.util.stream.Stream<? extends GeneratedFile> files) {
    files.forEach(f -> write(projectRoot, f));
  }
}
