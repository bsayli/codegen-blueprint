package io.github.blueprintplatform.codegen.adapter.out.framework.springboot.java.source;

import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.SourceLayoutPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedDirectory;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SourceLayoutAdapter implements SourceLayoutPort {

  private static final Path SRC_MAIN_JAVA = Path.of("src", "main", "java");
  private static final Path SRC_TEST_JAVA = Path.of("src", "test", "java");
  private static final Path SRC_MAIN_RESOURCES = Path.of("src", "main", "resources");
  private static final Path SRC_TEST_RESOURCES = Path.of("src", "test", "resources");

  private static final List<Path> COMMON_DIRS =
      List.of(SRC_MAIN_JAVA, SRC_TEST_JAVA, SRC_MAIN_RESOURCES, SRC_TEST_RESOURCES);

  private static final List<Path> HEX_DIRS =
      List.of(Path.of("adapter"), Path.of("application"), Path.of("bootstrap"), Path.of("domain"));

  private static final List<Path> STANDARD_DIRS =
      List.of(
          Path.of("controller"),
          Path.of("service"),
          Path.of("repository"),
          Path.of("config"),
          Path.of("domain"),
          Path.of("domain", "model"));

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.SOURCE_LAYOUT;
  }

  @Override
  public Iterable<? extends GeneratedResource> generate(ProjectBlueprint blueprint) {
    Path mainBasePackageDir =
        resolveBasePackageDir(SRC_MAIN_JAVA, blueprint.getMetadata().packageName());
    Path testBasePackageDir =
        resolveBasePackageDir(SRC_TEST_JAVA, blueprint.getMetadata().packageName());

    List<GeneratedResource> resources = new ArrayList<>();

    addDirectories(resources, COMMON_DIRS);
    addDirectories(resources, List.of(mainBasePackageDir, testBasePackageDir));

    var segments = blueprint.getArchitecture().layout().isHexagonal() ? HEX_DIRS : STANDARD_DIRS;
    addDirectoriesUnder(resources, mainBasePackageDir, segments);
    return List.copyOf(resources);
  }

  private Path resolveBasePackageDir(Path javaRoot, PackageName packageName) {
    String[] parts = packageName.value().split("\\.");
    Path pkgPath = Path.of(parts[0], Arrays.copyOfRange(parts, 1, parts.length));
    return javaRoot.resolve(pkgPath);
  }

  private void addDirectories(List<GeneratedResource> out, List<Path> dirs) {
    dirs.stream().map(GeneratedDirectory::new).forEach(out::add);
  }

  private void addDirectoriesUnder(List<GeneratedResource> out, Path base, List<Path> segments) {
    segments.stream().map(base::resolve).map(GeneratedDirectory::new).forEach(out::add);
  }
}
