package io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot;

import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.model.templating.TemplateType;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ApplicationYamlGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.TemplateEngine;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("springBootApplicationYamlGenerator")
public class SpringBootApplicationYamlGenerator implements ApplicationYamlGenerator {

  private static final String SRC_MAIN_RESOURCES = "src/main/resources";
  private final TemplateEngine freeMarkerTemplateEngine;

  public SpringBootApplicationYamlGenerator(TemplateEngine freeMarkerTemplateEngine) {
    this.freeMarkerTemplateEngine = freeMarkerTemplateEngine;
  }

  @Override
  public void generateApplicationYaml(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException {
    Map<String, Object> appPropertiesModel = new HashMap<>();
    appPropertiesModel.put("projectName", projectMetadata.getName());

    File srcMainResourcesFile = new File(projectDestination, SRC_MAIN_RESOURCES);

    freeMarkerTemplateEngine.generateFileFromTemplate(
        TemplateType.SPRING_BOOT_APPLICATION_YAML, appPropertiesModel, srcMainResourcesFile);
  }
}
