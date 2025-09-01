package io.github.bsayli.codegen.initializr.projectgeneration.ports;

import io.github.bsayli.codegen.initializr.projectgeneration.model.templating.TemplateType;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface TemplateEngine {

  void generateFileFromTemplate(
      TemplateType templateType, Map<String, Object> data, File destination) throws IOException;

  void generateFileFromTemplate(
      String templateFileName, String fileName, Map<String, Object> data, File destination)
      throws IOException;
}
