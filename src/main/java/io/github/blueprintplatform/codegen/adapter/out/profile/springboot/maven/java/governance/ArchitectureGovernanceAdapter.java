package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.governance;

import io.github.blueprintplatform.codegen.adapter.error.exception.templating.ArchitectureGovernanceTemplatesScanException;
import io.github.blueprintplatform.codegen.adapter.error.exception.templating.TemplateScanException;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.dependency.DependencyFeature;
import io.github.blueprintplatform.codegen.adapter.out.shared.templating.ClasspathTemplateScanner;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArchitectureGovernancePort;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.EnforcementMode;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArchitectureGovernanceAdapter implements ArchitectureGovernancePort {

  private static final String PATH_SEPARATOR = "/";
  private static final String GOV_ROOT_DIR = "governance";

  private static final String SRC_TEST_JAVA = "src/test/java";
  private static final String JAVA_FTL_SUFFIX = ".java.ftl";
  private static final String FTL_SUFFIX = ".ftl";

  private static final String MODEL_KEY_PROJECT_PACKAGE_NAME = "projectPackageName";
  private static final String OUT_SEGMENT_ARCHITECTURE = "architecture";
  private static final String OUT_SEGMENT_ARCH_UNIT = "archunit";

  private static final String SPRING_BOOT_GROUP_ID = "org.springframework.boot";
  private static final DependencyFeature WEB_STARTER =
      new DependencyFeature("web", SPRING_BOOT_GROUP_ID, "spring-boot-starter-web");

  private static final Set<String> WEB_REQUIRED_GOVERNANCE_TEMPLATES =
      Set.of(
          "HexagonalStrictRestBoundarySignatureIsolationTest.java.ftl",
          "StandardStrictRestBoundarySignatureIsolationTest.java.ftl");

  private final TemplateRenderer renderer;
  private final ArtifactSpec artifactSpec;
  private final ClasspathTemplateScanner templateScanner;

  public ArchitectureGovernanceAdapter(
      TemplateRenderer renderer,
      ArtifactSpec artifactSpec,
      ClasspathTemplateScanner templateScanner) {
    this.renderer = renderer;
    this.artifactSpec = artifactSpec;
    this.templateScanner = templateScanner;
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.ARCHITECTURE_GOVERNANCE;
  }

  @Override
  public Iterable<? extends GeneratedResource> generate(ProjectBlueprint blueprint) {
    EnforcementMode mode = blueprint.getArchitecture().governance().mode();
    if (!mode.isEnabled()) {
      return List.of();
    }

    ProjectLayout layout = blueprint.getArchitecture().layout();
    String templateRoot = resolveTemplateRoot(layout, mode);

    List<String> templatePaths = scanTemplates(templateRoot);
    if (templatePaths.isEmpty()) {
      return List.of();
    }

    boolean webSelected = isWebSelected(blueprint);

    PackageName pkg = blueprint.getMetadata().packageName();
    String packagePath = pkg.value().replace('.', '/');

    Path outBase =
        Paths.get(SRC_TEST_JAVA)
            .resolve(packagePath)
            .resolve(OUT_SEGMENT_ARCHITECTURE)
            .resolve(OUT_SEGMENT_ARCH_UNIT);

    Map<String, Object> model = Map.of(MODEL_KEY_PROJECT_PACKAGE_NAME, pkg.value());

    List<String> renderableTemplates =
        templatePaths.stream()
            .filter(p -> p.endsWith(JAVA_FTL_SUFFIX))
            .filter(p -> !shouldSkipTemplateBecauseWebNotSelected(p, webSelected))
            .toList();

    List<GeneratedResource> generated = new ArrayList<>(renderableTemplates.size());

    for (String fullTemplatePath : renderableTemplates) {
      String relativeUnderRoot = fullTemplatePath.substring(templateRoot.length() + 1);
      String javaRelative = stripSuffix(relativeUnderRoot);

      Path outPath = outBase.resolve(javaRelative);
      generated.add(renderer.renderUtf8(outPath, fullTemplatePath, model));
    }

    return List.copyOf(generated);
  }

  private boolean shouldSkipTemplateBecauseWebNotSelected(
      String fullTemplatePath, boolean webSelected) {
    if (webSelected) {
      return false;
    }
    String fileName = fileNameOf(fullTemplatePath);
    return WEB_REQUIRED_GOVERNANCE_TEMPLATES.contains(fileName);
  }

  private String fileNameOf(String path) {
    int idx = path.lastIndexOf(PATH_SEPARATOR);
    if (idx < 0) {
      return path;
    }
    return path.substring(idx + 1);
  }

  private boolean isWebSelected(ProjectBlueprint bp) {
    var deps = bp.getDependencies();
    return deps != null
        && !deps.isEmpty()
        && deps.asList().stream().anyMatch(WEB_STARTER.matches());
  }

  private String resolveTemplateRoot(ProjectLayout layout, EnforcementMode mode) {
    String basePath = artifactSpec.basePath();
    String normalizedBase =
        basePath.endsWith(PATH_SEPARATOR) ? basePath.substring(0, basePath.length() - 1) : basePath;

    return normalizedBase
        + PATH_SEPARATOR
        + GOV_ROOT_DIR
        + PATH_SEPARATOR
        + layout.key()
        + PATH_SEPARATOR
        + mode.key();
  }

  private List<String> scanTemplates(String templateRoot) {
    try {
      return templateScanner.scan(templateRoot);
    } catch (TemplateScanException e) {
      throw new ArchitectureGovernanceTemplatesScanException(templateRoot, e);
    }
  }

  private String stripSuffix(String value) {
    if (value.endsWith(FTL_SUFFIX)) {
      return value.substring(0, value.length() - FTL_SUFFIX.length());
    }
    return value;
  }
}
