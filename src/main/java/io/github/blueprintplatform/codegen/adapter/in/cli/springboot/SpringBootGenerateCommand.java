package io.github.blueprintplatform.codegen.adapter.in.cli.springboot;

import io.github.blueprintplatform.codegen.adapter.in.cli.CliProjectRequest;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.dependency.SpringBootDependencyAlias;
import io.github.blueprintplatform.codegen.application.port.in.project.CreateProjectPort;
import io.github.blueprintplatform.codegen.application.port.in.project.dto.CreateProjectResponse;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.EnforcementMode;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeLevel;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
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
    description = "Generate a Spring Boot project scaffold (standard or hexagonal layout)")
public class SpringBootGenerateCommand implements Callable<Integer> {

  private static final Logger log = LoggerFactory.getLogger(SpringBootGenerateCommand.class);

  private final CreateProjectRequestMapper mapper;
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
  BuildTool buildTool;

  @Option(
      names = {"--language"},
      required = false,
      description = "Programming language. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "java")
  Language language;

  @Option(
      names = {"--java"},
      required = false,
      description = "Java version. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "21")
  JavaVersion javaVersion;

  @Option(
      names = {"--boot"},
      required = false,
      description = "Spring Boot version. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "3.5")
  SpringBootVersion bootVersion;

  @Option(
      names = {"--layout"},
      required = false,
      description = "Project layout. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "standard")
  ProjectLayout layout;

  @Option(
      names = {"--architecture", "--enforcement"},
      required = false,
      description = "Architecture enforcement mode. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "none")
  EnforcementMode enforcementMode;

  @Option(
      names = {"--dependency"},
      required = false,
      description = "Dependency alias, can be repeated. Available: ${COMPLETION-CANDIDATES}")
  List<SpringBootDependencyAlias> dependencies;

  @Option(
      names = {"--sample-code"},
      required = false,
      description = "Sample code level. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "none")
  SampleCodeLevel sampleCode;

  @Option(
      names = {"--target-dir"},
      required = false,
      description = "Target directory for the generated project",
      defaultValue = ".")
  Path targetDirectory;

  public SpringBootGenerateCommand(
      CreateProjectRequestMapper mapper, CreateProjectPort createProjectPort) {
    this.mapper = mapper;
    this.createProjectPort = createProjectPort;
  }

  @Override
  public Integer call() {
    String profile = buildProfileKey(buildTool, language);

    List<String> dependencyAliases =
        dependencies == null ? List.of() : dependencies.stream().map(Enum::name).toList();

    CliProjectRequest request =
        new CliProjectRequest(
            groupId,
            artifactId,
            name,
            description,
            packageName,
            profile,
            layout.key(),
            enforcementMode.key(),
            dependencyAliases,
            sampleCode.key(),
            targetDirectory);

    var command = mapper.from(request, buildTool, language, javaVersion, bootVersion);

    CreateProjectResponse result = createProjectPort.handle(command);

    log.info("Spring Boot project generated successfully.");
    log.info("Archive path: {}", result.archivePath());

    return 0;
  }

  private String buildProfileKey(BuildTool buildTool, Language language) {
    return Framework.SPRING_BOOT.key() + "-" + buildTool.key() + "-" + language.key();
  }
}
