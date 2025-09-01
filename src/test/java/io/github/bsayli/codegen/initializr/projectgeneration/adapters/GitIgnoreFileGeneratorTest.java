package io.github.bsayli.codegen.initializr.projectgeneration.adapters;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import io.github.bsayli.codegen.initializr.projectgeneration.adapters.templating.FreeMarkerTemplateEngine;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GitIgnoreFileGeneratorTest {

  private static final String GITIGNORE_FILE_NAME = ".gitignore";

  @Autowired private GitIgnoreFileGenerator gitIgnoreFileGenerator;

  @TempDir private Path tempFolder;

  @Test
  void testGenerateGitIgnoreContent_CreatesCorrectFileWithEmptyIgnoreList() throws IOException {
    gitIgnoreFileGenerator.generateGitIgnoreContent(tempFolder.toFile(), Collections.emptyList());

    Path generatedFile = tempFolder.resolve(GITIGNORE_FILE_NAME);
    assertTrue(Files.exists(generatedFile));
    String content = Files.readString(generatedFile);
    assertTrue(content.length() > 10);
  }

  @Test
  void testGenerateGitIgnoreContent_CreatesCorrectFileWithSomeIgnoreList() throws IOException {
    List<String> ignoreList = Arrays.asList("*.pyc", "__pycache__");
    gitIgnoreFileGenerator.generateGitIgnoreContent(tempFolder.toFile(), ignoreList);

    Path generatedFile = tempFolder.resolve(GITIGNORE_FILE_NAME);
    assertTrue(Files.exists(generatedFile));
    String content = Files.readString(generatedFile);
    assertTrue(content.contains("*.py"));
    assertTrue(content.contains("__pycache__"));
  }

  @Test
  void testGenerateGitIgnoreContent_CreatesFileAndVerifiesContent() throws IOException {
    File projectDestination = tempFolder.toFile();

    List<String> additionalIgnorePatterns = List.of("*.md");
    gitIgnoreFileGenerator.generateGitIgnoreContent(projectDestination, additionalIgnorePatterns);

    File generatedFile = new File(projectDestination, GITIGNORE_FILE_NAME);
    assertTrue(generatedFile.exists(), "Generated file should be created!");

    List<String> gitIgnoreList =
        Files.readAllLines(generatedFile.toPath()).stream()
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .toList();

    assertThat(gitIgnoreList, hasItems("*.com", "*.md", ".idea/", "target/", "generated-sources/"));
  }

  @Test
  void testGenerateGitIgnoreContent_ThrowsExceptionOnTemplateEngineError() throws Exception {
    FreeMarkerTemplateEngine mockEngine = Mockito.mock(FreeMarkerTemplateEngine.class);
    GitIgnoreFileGenerator gitIgnoreFileGeneratorLocal = new GitIgnoreFileGenerator(mockEngine);

    Mockito.doThrow(new IOException("Template processing error"))
        .when(mockEngine)
        .generateFileFromTemplate(any(), any(), any());

    assertThrows(
        IOException.class,
        () -> gitIgnoreFileGeneratorLocal.generateGitIgnoreContent(tempFolder.toFile(), null));
  }
}
