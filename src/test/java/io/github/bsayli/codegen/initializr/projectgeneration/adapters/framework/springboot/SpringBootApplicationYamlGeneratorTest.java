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
class SpringBootApplicationYamlGeneratorTest {

  private static final String APPLICATION_YAML_FILE_NAME = "application.yml";
  private static final String EXPECTED_APPLICATION_NAME = "codegen-demo";
  private static final String SRC_MAIN_RESOURCES = "src/main/resources";

  @Autowired private SpringBootApplicationYamlGenerator applicationYamlGenerator;

  @TempDir private Path tempFolder;

  @Test
  void testGenerateApplicationYaml_CreatesCorrectFileStructureAndFileName() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder().name("codegen-demo").build();

    File projectDestination = tempFolder.toFile();

    applicationYamlGenerator.generateApplicationYaml(projectDestination, projectMetadata);

    File srcMainResourcesFileDestination = new File(projectDestination, SRC_MAIN_RESOURCES);
    File generatedFile = new File(srcMainResourcesFileDestination, APPLICATION_YAML_FILE_NAME);
    assertTrue(generatedFile.exists());

    String content = Files.readString(generatedFile.toPath());
    assertTrue(content.length() > 10);
  }

  @Test
  void testGenerateApplicationYaml_CreatesFileAndVerifiesContent() throws IOException {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder().name("codegen-demo").build();

    File projectDestination = tempFolder.toFile();

    applicationYamlGenerator.generateApplicationYaml(projectDestination, projectMetadata);

    File srcMainResourcesFileDestination = new File(projectDestination, SRC_MAIN_RESOURCES);
    File generatedFile = new File(srcMainResourcesFileDestination, APPLICATION_YAML_FILE_NAME);
    assertTrue(generatedFile.exists());

    String yml = Files.readString(generatedFile.toPath());

    assertTrue(yml.contains("spring:"), "YAML should contain 'spring:'");
    assertTrue(yml.contains("application:"), "YAML should contain 'application:'");
    assertTrue(
        yml.contains("name: " + EXPECTED_APPLICATION_NAME), "YAML should set application name");
  }

  @Test
  void testGenerateApplicationYaml_ThrowsExceptionOnTemplateEngineError() throws Exception {
    ProjectMetadata projectMetadata =
        new ProjectMetadata.ProjectMetadataBuilder().name("codegen-demo").build();

    File projectDestination = tempFolder.toFile();

    FreeMarkerTemplateEngine mockEngine = Mockito.mock(FreeMarkerTemplateEngine.class);
    SpringBootApplicationYamlGenerator generatorLocal =
        new SpringBootApplicationYamlGenerator(mockEngine);

    Mockito.doThrow(new IOException("Template processing error"))
        .when(mockEngine)
        .generateFileFromTemplate(any(), any(), any());

    assertThrows(
        IOException.class,
        () -> generatorLocal.generateApplicationYaml(projectDestination, projectMetadata));
  }
}
