package io.github.bsayli.codegen.initializr.adapter.out.templating;

import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.nio.file.Path;
import java.util.Map;

public interface TemplateRenderer {
  GeneratedFile renderUtf8(Path outPath, String templateName, Map<String, Object> model);
}
