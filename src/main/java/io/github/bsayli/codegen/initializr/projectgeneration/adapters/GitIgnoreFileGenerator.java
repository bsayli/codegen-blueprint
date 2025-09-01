package io.github.bsayli.codegen.initializr.projectgeneration.adapters;

import io.github.bsayli.codegen.initializr.projectgeneration.model.templating.TemplateType;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.GitIgnoreGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.TemplateEngine;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("gitIgnoreFileGenerator")
public class GitIgnoreFileGenerator implements GitIgnoreGenerator {

  private final TemplateEngine freeMarkerTemplateEngine;

  public GitIgnoreFileGenerator(TemplateEngine freeMarkerTemplateEngine) {
    this.freeMarkerTemplateEngine = freeMarkerTemplateEngine;
  }

  @Override
  public void generateGitIgnoreContent(File projectDestination, List<String> ignoreList)
      throws IOException {

    Map<String, Object> gitIgnoreData = new HashMap<>();
    gitIgnoreData.put("ignoreList", ignoreList != null ? ignoreList : Collections.emptyList());

    freeMarkerTemplateEngine.generateFileFromTemplate(
        TemplateType.GITIGNORE, gitIgnoreData, projectDestination);
  }
}
