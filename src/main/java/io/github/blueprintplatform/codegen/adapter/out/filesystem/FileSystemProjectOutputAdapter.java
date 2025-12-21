package io.github.blueprintplatform.codegen.adapter.out.filesystem;

import io.github.blueprintplatform.codegen.adapter.error.exception.filesystem.ProjectOutputDiscoveryException;
import io.github.blueprintplatform.codegen.application.port.out.output.ProjectOutputItem;
import io.github.blueprintplatform.codegen.application.port.out.output.ProjectOutputPort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

public class FileSystemProjectOutputAdapter implements ProjectOutputPort {

  private static final Set<String> EXECUTABLE_FILE_NAMES = Set.of("mvnw", "gradlew");
  private static final Set<String> EXECUTABLE_EXTENSIONS = Set.of(".sh", ".cmd", ".bat");
  private static final Set<String> BINARY_EXTENSIONS =
      Set.of(".jar", ".zip", ".png", ".jpg", ".jpeg", ".gif", ".pdf", ".ico");

  @Override
  public List<ProjectOutputItem> list(Path projectRoot) {
    if (projectRoot == null) {
      return List.of();
    }

    Path normalizedRoot = projectRoot.toAbsolutePath().normalize();

    if (!Files.exists(normalizedRoot) || !Files.isDirectory(normalizedRoot)) {
      return List.of();
    }

    try (Stream<Path> paths = Files.walk(normalizedRoot)) {
      return paths.filter(Files::isRegularFile).map(path -> toItem(normalizedRoot, path)).toList();
    } catch (IOException e) {
      throw new ProjectOutputDiscoveryException(normalizedRoot, e);
    }
  }

  private ProjectOutputItem toItem(Path normalizedRoot, Path absolutePath) {
    Path relativePath = normalizedRoot.relativize(absolutePath);

    boolean executable = isExecutableFile(relativePath);
    boolean binary = isBinaryFile(relativePath);

    return new ProjectOutputItem(relativePath, binary, executable);
  }

  private boolean isExecutableFile(Path relativePath) {
    return hasExecutableName(relativePath) || hasExecutableExtension(relativePath);
  }

  private boolean isBinaryFile(Path relativePath) {
    String name = fileNameLower(relativePath);
    return BINARY_EXTENSIONS.stream().anyMatch(name::endsWith);
  }

  private boolean hasExecutableName(Path relativePath) {
    return EXECUTABLE_FILE_NAMES.contains(fileNameLower(relativePath));
  }

  private boolean hasExecutableExtension(Path relativePath) {
    String name = fileNameLower(relativePath);
    return EXECUTABLE_EXTENSIONS.stream().anyMatch(name::endsWith);
  }

  private String fileNameLower(Path relativePath) {
    Path fileName = relativePath.getFileName();
    String raw = fileName != null ? fileName.toString() : relativePath.toString();
    return raw.toLowerCase(Locale.ROOT);
  }
}
