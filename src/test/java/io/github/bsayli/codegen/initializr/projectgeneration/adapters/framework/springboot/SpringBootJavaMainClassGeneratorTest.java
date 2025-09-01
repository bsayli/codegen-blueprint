package io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import io.github.bsayli.codegen.initializr.projectgeneration.configuration.properties.MavenJavaSourceFolderProperties;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootJavaMainClassGeneratorTest {

  @Autowired private SpringBootJavaMainClassGenerator generator;

  @Mock private MavenJavaSourceFolderProperties mockSourceFolder;

  @TempDir private Path tempFolder;

  @Test
  void testGenerateMainClass_CreatesMainClassFile() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo")
            .packageName("com.codegen.core")
            .build();

    when(mockSourceFolder.srcMainJava()).thenReturn("src/main/java");

    generator.generateProjectStarterClass(tempFolder.toFile(), projectMetadata);

    File expectedMainClassFile =
        new File(tempFolder.toFile(), "src/main/java/com/codegen/core/CodegendemoApplication.java");

    assertTrue(expectedMainClassFile.exists(), "Main class file should be created");
    assertEquals(
        "CodegendemoApplication.java",
        expectedMainClassFile.getName(),
        "Main class file name should be CodegendemoApplication.java");
  }

  @Test
  void testGenerateMainClass_CreatesMainClassAndVerifiesContent() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo")
            .packageName("com.codegen.core")
            .build();

    when(mockSourceFolder.srcMainJava()).thenReturn("src/main/java");

    generator.generateProjectStarterClass(tempFolder.toFile(), projectMetadata);

    String generatedContent =
        Files.readString(
            new File(
                    tempFolder.toString(),
                    "src/main/java/com/codegen/core/CodegendemoApplication.java")
                .toPath());

    String expectedSpringBootAnnotation = "@SpringBootApplication";
    assertTrue(
        generatedContent.contains(expectedSpringBootAnnotation),
        "Generated content should contain @SpringBootApplication");

    String expectedMainClassName = "CodegendemoApplication";
    assertTrue(
        generatedContent.contains(expectedMainClassName),
        "Generated content should contain CodegendemoApplication");

    String expectedRunClassDefinition = "SpringApplication.run(CodegendemoApplication.class, args)";
    assertTrue(
        generatedContent.contains(expectedRunClassDefinition),
        "Generated content should contain this line " + expectedRunClassDefinition);
  }

  @Test
  void testGenerateMainClass_CreatesMainClassWithSpecilCharsAndVerifiesContent()
      throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo?*")
            .packageName("com.codegen.core")
            .build();

    when(mockSourceFolder.srcMainJava()).thenReturn("src/main/java");

    generator.generateProjectStarterClass(tempFolder.toFile(), projectMetadata);

    String generatedContent =
        Files.readString(
            new File(
                    tempFolder.toString(),
                    "src/main/java/com/codegen/core/CodegendemoApplication.java")
                .toPath());

    String expectedSpringBootAnnotation = "@SpringBootApplication";
    assertTrue(
        generatedContent.contains(expectedSpringBootAnnotation),
        "Generated content should contain @SpringBootApplication");

    String expectedMainClassName = "CodegendemoApplication";
    assertTrue(
        generatedContent.contains(expectedMainClassName),
        "Generated content should contain CodegendemoApplication");

    String expectedRunClassDefinition = "SpringApplication.run(CodegendemoApplication.class, args)";
    assertTrue(
        generatedContent.contains(expectedRunClassDefinition),
        "Generated content should contain this line " + expectedRunClassDefinition);
  }
}
