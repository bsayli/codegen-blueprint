package io.github.bsayli.codegen.initializr.adapter.out.templating;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public class FreeMarkerTemplateRenderer implements TemplateRenderer {

  private final Configuration cfg;

  public FreeMarkerTemplateRenderer(Configuration cfg) {
    this.cfg = cfg;
  }

  @Override
  public GeneratedFile renderUtf8(Path outPath, String templateName, Map<String, Object> model) {
    try (StringWriter sw = new StringWriter()) {
      Template tpl = cfg.getTemplate(templateName);
      tpl.process(model, sw);
      return new GeneratedFile.Text(outPath, sw.toString(), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new TemplateRenderingException(templateName, e);
    }
  }
}
