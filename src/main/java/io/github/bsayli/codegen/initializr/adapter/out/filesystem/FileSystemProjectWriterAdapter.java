package io.github.bsayli.codegen.initializr.adapter.out.filesystem;

import io.github.bsayli.codegen.initializr.adapter.error.exception.ProjectWriteException;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectWriterPort;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileSystemProjectWriterAdapter implements ProjectWriterPort {

  @Override
  public void writeBytes(Path projectRoot, Path relativePath, byte[] content) {
    Path target = projectRoot.resolve(relativePath);
    try {
      Path parent = target.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      Files.write(
          target,
          content,
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING,
          StandardOpenOption.WRITE);
    } catch (IOException e) {
      throw new ProjectWriteException(target, e);
    }
  }

  @Override
  public void writeText(Path projectRoot, Path relativePath, String content, Charset charset) {
    byte[] bytes = content.getBytes(charset);
    writeBytes(projectRoot, relativePath, bytes);
  }
}
