package io.github.blueprintplatform.codegen.testsupport.templating;

import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public final class NoopTemplateRenderer implements TemplateRenderer {

  @Override
  public GeneratedFile renderUtf8(Path outPath, String templateName, Map<String, Object> model) {
    return new GeneratedFile.Text(outPath, "", StandardCharsets.UTF_8);
  }
}
