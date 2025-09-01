package io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootJavaTestClassGeneratorTest {

  @Autowired private SpringBootJavaTestClassGenerator generator;

  @TempDir private Path tempFolder;

  @Test
  void testGenerateTestClass_CreatesTestClassFile() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo")
            .packageName("com.codegen.core")
            .build();

    generator.generateTestClass(tempFolder.toFile(), projectMetadata);

    File expectedTestClassFile =
        new File(
            tempFolder.toFile(), "src/test/java/com/codegen/core/CodegenDemoApplicationTests.java");

    assertTrue(expectedTestClassFile.exists(), "Test class file should be created");
    assertEquals(
        "CodegenDemoApplicationTests.java",
        expectedTestClassFile.getName(),
        "Test class file name should be CodegenDemoApplicationTests.java");
  }

  @Test
  void testGenerateTestClass_CreatesTestClassAndVerifiesContent() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder()
            .name("codegen-demo")
            .packageName("com.codegen.core")
            .build();

    generator.generateTestClass(tempFolder.toFile(), projectMetadata);

    String generatedContent =
        Files.readString(
            new File(
                    tempFolder.toString(),
                    "src/test/java/com/codegen/core/CodegenDemoApplicationTests.java")
                .toPath());

    assertTrue(
        generatedContent.contains("@SpringBootTest"),
        "Generated content should contain @SpringBootTest");

    String expectedTestClassName = "CodegenDemoApplicationTests";
    assertTrue(
        generatedContent.contains(expectedTestClassName),
        "Generated content should contain CodegenDemoApplicationTests");
  }
}
