package io.github.bsayli.codegen.initializr.projectgeneration.adapters.maven;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MavenBuildWrapperGeneratorTest {

  private static final String WRAPPER_FILE_DIR = ".mvn/wrapper";
  private static final String WRAPPER_FILE_NAME = "maven-wrapper.properties";
  private static final String WRAPPER_VERSION = "3.3.3";
  @TempDir public Path tempFolder;
  @Autowired private MavenBuildWrapperGenerator generator;

  @Test
  void testGenerateBuildWrapper_CreatesWrapperFile() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo")
            .packageName("com.codegen.core")
            .build();

    File projectDestination = tempFolder.toFile();

    generator.generateBuildWrapper(projectDestination, projectMetadata);

    File wrapperFileDir = new File(projectDestination, WRAPPER_FILE_DIR);
    assertTrue(wrapperFileDir.exists(), "Wrapper file directory should be created!");

    File wrapperFile = new File(wrapperFileDir, WRAPPER_FILE_NAME);
    assertTrue(wrapperFile.exists(), "Wrapper file should be created!");
  }

  @Test
  void testGenerateBuildWrapper_CreatesFileAndVerifiesContent() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo")
            .packageName("com.codegen.core")
            .build();

    File projectDestination = tempFolder.toFile();

    generator.generateBuildWrapper(projectDestination, projectMetadata);

    File wrapperFileDir = new File(projectDestination, WRAPPER_FILE_DIR);
    File wrapperFile = new File(wrapperFileDir, WRAPPER_FILE_NAME);
    assertTrue(wrapperFile.exists(), "Wrapper file should be created!");

    List<String> wrapperFileList = Files.readAllLines(wrapperFile.toPath());
    assertThat(wrapperFileList, hasItem("wrapperVersion=" + WRAPPER_VERSION));
  }
}
