package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.source;

import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.SourceLayoutPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedDirectory;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SourceLayoutAdapter implements SourceLayoutPort {

  private static final Path SRC_MAIN_JAVA = Path.of("src/main/java");
  private static final Path SRC_TEST_JAVA = Path.of("src/test/java");
  private static final Path SRC_MAIN_RESOURCES = Path.of("src/main/resources");
  private static final Path SRC_TEST_RESOURCES = Path.of("src/test/resources");

  private static final String SEGMENT_ADAPTER = "adapter";
  private static final String SEGMENT_APPLICATION = "application";
  private static final String SEGMENT_BOOTSTRAP = "bootstrap";
  private static final String SEGMENT_DOMAIN = "domain";

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.SOURCE_LAYOUT;
  }

  @Override
  public Iterable<? extends GeneratedResource> generate(ProjectBlueprint blueprint) {
    List<GeneratedResource> resources = new ArrayList<>();

    resources.add(new GeneratedDirectory(SRC_MAIN_JAVA));
    resources.add(new GeneratedDirectory(SRC_TEST_JAVA));
    resources.add(new GeneratedDirectory(SRC_MAIN_RESOURCES));
    resources.add(new GeneratedDirectory(SRC_TEST_RESOURCES));

    PackageName packageName = blueprint.getMetadata().packageName();
    String packagePath = packageName.value().replace('.', '/');

    Path mainBasePackageDir = SRC_MAIN_JAVA.resolve(packagePath);
    Path testBasePackageDir = SRC_TEST_JAVA.resolve(packagePath);

    resources.add(new GeneratedDirectory(mainBasePackageDir));
    resources.add(new GeneratedDirectory(testBasePackageDir));

    ProjectLayout layout = blueprint.getArchitecture().layout();
    if (layout.isHexagonal()) {
      resources.add(new GeneratedDirectory(mainBasePackageDir.resolve(SEGMENT_ADAPTER)));
      resources.add(new GeneratedDirectory(mainBasePackageDir.resolve(SEGMENT_APPLICATION)));
      resources.add(new GeneratedDirectory(mainBasePackageDir.resolve(SEGMENT_BOOTSTRAP)));
      resources.add(new GeneratedDirectory(mainBasePackageDir.resolve(SEGMENT_DOMAIN)));
    }

    return List.copyOf(resources);
  }
}
