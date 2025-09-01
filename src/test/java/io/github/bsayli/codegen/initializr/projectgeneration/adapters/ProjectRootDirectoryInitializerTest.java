package io.github.bsayli.codegen.initializr.projectgeneration.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectRootDirectoryInitializerTest {

  @TempDir public Path tempFolder;
  @Autowired private ProjectRootDirectoryInitializer initializer;
  private Path projectDirectory;

  @Test
  void testInitializeProjectDirectory_CreatesCorrectDirectoryWithProjectName() throws IOException {
    String projectName = "springBootTest";

    projectDirectory = initializer.initializeProjectDirectory(projectName);

    assertTrue(Files.isDirectory(projectDirectory), "Project directory should be created");

    assertTrue(
        projectDirectory.getFileName().toString().startsWith(projectName),
        "Directory name should start with " + projectName);
  }

  @Test
  void testInitializeProjectDirectory_CreatesCorrectDirectoryWithProjectNameAndLocation()
      throws IOException {
    String projectName = "springBootTest";
    Path projectLocation = Path.of(tempFolder.toFile().toPath().toString() + "/resources/app");

    Path projectFullPath = projectLocation.resolve(projectName);

    Path createdDir = initializer.initializeProjectDirectory(projectName, projectLocation);

    assertTrue(Files.isDirectory(createdDir), "Project directory should be created");

    assertEquals(projectFullPath, createdDir, "Directory should be created at specified location");

    String directoryName = createdDir.getFileName().toString();
    assertEquals(projectName, directoryName, "Directory name should include project name");
  }

  @Test
  void testInitializeProjectDirectory_CreatesCorrectDirectoryWithNullProjectLocation()
      throws IOException {
    String projectName = "null-location-app";

    projectDirectory = initializer.initializeProjectDirectory(projectName, null);

    assertTrue(Files.isDirectory(projectDirectory), "Project directory should be created");
    assertTrue(
        projectDirectory.getFileName().toString().startsWith(projectName),
        "Directory name should start with project name");
  }

  @Test
  void testInitializeProjectDirectory_ShouldFailWhenDirectoryAlreadyExists() throws IOException {
    String projectName = "codegen-demo";
    Path projectLocation = tempFolder.toFile().toPath();

    Path projectDir = projectLocation.resolve(projectName);
    Files.createDirectories(projectDir); // Create directories recursively

    try {
      initializer.initializeProjectDirectory(projectName, projectLocation);
      fail("Expected an exception for existing directory");
    } catch (FileAlreadyExistsException e) {
      assertTrue(e.getMessage().contains("File already exists"));
    }
  }

  @AfterEach
  void cleanup() throws IOException {
    if (projectDirectory != null && Files.exists(projectDirectory)) {
      FileUtils.deleteDirectory(projectDirectory.toFile());
      File parentFile = projectDirectory.getParent().toFile();
      if (parentFile.exists() && FileUtils.isEmptyDirectory(parentFile)) {
        FileUtils.deleteDirectory(parentFile);
      }
    }
  }
}
