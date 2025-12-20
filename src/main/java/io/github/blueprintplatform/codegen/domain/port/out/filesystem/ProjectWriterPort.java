package io.github.blueprintplatform.codegen.domain.port.out.filesystem;

import io.github.blueprintplatform.codegen.domain.port.out.artifact.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface ProjectWriterPort {

  void writeBytes(Path projectRoot, Path relativePath, byte[] content);

  void writeText(Path projectRoot, Path relativePath, String content, Charset charset);

  void createDirectories(Path projectRoot, Path relativeDir);

  default void writeText(Path projectRoot, Path relativePath, String content) {
    writeText(projectRoot, relativePath, content, StandardCharsets.UTF_8);
  }

  default void write(Path projectRoot, GeneratedResource resource) {
    switch (resource) {
      case GeneratedTextResource(Path relativePath, String content, Charset charset) ->
              writeText(projectRoot, relativePath, content, charset);

      case GeneratedBinaryResource(Path relativePath, BinaryContent content) ->
              writeBytes(projectRoot, relativePath, content.bytes());

      case GeneratedDirectory(Path relativePath) ->
              createDirectories(projectRoot, relativePath);
    }
  }

  default void write(Path projectRoot, Iterable<? extends GeneratedResource> resources) {
    for (GeneratedResource resource : resources) {
      write(projectRoot, resource);
    }
  }

  default void write(Path projectRoot, GeneratedResource... resources) {
    for (GeneratedResource resource : resources) {
      write(projectRoot, resource);
    }
  }

  default void write(Path projectRoot, Stream<? extends GeneratedResource> resources) {
    resources.forEach(resource -> write(projectRoot, resource));
  }
}
