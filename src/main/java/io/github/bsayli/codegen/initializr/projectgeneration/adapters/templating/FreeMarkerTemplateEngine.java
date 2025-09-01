package io.github.bsayli.codegen.initializr.projectgeneration.adapters.templating;

import io.github.bsayli.codegen.initializr.projectgeneration.model.templating.TemplateType;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.TemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("freeMarkerTemplateEngine")
public class FreeMarkerTemplateEngine implements TemplateEngine {

  private final Configuration freemarkerTemplateConfiguration;

  public FreeMarkerTemplateEngine(Configuration freemarkerTemplateConfiguration) {
    this.freemarkerTemplateConfiguration = freemarkerTemplateConfiguration;
  }

  @Override
  public void generateFileFromTemplate(
      TemplateType templateType, Map<String, Object> data, File destination) throws IOException {

    String templateFileName = templateType.getTemplateFileName();
    String fileName = templateType.getFileName();

    generateFileFromTemplate(templateFileName, fileName, data, destination);
  }

  public void generateFileFromTemplate(
      String templateFileName, String fileName, Map<String, Object> data, File destination)
      throws IOException {
    Template template = freemarkerTemplateConfiguration.getTemplate(templateFileName);

    if (!destination.exists()) {
      destination.mkdirs();
    }

    try (Writer writer = new FileWriter(new File(destination, fileName))) {
      template.process(data, writer);
    } catch (TemplateException e) {
      throw new IOException("Error processing template: " + e.getMessage(), e);
    }
  }
}
