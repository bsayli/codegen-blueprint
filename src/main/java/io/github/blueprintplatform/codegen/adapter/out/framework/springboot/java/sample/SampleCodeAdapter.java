package io.github.blueprintplatform.codegen.adapter.out.framework.springboot.java.sample;

import io.github.blueprintplatform.codegen.adapter.error.exception.sample.SampleCodeTemplatesNotFoundException;
import io.github.blueprintplatform.codegen.adapter.error.exception.templating.SampleCodeTemplatesScanException;
import io.github.blueprintplatform.codegen.adapter.error.exception.templating.TemplateScanException;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.templating.ClasspathTemplateScanner;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.SampleCodePort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeLevel;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SampleCodeAdapter implements SampleCodePort {

  private static final String PATH_SEPARATOR = "/";
  private static final String SAMPLE_ROOT_DIR = "sample";

  private static final String MAIN_DIR = "main";
  private static final String TEST_DIR = "test";

  private static final String SRC_MAIN_JAVA = "src/main/java";
  private static final String SRC_TEST_JAVA = "src/test/java";

  private static final String JAVA_FTL_SUFFIX = ".java.ftl";
  private static final String FTL_SUFFIX = ".ftl";

  private static final String MODEL_KEY_PROJECT_PACKAGE_NAME = "projectPackageName";

  private final TemplateRenderer renderer;
  private final ArtifactSpec artifactSpec;
  private final ClasspathTemplateScanner templateScanner;

  public SampleCodeAdapter(
      TemplateRenderer renderer,
      ArtifactSpec artifactSpec,
      ClasspathTemplateScanner templateScanner) {
    this.renderer = renderer;
    this.artifactSpec = artifactSpec;
    this.templateScanner = templateScanner;
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.SAMPLE_CODE;
  }

  @Override
  public Iterable<? extends GeneratedResource> generate(ProjectBlueprint blueprint) {
    SampleCodeLevel level = blueprint.getArchitecture().sampleCodeOptions().level();
    ProjectLayout layout = blueprint.getArchitecture().layout();

    if (SampleCodeLevel.BASIC != level) {
      return List.of();
    }

    String templateRoot = resolveTemplateRoot(layout, level);

    List<String> templatePaths = scanTemplates(templateRoot);
    if (templatePaths.isEmpty()) {
      throw new SampleCodeTemplatesNotFoundException(templateRoot, layout.key(), level.key());
    }

    PackageName pkg = blueprint.getMetadata().packageName();
    String packagePath = pkg.value().replace('.', '/');

    Map<String, Object> model = Map.of(MODEL_KEY_PROJECT_PACKAGE_NAME, pkg.value());
    List<GeneratedResource> generated = new ArrayList<>();

    for (String fullTemplatePath : templatePaths) {
      if (!fullTemplatePath.endsWith(JAVA_FTL_SUFFIX)) {
        continue;
      }

      String relativeUnderRoot = fullTemplatePath.substring(templateRoot.length() + 1);

      String srcRoot = resolveSourceRoot(relativeUnderRoot);
      String relativeForJava = stripChannelPrefix(relativeUnderRoot);

      String javaRelative = stripSuffix(relativeForJava);

      Path outPath = Paths.get(srcRoot).resolve(packagePath).resolve(javaRelative);
      generated.add(renderer.renderUtf8(outPath, fullTemplatePath, model));
    }

    return List.copyOf(generated);
  }

  private String resolveSourceRoot(String relativeUnderRoot) {
    if (relativeUnderRoot.startsWith(TEST_DIR + PATH_SEPARATOR)) {
      return SRC_TEST_JAVA;
    }
    if (relativeUnderRoot.startsWith(MAIN_DIR + PATH_SEPARATOR)) {
      return SRC_MAIN_JAVA;
    }
    return SRC_MAIN_JAVA;
  }

  private String stripChannelPrefix(String relativeUnderRoot) {
    if (relativeUnderRoot.startsWith(TEST_DIR + PATH_SEPARATOR)) {
      return relativeUnderRoot.substring((TEST_DIR + PATH_SEPARATOR).length());
    }
    if (relativeUnderRoot.startsWith(MAIN_DIR + PATH_SEPARATOR)) {
      return relativeUnderRoot.substring((MAIN_DIR + PATH_SEPARATOR).length());
    }
    return relativeUnderRoot;
  }

  private String resolveTemplateRoot(ProjectLayout layout, SampleCodeLevel level) {
    String basePath = artifactSpec.basePath();
    String normalizedBase =
        basePath.endsWith(PATH_SEPARATOR) ? basePath.substring(0, basePath.length() - 1) : basePath;

    return normalizedBase
        + PATH_SEPARATOR
        + SAMPLE_ROOT_DIR
        + PATH_SEPARATOR
        + layout.key()
        + PATH_SEPARATOR
        + level.key();
  }

  private List<String> scanTemplates(String templateRoot) {
    try {
      return templateScanner.scan(templateRoot);
    } catch (TemplateScanException e) {
      throw new SampleCodeTemplatesScanException(templateRoot, e);
    }
  }

  private String stripSuffix(String value) {
    if (value.endsWith(FTL_SUFFIX)) {
      return value.substring(0, value.length() - FTL_SUFFIX.length());
    }
    return value;
  }
}
