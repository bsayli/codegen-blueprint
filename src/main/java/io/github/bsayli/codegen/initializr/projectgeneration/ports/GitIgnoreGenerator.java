package io.github.bsayli.codegen.initializr.projectgeneration.ports;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface GitIgnoreGenerator {

  void generateGitIgnoreContent(File projectDestination, List<String> ignoreList)
      throws IOException;
}
