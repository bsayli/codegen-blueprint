package io.github.bsayli.codegen.initializr.projectgeneration.ports;

import java.io.IOException;
import java.nio.file.Path;

public interface ProjectDirectoryInitializer {

  Path initializeProjectDirectory(String projectName) throws IOException;

  Path initializeProjectDirectory(String projectName, Path projectLocation) throws IOException;
}
