package io.github.blueprintplatform.codegen.adapter.in.cli.springboot;

import io.github.blueprintplatform.codegen.adapter.in.cli.mapper.CreateProjectCommandMapper;
import io.github.blueprintplatform.codegen.adapter.in.cli.request.CliProjectRequest;
import io.github.blueprintplatform.codegen.adapter.in.cli.request.model.*;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option.*;
import io.github.blueprintplatform.codegen.application.port.in.project.CreateProjectPort;
import io.github.blueprintplatform.codegen.application.port.in.project.model.CreateProjectResult;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "springboot",
    mixinStandardHelpOptions = true,
    description = "Generate a Spring Boot project scaffold (standard or hexagonal layout)")
public class SpringBootGenerateCommand implements Callable<Integer> {

  private static final Logger log = LoggerFactory.getLogger(SpringBootGenerateCommand.class);
  private static final String SPRING_BOOT_FRAMEWORK_KEY = "spring-boot";

  private final CreateProjectCommandMapper mapper;
  private final CreateProjectPort createProjectPort;

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
      defaultValue = "maven")
  SpringBootBuildToolOption buildTool;

  @Option(
      names = {"--language"},
      required = false,
      description = "Programming language. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "java")
  SpringBootLanguageOption language;

  @Option(
      names = {"--java"},
      required = false,
      description = "Java version. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "21")
  SpringBootJavaVersionOption javaVersion;

  @Option(
      names = {"--boot"},
      required = false,
      description = "Spring Boot version. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "3.5")
  SpringBootVersionOption bootVersion;

  @Option(
      names = {"--layout"},
      required = false,
      description =
          "Project layout. Valid values: ${COMPLETION-CANDIDATES}. "
              + "standard = layered packages (controller/service/repository/domain/config), "
              + "hexagonal = ports & adapters structure.",
      defaultValue = "standard")
  SpringBootLayoutOption layout;

  @Option(
      names = {"--enforcement"},
      required = false,
      description = "Architecture enforcement mode. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "basic")
  SpringBootArchitectureEnforcementOption enforcementMode;

  @Option(
      names = {"--dependency"},
      required = false,
      description = "Dependency alias, can be repeated. Available: ${COMPLETION-CANDIDATES}")
  List<SpringBootDependencyOption> dependencies;

  @Option(
      names = {"--sample-code"},
      required = false,
      description = "Sample code level. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "none")
  SpringBootSampleCodeOption sampleCode;

  @Option(
      names = {"--target-dir"},
      required = false,
      description = "Target directory for the generated project",
      defaultValue = ".")
  Path targetDirectory;

  public SpringBootGenerateCommand(
      CreateProjectCommandMapper mapper, CreateProjectPort createProjectPort) {
    this.mapper = mapper;
    this.createProjectPort = createProjectPort;
  }

  @Override
  public Integer call() {

    var metadata = new CliProjectMetadata(groupId, artifactId, name, description, packageName);

    var techStack = new CliTechStack(SPRING_BOOT_FRAMEWORK_KEY, buildTool.key(), language.key());

    var runtimeTargetParams =
        Map.of(
            CliRuntimeTargetKeys.PARAM_JAVA_VERSION, javaVersion.value(),
            CliRuntimeTargetKeys.PARAM_SPRING_BOOT_VERSION, bootVersion.value());

    var runtimeTarget =
        new CliRuntimeTarget(CliRuntimeTargetKeys.TYPE_SPRING_BOOT_JVM, runtimeTargetParams);

    var architecture =
        new CliArchitectureSpec(layout.key(), enforcementMode.key(), sampleCode.key());

    List<CliDependency> cliDependencies = List.of();
    if (dependencies != null) {
      cliDependencies =
          dependencies.stream()
              .map(d -> new CliDependency(d.groupId(), d.artifactId(), null, null))
              .toList();
    }

    var request =
        new CliProjectRequest(
            metadata, techStack, runtimeTarget, architecture, cliDependencies, targetDirectory);

    var command = mapper.from(request);

    CreateProjectResult result = createProjectPort.handle(command);

    log.info("Spring Boot project generated successfully.");
    log.info("Archive path: {}", result.archivePath());

    return 0;
  }
}
