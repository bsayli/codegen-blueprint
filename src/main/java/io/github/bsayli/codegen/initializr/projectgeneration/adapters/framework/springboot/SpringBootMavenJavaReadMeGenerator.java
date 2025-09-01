package io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot;

import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.model.templating.TemplateType;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectDocumentationGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.TemplateEngine;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("springBootMavenJavaReadMeGenerator")
public class SpringBootMavenJavaReadMeGenerator implements ProjectDocumentationGenerator {

  private final TemplateEngine freeMarkerTemplateEngine;

  public SpringBootMavenJavaReadMeGenerator(TemplateEngine freeMarkerTemplateEngine) {
    this.freeMarkerTemplateEngine = freeMarkerTemplateEngine;
  }

  @Override
  public void generateProjectDocument(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException {
    Map<String, Object> readMeModel = new HashMap<>();
    readMeModel.put("projectName", projectMetadata.getName());
    freeMarkerTemplateEngine.generateFileFromTemplate(
        TemplateType.SPRING_BOOT_MAVEN_JAVA_README, readMeModel, projectDestination);
  }
}
