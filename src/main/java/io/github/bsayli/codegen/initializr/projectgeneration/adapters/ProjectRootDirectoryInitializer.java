package io.github.bsayli.codegen.initializr.projectgeneration.adapters;

import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectDirectoryInitializer;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

@Component("projectRootDirectoryInitializer")
public class ProjectRootDirectoryInitializer implements ProjectDirectoryInitializer {

  @Override
  public Path initializeProjectDirectory(String projectName) throws IOException {
    Path tempPath = Files.createTempDirectory(projectName);
    Path projectPath = Paths.get(tempPath.toString(), projectName);
    Files.createDirectories(projectPath);
    return projectPath;
  }

  @Override
  public Path initializeProjectDirectory(String projectName, Path projectLocation)
      throws IOException {
    if (projectLocation != null) {
      Path projectDir = projectLocation.resolve(projectName);
      if (Files.exists(projectDir)) {
        throw new FileAlreadyExistsException(projectDir.toString(), null, "File already exists!");
      }
      Files.createDirectories(projectDir); // Create directories recursively
      return projectDir;
    } else {
      return initializeProjectDirectory(projectName);
    }
  }
}
