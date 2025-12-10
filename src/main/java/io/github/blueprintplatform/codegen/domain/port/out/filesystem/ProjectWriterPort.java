package io.github.blueprintplatform.codegen.domain.port.out.filesystem;

import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedBinaryResource;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedDirectory;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedTextResource;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface ProjectWriterPort {

  void writeBytes(Path projectRoot, Path relativePath, byte[] content);

  void writeText(Path projectRoot, Path relativePath, String content, Charset charset);

  void createDirectories(Path projectRoot, Path relativeDir);

  default void writeText(Path root, Path relative, String content) {
    writeText(root, relative, content, StandardCharsets.UTF_8);
  }

  default void write(Path projectRoot, GeneratedResource resource) {
    switch (resource) {
      case GeneratedTextResource text ->
          writeText(projectRoot, text.relativePath(), text.content(), text.charset());
      case GeneratedBinaryResource binary ->
          writeBytes(projectRoot, binary.relativePath(), binary.bytes());
      case GeneratedDirectory dir -> createDirectories(projectRoot, dir.relativePath());
      default -> throw new IllegalArgumentException("Unsupported resource type: " + resource);
    }
  }

  default void write(Path projectRoot, Iterable<? extends GeneratedResource> resources) {
    for (GeneratedResource r : resources) {
      write(projectRoot, r);
    }
  }

  default void write(Path projectRoot, GeneratedResource... resources) {
    for (GeneratedResource r : resources) {
      write(projectRoot, r);
    }
  }

  default void write(Path projectRoot, Stream<? extends GeneratedResource> resources) {
    resources.forEach(r -> write(projectRoot, r));
  }
}
