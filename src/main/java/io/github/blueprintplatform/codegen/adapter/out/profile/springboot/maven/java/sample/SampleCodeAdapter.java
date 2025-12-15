package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.sample;

import io.github.blueprintplatform.codegen.adapter.error.exception.SampleCodeLevelNotSupportedException;
import io.github.blueprintplatform.codegen.adapter.error.exception.SampleCodeTemplatesNotFoundException;
import io.github.blueprintplatform.codegen.adapter.error.exception.SampleCodeTemplatesScanException;
import io.github.blueprintplatform.codegen.adapter.out.shared.SampleCodeLayoutSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.SampleCodePort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeLevel;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SampleCodeAdapter implements SampleCodePort {

  private static final String TEMPLATES_ROOT_DIR = "templates";
  private static final String SRC_MAIN_JAVA = "src/main/java";
  private static final String PATH_SEPARATOR = "/";
  private static final String JAVA_FTL_SUFFIX = ".java.ftl";
  private static final String FTL_SUFFIX = ".ftl";
  private static final String MODEL_KEY_PROJECT_PACKAGE_NAME = "projectPackageName";

  private final TemplateRenderer renderer;
  private final ArtifactSpec artifactSpec;
  private final SampleCodeLayoutSpec sampleCodeLayoutSpec;

  public SampleCodeAdapter(
      TemplateRenderer renderer,
      ArtifactSpec artifactSpec,
      SampleCodeLayoutSpec sampleCodeLayoutSpec) {
    this.renderer = renderer;
    this.artifactSpec = artifactSpec;
    this.sampleCodeLayoutSpec = sampleCodeLayoutSpec;
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.SAMPLE_CODE;
  }

  @Override
  public Iterable<? extends GeneratedResource> generate(ProjectBlueprint blueprint) {
    SampleCodeOptions options = blueprint.getArchitecture().sampleCodeOptions();
    SampleCodeLevel level = options == null ? SampleCodeLevel.NONE : options.level();

    boolean levelInvalid = (level == null) || !level.isEnabled() || SampleCodeLevel.RICH == level;
    if (levelInvalid || !blueprint.getArchitecture().layout().isHexagonal()) {
      return List.of();
    }

    ProjectLayout layout = blueprint.getArchitecture().layout();
    String templateBasePath = artifactSpec.basePath();
    String samplesRootRelative = resolveSamplesRoot(layout, level);
    String templateRoot = normalizeTemplateRoot(templateBasePath, samplesRootRelative);

    List<String> templatePaths = resolveTemplatePaths(templateRoot);
    if (templatePaths.isEmpty()) {
      throw new SampleCodeTemplatesNotFoundException(templateRoot, layout.key(), level.key());
    }

    PackageName pkg = blueprint.getMetadata().packageName();
    String packagePath = pkg.value().replace('.', '/');

    Map<String, Object> model = buildModel(blueprint);
    List<GeneratedResource> generated = new ArrayList<>();

    for (String fullTemplatePath : templatePaths) {
      if (!fullTemplatePath.endsWith(JAVA_FTL_SUFFIX)) {
        continue;
      }

      String relativeUnderRoot = fullTemplatePath.substring(templateRoot.length() + 1);
      String javaRelative = stripSuffix(relativeUnderRoot);

      Path outPath = Paths.get(SRC_MAIN_JAVA).resolve(packagePath).resolve(javaRelative);

      GeneratedResource resource = renderer.renderUtf8(outPath, fullTemplatePath, model);

      generated.add(resource);
    }

    return List.copyOf(generated);
  }

  private String resolveSamplesRoot(ProjectLayout layout, SampleCodeLevel level) {
    String layoutRoot =
        layout != null && layout.isHexagonal()
            ? sampleCodeLayoutSpec.roots().hexagonal()
            : sampleCodeLayoutSpec.roots().standard();

    String levelDir =
        switch (level) {
          case BASIC -> sampleCodeLayoutSpec.levels().basicDirName();
          case RICH -> sampleCodeLayoutSpec.levels().richDirName();
          case NONE -> throw new SampleCodeLevelNotSupportedException(level.key());
        };

    String normalizedLayoutRoot =
        layoutRoot.endsWith(PATH_SEPARATOR)
            ? layoutRoot.substring(0, layoutRoot.length() - 1)
            : layoutRoot;

    return normalizedLayoutRoot + PATH_SEPARATOR + levelDir;
  }

  private String normalizeTemplateRoot(String basePath, String samplesRootRelative) {
    String normalizedBase =
        basePath.endsWith(PATH_SEPARATOR) ? basePath.substring(0, basePath.length() - 1) : basePath;
    return normalizedBase + PATH_SEPARATOR + samplesRootRelative;
  }

  private Map<String, Object> buildModel(ProjectBlueprint blueprint) {
    PackageName pkg = blueprint.getMetadata().packageName();
    return Map.of(MODEL_KEY_PROJECT_PACKAGE_NAME, pkg.value());
  }

  private List<String> resolveTemplatePaths(String templateRoot) {
    List<String> result = new ArrayList<>();

    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    String rootWithPrefix = TEMPLATES_ROOT_DIR + PATH_SEPARATOR + templateRoot;

    URL rootUrl = cl.getResource(rootWithPrefix);
    if (rootUrl == null) {
      return result;
    }

    try {
      Path rootPath = Paths.get(rootUrl.toURI());

      try (var paths = java.nio.file.Files.walk(rootPath)) {
        paths
            .filter(java.nio.file.Files::isRegularFile)
            .filter(p -> p.getFileName().toString().endsWith(FTL_SUFFIX))
            .forEach(
                file -> {
                  Path relativeToRoot = rootPath.relativize(file);
                  String normalizedRelativeToRoot =
                      relativeToRoot
                          .toString()
                          .replace(File.separatorChar, PATH_SEPARATOR.charAt(0));

                  String fullTemplatePath =
                      templateRoot + PATH_SEPARATOR + normalizedRelativeToRoot;

                  result.add(fullTemplatePath);
                });
      }

      return result;
    } catch (URISyntaxException | java.io.IOException e) {
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
