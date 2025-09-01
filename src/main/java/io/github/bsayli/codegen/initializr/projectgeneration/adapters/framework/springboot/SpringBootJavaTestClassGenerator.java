package io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot;

import static io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot.constants.SpringBootJavaTestClassGeneratorConstants.*;

import io.github.bsayli.codegen.initializr.projectgeneration.configuration.properties.MavenJavaSourceFolderProperties;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
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

  public SpringBootJavaTestClassGenerator(
      TemplateEngine freeMarkerTemplateEngine, MavenJavaSourceFolderProperties sourceFolder) {
    this.sourceFolder = sourceFolder;
    this.freeMarkerTemplateEngine = freeMarkerTemplateEngine;
  }

  @Override
  public void generateTestClass(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException {
    Map<String, Object> testClassModel = new HashMap<>();
    testClassModel.put("projectPackageName", projectMetadata.getPackageName());

    String className = projectMetadata.getName().replace("-", "");
    className =
        className.substring(0, 1).toUpperCase() + className.substring(1) + FILE_NAME_POSTFIX;
    testClassModel.put("className", className);

    String basePackagePath = projectMetadata.getPackageName().replace(".", "/");
    File srcTestJavaFile = new File(projectDestination, sourceFolder.srcTestJava());

    File testClassFileDestination = new File(srcTestJavaFile, basePackagePath);
    String fileName = className + FILE_NAME_EXTENSION;

    freeMarkerTemplateEngine.generateFileFromTemplate(
        TEMPLATE_NAME, fileName, testClassModel, testClassFileDestination);
  }
}
