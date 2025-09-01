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
class SpringBootJavaTestClassGeneratorTest {

  @Autowired private SpringBootJavaTestClassGenerator generator;

  @Mock private MavenJavaSourceFolderProperties mockSourceFolder;

  @TempDir private Path tempFolder;

  @Test
  void testGenerateTestClass_CreatesTestClassFile() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo")
            .packageName("com.codegen.core")
            .build();

    when(mockSourceFolder.srcTestJava()).thenReturn("src/test/java");

    generator.generateTestClass(tempFolder.toFile(), projectMetadata);

    File expectedTestClassFile =
        new File(
            tempFolder.toFile(), "src/test/java/com/codegen/core/CodegendemoApplicationTests.java");

    assertTrue(expectedTestClassFile.exists(), "Test class file should be created");
    assertEquals(
        "CodegendemoApplicationTests.java",
        expectedTestClassFile.getName(),
        "Test class file name should be CodegendemoApplicationTests.java");
  }

  @Test
  void testGenerateTestClass_CreatesTestClassAndVerifiesContent() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo")
            .packageName("com.codegen.core")
            .build();

    when(mockSourceFolder.srcTestJava()).thenReturn("src/test/java");

    generator.generateTestClass(tempFolder.toFile(), projectMetadata);

    String generatedContent =
        Files.readString(
            new File(
                    tempFolder.toString(),
                    "src/test/java/com/codegen/core/CodegenDemoApplicationTests.java")
                .toPath());

    String expectedSpringBootTestAnnotation = "@SpringBootTest";
    assertTrue(
        generatedContent.contains(expectedSpringBootTestAnnotation),
        "Generated content should contain @SpringBootTest");

    String expectedTestClassName = "CodegendemoApplicationTests";
    assertTrue(
        generatedContent.contains(expectedTestClassName),
        "Generated content should contain CodegendemoApplicationTests");
  }
}
