package io.github.bsayli.codegen.initializr.projectgeneration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.github.bsayli.codegen.initializr.projectgeneration.model.Dependency;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectType;
import io.github.bsayli.codegen.initializr.projectgeneration.model.spring.SpringBootJavaProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.model.spring.SpringBootJavaProjectMetadata.SpringBootJavaProjectMetadataBuilder;
import io.github.bsayli.codegen.initializr.projectgeneration.model.techstack.BuildTool;
import io.github.bsayli.codegen.initializr.projectgeneration.model.techstack.Framework;
import io.github.bsayli.codegen.initializr.projectgeneration.model.techstack.Language;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

@SpringBootTest
class ProjectGenerationServiceImplTest {

  private static final String DIRECTORY_NAME_UNARCHIVED = "unarchived";

  @Autowired private ProjectGenerationService projectGenerationService;

  private Path archivedProjectPath;

  @Test
  void testGenerateProject_SupportedProjectType_GeneratesProjectAndVerifiesContent()
      throws IOException {
    ProjectType springBootMavenJavaProjectType =
        new ProjectType(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);

    Dependency dependencySpringBootStarterWeb =
        new Dependency.DependencyBuilder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-web")
            .build();

    Dependency dependencySpringBootStarterTest =
        new Dependency.DependencyBuilder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-test")
            .scope("test")
            .build();

    SpringBootJavaProjectMetadataBuilder projectMetadataBuilder =
        new SpringBootJavaProjectMetadata.SpringBootJavaProjectMetadataBuilder();
    projectMetadataBuilder
        .groupId("com.codegen")
        .artifactId("codegen-demo")
        .name("codegen-demo")
        .description("Codegen Demo Project")
        .packageName("com.codegen.demo")
        .dependencies(List.of(dependencySpringBootStarterWeb, dependencySpringBootStarterTest));

    SpringBootJavaProjectMetadata springBootJavaProjectMetadata =
        projectMetadataBuilder.springBootVersion("3.5.5").javaVersion("21").build();

    archivedProjectPath =
        projectGenerationService.generateProject(
            springBootMavenJavaProjectType, springBootJavaProjectMetadata);
    File archivedProjectFile = archivedProjectPath.toFile();

    assertTrue(archivedProjectFile.exists(), "Archive project file should be created");

    File extractedDir = createExtractedProject(archivedProjectFile);
    assertTrue(
        extractedDir.exists() && !FileUtils.isEmptyDirectory(extractedDir),
        "Archived file was corrupted!");

    String archived = archivedProjectFile.getName().replace(".zip", "");
    File extractedProjectDir = new File(extractedDir, archived);

    String gitIgnoreFileName = ".gitignore";
    File gitIgnoreFileFromUnarchived = new File(extractedProjectDir, gitIgnoreFileName);
    assertTrue(
        gitIgnoreFileFromUnarchived.exists(),
        "Archived project file does not contain " + gitIgnoreFileName + " file");

    String pomFileName = "pom.xml";
    File pomFileFromUnarchived = new File(extractedProjectDir, pomFileName);
    assertTrue(
        pomFileFromUnarchived.exists(),
        "Archived project file does not contain " + pomFileName + " file");
  }

  @Test
  void testGenerateProject_UnsupportedProjectType_ThrowsException() throws IOException {
    ProjectType quarkusMavenJavaProjectType =
        new ProjectType(Framework.QUARKUS, BuildTool.MAVEN, Language.JAVA);

    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .groupId("com.codegen")
            .artifactId("codegen-demo")
            .name("codegen-demo")
            .description("Codegen Demo Project")
            .packageName("com.codegen.demo")
            .dependencies(Collections.emptyList())
            .build();

    try {
      archivedProjectPath =
          projectGenerationService.generateProject(quarkusMavenJavaProjectType, projectMetadata);
      fail("Expected exception for unsupported project type");
    } catch (IllegalArgumentException e) {
      assertEquals("Unsupported project type: " + quarkusMavenJavaProjectType, e.getMessage());
    }
  }

  @AfterEach
  void cleanup() throws IOException {
    if (archivedProjectPath != null && archivedProjectPath.toFile().exists()) {
      Path parentPath = archivedProjectPath.getParent();
      if (parentPath != null && Files.exists(parentPath)) {
        Collection<File> files = FileUtils.listFiles(parentPath.toFile(), null, false);
        List<String> subfolderNames = getSubfolderNames(parentPath);
        if (!CollectionUtils.isEmpty(files) && !CollectionUtils.isEmpty(subfolderNames)) {
          boolean countSizeOk = files.size() == 1 && subfolderNames.size() == 2;
          if (countSizeOk && subfolderNames.contains(DIRECTORY_NAME_UNARCHIVED)) {
            FileUtils.deleteDirectory(parentPath.toFile());
          }
        }
      }
    }
  }

  private File createExtractedProject(File archivedProjectFile) throws IOException {
    File extractedDir = new File(archivedProjectFile.getParentFile(), DIRECTORY_NAME_UNARCHIVED);
    ensureDir(extractedDir, "Failed to create extracted dir: " + extractedDir);

    try (ArchiveInputStream<?> inputStream =
        new ZipArchiveInputStream(new FileInputStream(archivedProjectFile))) {
      ArchiveEntry entry;
      while ((entry = inputStream.getNextEntry()) != null) {
        if (entry.isDirectory()) {
          File directory = new File(extractedDir, entry.getName());
          ensureDir(directory, "Failed to create directory: " + directory);
        } else {
          File file = new File(extractedDir, entry.getName());
          File parent = file.getParentFile();
          ensureDir(parent, "Failed to create parent directory: " + parent);
          try (OutputStream outputStream = new FileOutputStream(file)) {
            IOUtils.copy(inputStream, outputStream);
          }
        }
      }
    }

    return extractedDir;
  }

  private void ensureDir(File dir, String errorMessage) throws IOException {
    if (dir == null) {
      throw new IOException(errorMessage + " (null)");
    }
    if (dir.exists()) {
      if (!dir.isDirectory()) {
        throw new IOException(errorMessage + " (exists but not a directory)");
      }
      return;
    }
    boolean created = dir.mkdirs();
    if (!created && !dir.exists()) {
      throw new IOException(errorMessage);
    }
  }

  private List<String> getSubfolderNames(Path folderPath) {
    List<String> subfolderNames = new ArrayList<>();
    File folder = new File(folderPath.toString());
    if (!folder.isDirectory()) {
      return subfolderNames;
    }
    File[] files = folder.listFiles();
    if (files == null) {
      return subfolderNames;
    }
    for (File file : files) {
      if (file.isDirectory()) {
        subfolderNames.add(file.getName());
      }
    }
    return subfolderNames;
  }
}
