package io.github.bsayli.codegen.initializr.projectgeneration.adapters.maven;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MavenJavaProjectLayoutGeneratorTest {

  @TempDir public Path tempFolder;
  @Autowired private MavenJavaProjectLayoutGenerator generator;

  @Test
  void testGenerateProjectLayout_CreatesCorrectDirectoryAndPackageNames() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo")
            .packageName("com.example.demo")
            .build();

    File projectDestination = tempFolder.toFile();

    generator.generateProjectLayout(projectDestination, projectMetadata);

    List<String> expectedDirectories =
        List.of(
            "src/main/java/com/example/demo",
            "src/main/resources",
            "src/test/java/com/example/demo",
            "src/test/resources",
            "src/gen/java/com/example/demo/codegen");

    expectedDirectories.forEach(
        dir -> {
          File expectedDir = new File(projectDestination, dir);
          assertTrue(expectedDir.exists(), "Directory " + dir + " was not created!");
        });
  }

  @Test
  void testGenerateProjectLayout_CreatesProjectLayoutWithEmptyPackageName() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder().name("codegen-demo").packageName("").build();

    File projectDestination = tempFolder.toFile();

    generator.generateProjectLayout(projectDestination, projectMetadata);

    File expectedDir = new File(projectDestination, "src/main/java");
    assertTrue(expectedDir.exists(), "Main Java source directory was not created!");
  }

  @Test
  void testGenerateProjectLayout_CreatesProjectLayoutWithSpecialCharInPackageName()
      throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo")
            .packageName("com.example-demo")
            .build();

    File projectDestination = tempFolder.toFile();

    generator.generateProjectLayout(projectDestination, projectMetadata);

    String expectedDirName = "src/main/java/com/example_demo";
    File expectedDir = new File(projectDestination, expectedDirName);
    assertTrue(expectedDir.exists(), "Directory with special character not created!");
  }
}
