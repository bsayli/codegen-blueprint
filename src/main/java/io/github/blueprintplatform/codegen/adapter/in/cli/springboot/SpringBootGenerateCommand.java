package io.github.blueprintplatform.codegen.adapter.in.cli.springboot;

import io.github.blueprintplatform.codegen.adapter.in.cli.CliProjectRequest;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.dependency.SpringBootDependencyAlias;
import io.github.blueprintplatform.codegen.application.usecase.project.CreateProjectResult;
import io.github.blueprintplatform.codegen.application.usecase.project.CreateProjectUseCase;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "springboot",
    mixinStandardHelpOptions = true,
    description = "Generate a Spring Boot project scaffold")
public class SpringBootGenerateCommand implements Callable<Integer> {

  private static final Logger log = LoggerFactory.getLogger(SpringBootGenerateCommand.class);

  private final CreateProjectCommandMapper mapper;
  private final CreateProjectUseCase createProjectUseCase;

  @Option(
      names = {"--group-id"},
      required = true,
      description = "Maven groupId, for example: com.example")
  String groupId;

  @Option(
      names = {"--artifact-id"},
      required = true,
      description = "Maven artifactId, for example: demo-app")
  String artifactId;

  @Option(
      names = {"--name"},
      required = true,
      description = "Human-readable project name")
  String name;

  @Option(
      names = {"--description"},
      required = true,
      description = "Project description (min 10 characters)")
  String description;

  @Option(
      names = {"--package-name"},
      required = true,
      description = "Base package name, for example: com.example.demo")
  String packageName;

  @Option(
      names = {"--build-tool"},
      required = false,
      description = "Build tool. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "MAVEN")
  BuildTool buildTool;

  @Option(
      names = {"--language"},
      required = false,
      description = "Programming language. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "JAVA")
  Language language;

  @Option(
      names = {"--java"},
      required = false,
      description = "Java version. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "JAVA_21")
  JavaVersion javaVersion;

  @Option(
      names = {"--boot"},
      required = false,
      description = "Spring Boot version. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "V3_5_8")
  SpringBootVersion bootVersion;

  @Option(
      names = {"--dependency"},
      required = false,
      description = "Dependency alias, can be repeated. Available: ${COMPLETION-CANDIDATES}")
  List<SpringBootDependencyAlias> dependencies;

  @Option(
      names = {"--target-dir"},
      required = false,
      description = "Target directory for the generated project",
      defaultValue = ".")
  Path targetDirectory;

  public SpringBootGenerateCommand(
      CreateProjectCommandMapper mapper, CreateProjectUseCase createProjectUseCase) {
    this.mapper = mapper;
    this.createProjectUseCase = createProjectUseCase;
  }

  @Override
  public Integer call() {
    String profile = buildProfileKey(buildTool, language);

    List<String> dependencyCoordinates =
        dependencies == null
            ? List.of()
            : dependencies.stream().map(d -> d.groupId() + ":" + d.artifactId()).toList();

    CliProjectRequest request =
        new CliProjectRequest(
            groupId,
            artifactId,
            name,
            description,
            packageName,
            profile,
            dependencyCoordinates,
            targetDirectory);

    var command = mapper.from(request, buildTool, language, javaVersion, bootVersion);

    CreateProjectResult result = createProjectUseCase.handle(command);

    log.info("Spring Boot project generated successfully.");
    log.info("Archive path: {}", result.archivePath());

    return 0;
  }

  private String buildProfileKey(BuildTool buildTool, Language language) {
    String bt = buildTool.name().toLowerCase();
    String lang = language.name().toLowerCase();
    return "springboot-" + bt + "-" + lang;
  }
}
