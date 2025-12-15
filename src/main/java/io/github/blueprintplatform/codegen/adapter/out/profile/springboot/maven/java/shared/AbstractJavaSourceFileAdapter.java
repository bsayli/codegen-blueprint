package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.shared;

import static java.util.Map.entry;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.TemplateSpec;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.adapter.shared.naming.StringCaseFormatter;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class AbstractJavaSourceFileAdapter implements ArtifactPort {

  private static final String KEY_PROJECT_PACKAGE = "projectPackageName";
  private static final String KEY_CLASS_NAME = "className";
  private static final String JAVA_FILE_EXTENSION = ".java";

  private static final String PACKAGE_PATH_DELIMITER = ".";
  private static final String FILE_PATH_DELIMITER = "/";

  private final TemplateRenderer renderer;
  private final ArtifactSpec artifactSpec;
  private final StringCaseFormatter stringCaseFormatter;

  protected AbstractJavaSourceFileAdapter(
      TemplateRenderer renderer,
      ArtifactSpec artifactSpec,
      StringCaseFormatter stringCaseFormatter) {
    this.renderer = renderer;
    this.artifactSpec = artifactSpec;
    this.stringCaseFormatter = stringCaseFormatter;
  }

  @Override
  public final Iterable<? extends GeneratedResource> generate(ProjectBlueprint blueprint) {
    String className = buildClassName(blueprint);
    PackageName packageName = blueprint.getMetadata().packageName();

    Map<String, Object> model =
        Map.ofEntries(
            entry(KEY_PROJECT_PACKAGE, packageName.value()), entry(KEY_CLASS_NAME, className));

    TemplateSpec templateSpec = artifactSpec.templates().getFirst();
    Path baseDir = Path.of(templateSpec.outputPath());
    String templateName = artifactSpec.basePath() + templateSpec.template();

    String packagePath = packageName.value().replace(PACKAGE_PATH_DELIMITER, FILE_PATH_DELIMITER);
    Path outPath = baseDir.resolve(packagePath).resolve(className + JAVA_FILE_EXTENSION);

    GeneratedResource file = renderer.renderUtf8(outPath, templateName, model);
    return List.of(file);
  }

  protected String pascal(String value) {
    return stringCaseFormatter.toPascalCase(value);
  }

  protected abstract String buildClassName(ProjectBlueprint blueprint);
}
