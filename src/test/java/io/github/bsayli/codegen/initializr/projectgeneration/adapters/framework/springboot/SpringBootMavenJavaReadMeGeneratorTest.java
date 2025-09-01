package io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import io.github.bsayli.codegen.initializr.projectgeneration.adapters.templating.FreeMarkerTemplateEngine;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootMavenJavaReadMeGeneratorTest {

  private static final String README_FILE_NAME = "README.md";

  @Autowired private SpringBootMavenJavaReadMeGenerator readMeGenerator;

  @TempDir private Path tempFolder;

  @Test
  void testGenerateReadMe_CreatesCorrectFileStructureAndFileName() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder().name("codegen-demo").build();

    File projectDestination = tempFolder.toFile();

    readMeGenerator.generateProjectDocument(projectDestination, projectMetadata);

    File generatedFile = new File(projectDestination, README_FILE_NAME);
    assertTrue(generatedFile.exists());

    String content = Files.readString(generatedFile.toPath());
    assertTrue(content.length() > 10);
  }

  @Test
  void testGenerateReadMe_CreatesFileAndVerifiesContent() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder().name("codegen-demo").build();

    File projectDestination = tempFolder.toFile();

    readMeGenerator.generateProjectDocument(projectDestination, projectMetadata);

    File generatedFile = new File(projectDestination, README_FILE_NAME);
    assertTrue(generatedFile.exists());

    String generatedContent =
        Files.readString(new File(tempFolder.toString(), README_FILE_NAME).toPath());

    String expectedLineProjectInitialization = "Project Initialization";
    assertTrue(
        generatedContent.contains(expectedLineProjectInitialization),
        "Generated content should contain" + expectedLineProjectInitialization);

    String expectedLineProjectName = "codegen-demo";
    assertTrue(
        generatedContent.contains(expectedLineProjectName),
        "Generated content should contain" + expectedLineProjectName);

    String expectedLineDependencies = "Dependencies";
    assertTrue(
        generatedContent.contains(expectedLineDependencies),
        "Generated content should contain" + expectedLineDependencies);
  }

  @Test
  void testGenerateReadMe_ThrowsExceptionOnTemplateEngineError() throws Exception {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder().name("codegen-demo").build();

    File projectDestination = tempFolder.toFile();

    FreeMarkerTemplateEngine mockEngine = Mockito.mock(FreeMarkerTemplateEngine.class);
    SpringBootMavenJavaReadMeGenerator generatorLocal =
        new SpringBootMavenJavaReadMeGenerator(mockEngine);

    Mockito.doThrow(new IOException("Template processing error"))
        .when(mockEngine)
        .generateFileFromTemplate(any(), any(), any());

    assertThrows(
        IOException.class,
        () -> generatorLocal.generateProjectDocument(projectDestination, projectMetadata));
  }
}
