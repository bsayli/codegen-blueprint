package io.github.blueprintplatform.codegen.adapter.out.shared.templating;

import io.github.blueprintplatform.codegen.adapter.error.exception.TemplateScanException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ClasspathTemplateScanner {

  private static final String TEMPLATES_ROOT_DIR = "templates";
  private static final String PATH_SEPARATOR = "/";
  private static final String FTL_SUFFIX = ".ftl";

  public List<String> scan(String templateRoot) {
    List<String> result = new ArrayList<>();

    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    String rootWithPrefix = TEMPLATES_ROOT_DIR + PATH_SEPARATOR + templateRoot;

    URL rootUrl = cl.getResource(rootWithPrefix);
    if (rootUrl == null) {
      return result;
    }

    try {
      Path rootPath = Paths.get(rootUrl.toURI());

      try (var paths = Files.walk(rootPath)) {
        paths
            .filter(Files::isRegularFile)
            .filter(p -> p.getFileName().toString().endsWith(FTL_SUFFIX))
            .forEach(
                file -> {
                  Path relative = rootPath.relativize(file);
                  String normalized =
                      relative.toString().replace(File.separatorChar, PATH_SEPARATOR.charAt(0));

                  result.add(templateRoot + PATH_SEPARATOR + normalized);
                });
      }

      return List.copyOf(result);

    } catch (URISyntaxException | IOException e) {
      throw new TemplateScanException(templateRoot, e);
    }
  }
}
