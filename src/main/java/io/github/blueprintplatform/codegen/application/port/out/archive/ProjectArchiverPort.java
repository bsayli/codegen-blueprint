package io.github.blueprintplatform.codegen.application.port.out.archive;

import java.nio.file.Path;

public interface ProjectArchiverPort {
  Path archive(Path projectRoot, String artifactId);
}
