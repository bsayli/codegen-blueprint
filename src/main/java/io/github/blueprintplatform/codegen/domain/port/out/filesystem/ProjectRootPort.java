package io.github.blueprintplatform.codegen.domain.port.out.filesystem;

import java.nio.file.Path;

public interface ProjectRootPort {

  Path prepareRoot(Path targetDir, String artifactId, ProjectRootExistencePolicy policy);
}
