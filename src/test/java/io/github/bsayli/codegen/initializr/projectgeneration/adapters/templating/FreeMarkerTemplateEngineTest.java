package io.github.bsayli.codegen.initializr.projectgeneration.adapters.templating;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bsayli.codegen.initializr.projectgeneration.model.templating.TemplateType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FreeMarkerTemplateEngineTest {

  private static final String GITIGNORE_FILE_NAME = ".gitignore";

  @Autowired FreeMarkerTemplateEngine freeMarkerTemplateEngine;

  @TempDir private Path tempFolder;

  @Test
  void testGenerateFileFromTemplateWithTemplateType() throws IOException {
    List<String> emptyList = Collections.emptyList();
    Map<String, Object> gitIgnoreData = new HashMap<>();
    gitIgnoreData.put("ignoreList", emptyList);

    freeMarkerTemplateEngine.generateFileFromTemplate(
        TemplateType.GITIGNORE, gitIgnoreData, tempFolder.toFile());
    Path generatedFile = tempFolder.resolve(GITIGNORE_FILE_NAME);
    assertTrue(Files.exists(generatedFile));
    String content = Files.readString(generatedFile);
    assertTrue(content.length() > 10);
  }

  @Test
  void testGenerateFileFromTemplate() throws IOException {
    List<String> emptyList = Collections.emptyList();
    Map<String, Object> gitIgnoreData = new HashMap<>();
    gitIgnoreData.put("ignoreList", emptyList);

    String templateFileName = TemplateType.GITIGNORE.getTemplateFileName();
    String fileName = TemplateType.GITIGNORE.getFileName();
    freeMarkerTemplateEngine.generateFileFromTemplate(
        templateFileName, fileName, gitIgnoreData, tempFolder.toFile());
    Path generatedFile = tempFolder.resolve(GITIGNORE_FILE_NAME);
    assertTrue(Files.exists(generatedFile));
    String content = Files.readString(generatedFile);
    assertTrue(content.length() > 10);
  }
}
