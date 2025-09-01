package io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot;

import static io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot.constants.SpringBootJavaTestClassGeneratorConstants.*;

import io.github.bsayli.codegen.initializr.projectgeneration.configuration.properties.MavenJavaSourceFolderProperties;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.naming.NameConverter;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.FrameworkSpecificTestUnitGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.TemplateEngine;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("springBootJavaTestClassGenerator")
public class SpringBootJavaTestClassGenerator implements FrameworkSpecificTestUnitGenerator {

  private final TemplateEngine freeMarkerTemplateEngine;
  private final MavenJavaSourceFolderProperties sourceFolder;
  private final NameConverter nameConverter;

  public SpringBootJavaTestClassGenerator(
      TemplateEngine freeMarkerTemplateEngine,
      MavenJavaSourceFolderProperties sourceFolder,
      NameConverter nameConverter) {
    this.sourceFolder = sourceFolder;
    this.freeMarkerTemplateEngine = freeMarkerTemplateEngine;
    this.nameConverter = nameConverter;
  }

  @Override
  public void generateTestClass(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException {

    Map<String, Object> testClassModel = new HashMap<>();
    testClassModel.put("projectPackageName", projectMetadata.getPackageName());

    String classBase = nameConverter.toPascalCase(projectMetadata.getName());
    String className = classBase + FILE_NAME_POSTFIX;
    testClassModel.put("className", className);

    String basePackagePath = projectMetadata.getPackageName().replace(".", "/");
    File srcTestJavaFile = new File(projectDestination, sourceFolder.srcTestJava());

    File testClassFileDestination = new File(srcTestJavaFile, basePackagePath);
    String fileName = className + FILE_NAME_EXTENSION;

    freeMarkerTemplateEngine.generateFileFromTemplate(
        TEMPLATE_NAME, fileName, testClassModel, testClassFileDestination);
  }
}
