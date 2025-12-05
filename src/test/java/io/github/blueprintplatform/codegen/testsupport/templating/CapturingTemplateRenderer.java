package io.github.blueprintplatform.codegen.testsupport.templating;

import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedFile;
import java.nio.file.Path;
import java.util.Map;

public final class CapturingTemplateRenderer implements TemplateRenderer {

  public Path capturedOutPath;
  public String capturedTemplateName;
  public Map<String, Object> capturedModel;
  public GeneratedFile nextFile;

  @Override
  public GeneratedFile renderUtf8(Path outPath, String templateName, Map<String, Object> model) {
    this.capturedOutPath = outPath;
    this.capturedTemplateName = templateName;
    this.capturedModel = model;
    return nextFile;
  }
}
